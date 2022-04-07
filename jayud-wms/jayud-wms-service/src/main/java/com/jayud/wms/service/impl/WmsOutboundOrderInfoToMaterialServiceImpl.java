package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.bo.ConfirmInformationForm;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.enums.OutboundOrdertSatus;
import com.jayud.wms.mapper.WmsOutboundOrderInfoToMaterialMapper;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import com.jayud.wms.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库单-物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class WmsOutboundOrderInfoToMaterialServiceImpl extends ServiceImpl<WmsOutboundOrderInfoToMaterialMapper, WmsOutboundOrderInfoToMaterial> implements IWmsOutboundOrderInfoToMaterialService {

    @Autowired
    private IWmsOutboundNoticeOrderInfoToMaterialService wmsOutboundNoticeOrderInfoToMaterialService;
    @Autowired
    private IWmsMaterialPackingSpecsService wmsMaterialPackingSpecsService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;
    @Autowired
    private IWmsOutboundNoticeOrderInfoService wmsOutboundNoticeOrderInfoService;

    @Autowired
    private WmsOutboundOrderInfoToMaterialMapper wmsOutboundOrderInfoToMaterialMapper;

    @Override
    public IPage<WmsOutboundOrderInfoToMaterialVO> selectPage(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO,
                                                              Integer currentPage,
                                                              Integer pageSize,
                                                              HttpServletRequest req){

        Page<WmsOutboundOrderInfoToMaterialVO> page=new Page<WmsOutboundOrderInfoToMaterialVO>(currentPage,pageSize);
        IPage<WmsOutboundOrderInfoToMaterialVO> pageList= wmsOutboundOrderInfoToMaterialMapper.pageList(page, wmsOutboundOrderInfoToMaterialVO);
        return pageList;
    }

    @Override
    public List<WmsOutboundOrderInfoToMaterialVO> selectList(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO){
        List<WmsOutboundOrderInfoToMaterialVO> thisMaterialList = wmsOutboundOrderInfoToMaterialMapper.list(wmsOutboundOrderInfoToMaterialVO);
        if (CollUtil.isNotEmpty(thisMaterialList)){
            thisMaterialList.forEach(material -> {
                if(StringUtils.isNotBlank(material.getImgUrl())){
                    material.setIsHavePic(true);
                }else{
                    material.setIsHavePic(false);
                }
            });
        }
        return thisMaterialList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundOrderInfoToMaterialMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundOrderInfoToMaterialForExcel(paramMap);
    }

    @Override
    public List<WmsOutboundOrderInfoToMaterial> transferOut(String noticeOrderNumber,String orderNumber) {
        WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO = new WmsOutboundNoticeOrderInfoToMaterialVO();
        wmsOutboundNoticeOrderInfoToMaterialVO.setOrderNumber(noticeOrderNumber);
        List<WmsOutboundNoticeOrderInfoToMaterialVO> noticeList = wmsOutboundNoticeOrderInfoToMaterialService.selectList(wmsOutboundNoticeOrderInfoToMaterialVO);
        List<WmsOutboundOrderInfoToMaterial> saveList = new ArrayList<>();
        noticeList.forEach(notice -> {
            WmsOutboundOrderInfoToMaterial wmsOutboundOrderInfoToMaterial = new WmsOutboundOrderInfoToMaterial();
            BeanUtils.copyProperties(notice,wmsOutboundOrderInfoToMaterial);
            wmsOutboundOrderInfoToMaterial.setId(null);
            wmsOutboundOrderInfoToMaterial.clearCreate();
            wmsOutboundOrderInfoToMaterial.clearUpdate();
            wmsOutboundOrderInfoToMaterial.setOriginUnit(wmsOutboundOrderInfoToMaterial.getUnit());
            wmsOutboundOrderInfoToMaterial.setUnit("EA");
            wmsOutboundOrderInfoToMaterial.setOrderNumber(orderNumber);
            //转换最小单位
            wmsOutboundOrderInfoToMaterial.setRequirementAccount(getDistributionAccount(notice.getMaterialCode(),notice.getUnit(),notice.getAccount()));
            saveList.add(wmsOutboundOrderInfoToMaterial);
        });
        if (!saveList.isEmpty()){
            this.saveBatch(saveList);
        }
        return saveList;
    }

    @Override
    public void allocateInventory(List<WmsOutboundOrderInfoToMaterialVO> materialList) {
        List<WmsOutboundOrderInfoToMaterial> list = new ArrayList<>();
        materialList.forEach(materials -> {
            WmsOutboundOrderInfoToMaterial material = new WmsOutboundOrderInfoToMaterial();
            BeanUtils.copyProperties(materials,material);
            list.add(material);
        });
        this.updateBatchById(list);
    }

    @Override
    public List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode) {
        return this.baseMapper.queryScanInformation(orderNumber,materialCode);
    }

    @Override
    public List<WmsOutboundOrderInfoToMaterial> getOutboundOrderInfoToMaterialByWaveNumber(String orderNumber) {
        QueryWrapper<WmsOutboundOrderInfoToMaterial> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,orderNumber);
        queryWrapper.lambda().ne(WmsOutboundOrderInfoToMaterial::getStatusType,4);
        queryWrapper.lambda().eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,0);
        return this.list(queryWrapper);
    }



    @Override
    public BigDecimal getDistributionAccount(String materialCode, String unit, BigDecimal count){
        BigDecimal reCount = new BigDecimal(0);
        WmsMaterialPackingSpecs wmsMaterialPackingSpecs = new WmsMaterialPackingSpecs();
        if (StringUtils.isNotBlank(materialCode)){
            WmsMaterialBasicInfoVO basicInfoVO = wmsMaterialBasicInfoService.selectByCode(materialCode);
            if (basicInfoVO != null){
                wmsMaterialPackingSpecs.setMaterialBasicInfoId(basicInfoVO.getId());
            }
        }
        List<WmsMaterialPackingSpecs> specsList = wmsMaterialPackingSpecsService.selectList(wmsMaterialPackingSpecs);
        for (int i=0;i<specsList.size();i++){
            WmsMaterialPackingSpecs specs = specsList.get(i);
            if (StringUtils.isNotBlank(specs.getUnit())&&specs.getUnit().equals(unit)){
                reCount = count.multiply(BigDecimal.valueOf(specs.getAccount()));
            }
        }
        return reCount;
    }

    @Override
    public void finishOrder(List<String> orderNumberList) {
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(WmsOutboundOrderInfoToMaterial::getOrderNumber,orderNumberList);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        List<WmsOutboundOrderInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            materialList.forEach(material -> {
                material.setStatusType(OutboundOrdertSatus.ISSUED.getType());
            });
            this.updateBatchById(materialList);
        }
    }

    @Override
    public BaseResult<List<WmsOutboundOrderInfoToMaterialVO>> comfirmOutput(ConfirmInformationForm confirmInformationForm) {
        //确认id
        List<Long> ids = confirmInformationForm.getMaterialVoList().stream().map(x->x.getId()).collect(Collectors.toList());
        //图片
        Map<Long,String> picMap = confirmInformationForm.getMaterialVoList().stream().filter(x->StringUtils.isNotBlank(x.getImgUrl())).collect(Collectors.toMap(x->x.getId(),x->x.getImgUrl()));
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.in(WmsOutboundOrderInfoToMaterial::getId,ids);
        List<WmsOutboundOrderInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            Map<Long,BigDecimal> msg = materialList.stream().collect(Collectors.toMap(x->x.getInventoryDetailId(),x->x.getRequirementAccount()));
            BaseResult baseResult = inventoryDetailService.outputByMsg(msg);
            if (baseResult.isSuccess()){
                materialList.forEach(material->{
                    material.setStatusType(2);
                    if (picMap.containsKey(material.getId())){
                        material.setImgUrl(picMap.get(material.getId()));
                    }
                });
                this.updateBatchById(materialList);
                WmsOutboundOrderInfoToMaterialVO vo = new WmsOutboundOrderInfoToMaterialVO();
                vo.setOrderNumber(materialList.get(0).getOrderNumber());
                List<WmsOutboundOrderInfoToMaterialVO> lists = selectList(vo);
                changeComfirTime(materialList.get(0).getOrderNumber());
                return BaseResult.ok(lists);
            }
        }
        return BaseResult.ok();
    }

    @Override
    public String getInWarehouseNumbers(String orderNumber) {
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,orderNumber);
        List<WmsOutboundOrderInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            List<String> numberList = materialList.stream().filter(x->StringUtils.isNotBlank(x.getInWarehouseNumber())).map(x->x.getInWarehouseNumber()).collect(Collectors.toList());
            return StringUtils.join(numberList, StrUtil.C_COMMA);
        }
        return null;
    }

    /**
     * @description 修改确认数据
     * @author  ciro
     * @date   2022/4/7 16:24
     * @param: orderNumber
     * @return: void
     **/
    private void changeComfirTime(String orderNumber){
        LambdaQueryWrapper<WmsOutboundOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getOrderNumber,orderNumber);
        WmsOutboundOrderInfo info = wmsOutboundOrderInfoService.getOne(lambdaQueryWrapper);
        info.setComfirmId(CurrentUserUtil.getUserId());
        info.setComfirmName(CurrentUserUtil.getUserRealName());
        info.setRealDeliveryTime(LocalDateTime.now());
        wmsOutboundOrderInfoService.updateById(info);
        changeAllStatus(orderNumber);
    }

    private void changeAllStatus(String orderNumber){
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,orderNumber);
        List<WmsOutboundOrderInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            boolean isAll = true;
            for (WmsOutboundOrderInfoToMaterial material:materialList){
                if (material.getStatusType() != 2){
                    isAll = false;
                    break;
                }
            }
            if (isAll){
                changeInfoStatus(orderNumber);
            }
        }
    }

    /**
     * @description 修改出库单状态
     * @author  ciro
     * @date   2022/4/7 17:39
     * @param: orderNumber
     * @return: void
     **/
    public void changeInfoStatus(String orderNumber){
        LambdaQueryWrapper<WmsOutboundOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getOrderNumber,orderNumber);
        WmsOutboundOrderInfo info = wmsOutboundOrderInfoService.getOne(lambdaQueryWrapper);
        info.setOrderStatusType("2");
        wmsOutboundOrderInfoService.updateById(info);
        changeNoticeInfoStatus(info.getNoticeOrderNumber());
    }

    /**
     * @description 修改出库通知单状态
     * @author  ciro
     * @date   2022/4/7 17:39
     * @param: noticOrderNumber
     * @return: void
     **/
    public void changeNoticeInfoStatus(String noticOrderNumber){
        LambdaQueryWrapper<WmsOutboundNoticeOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfo::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfo::getOrderNumber,noticOrderNumber);
        WmsOutboundNoticeOrderInfo info = wmsOutboundNoticeOrderInfoService.getOne(lambdaQueryWrapper);
        info.setOrderStatusType("2");
        wmsOutboundNoticeOrderInfoService.updateById(info);
    }


}
