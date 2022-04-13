package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.result.BasePage;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.WmsOutboundNoticeOrderInfoToMaterialMapper;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.service.IInventoryDetailService;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoToMaterialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库通知订单-物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class WmsOutboundNoticeOrderInfoToMaterialServiceImpl extends ServiceImpl<WmsOutboundNoticeOrderInfoToMaterialMapper, WmsOutboundNoticeOrderInfoToMaterial> implements IWmsOutboundNoticeOrderInfoToMaterialService {

    @Autowired
    private IInventoryDetailService iInventoryDetailService;

    @Autowired
    private WmsOutboundNoticeOrderInfoToMaterialMapper wmsOutboundNoticeOrderInfoToMaterialMapper;

    @Override
    public IPage<WmsOutboundNoticeOrderInfoToMaterialVO> selectPage(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO,
                                                                    Integer currentPage,
                                                                    Integer pageSize,
                                                                    HttpServletRequest req){

        Page<WmsOutboundNoticeOrderInfoToMaterialVO> page=new Page<WmsOutboundNoticeOrderInfoToMaterialVO>(currentPage,pageSize);
        IPage<WmsOutboundNoticeOrderInfoToMaterialVO> pageList= wmsOutboundNoticeOrderInfoToMaterialMapper.pageList(page, wmsOutboundNoticeOrderInfoToMaterialVO);
        return pageList;
    }

    @Override
    public List<WmsOutboundNoticeOrderInfoToMaterialVO> selectList(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO){
        return wmsOutboundNoticeOrderInfoToMaterialMapper.list(wmsOutboundNoticeOrderInfoToMaterialVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundNoticeOrderInfoToMaterialMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoToMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundNoticeOrderInfoToMaterialForExcel(paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult saveMaterial(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO) {
        //本次货品id
        List<String> detailIdList = wmsOutboundNoticeOrderInfoVO.getThisMaterialList().stream().map(x->String.valueOf(x.getInventoryDetailId())).distinct().collect(Collectors.toList());
        if (detailIdList.size() != wmsOutboundNoticeOrderInfoVO.getThisMaterialList().size()){
            return BaseResult.error("存在重复货品，请检查后提交！");
        }
        wmsOutboundNoticeOrderInfoToMaterialMapper.delteByOrderNumberAndMaterialCode(wmsOutboundNoticeOrderInfoVO.getOrderNumber(), null, null, CurrentUserUtil.getUsername());
        if (CollUtil.isNotEmpty( wmsOutboundNoticeOrderInfoVO.getThisMaterialList())){
            List<WmsOutboundNoticeOrderInfoToMaterial> saveList = new ArrayList<>();
            wmsOutboundNoticeOrderInfoVO.getThisMaterialList().forEach(materials->{
                WmsOutboundNoticeOrderInfoToMaterial material = new WmsOutboundNoticeOrderInfoToMaterial();
                BeanUtils.copyProperties(materials,material);
                material.clearCreate();
                material.clearUpdate();
                material.setId(null);
                material.setOrderNumber(wmsOutboundNoticeOrderInfoVO.getOrderNumber());
                saveList.add(material);
            });
            this.saveBatch(saveList);
        }
        return BaseResult.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByOrderNumber(String orderNumber) {
        wmsOutboundNoticeOrderInfoToMaterialMapper.delteByOrderNumberAndMaterialCode(orderNumber, null,null, CurrentUserUtil.getUsername());
    }

    @Override
    public List<WmsOutboundNoticeOrderInfoToMaterialVO> getInventoryMetailDetailList(InventoryDetail inventoryDetail) {
        List<InventoryDetail> detailList = iInventoryDetailService.selectList(inventoryDetail);
        List<WmsOutboundNoticeOrderInfoToMaterialVO> materialList = new ArrayList<>();
        if (CollUtil.isNotEmpty(detailList)){
            detailList.forEach(detail -> {
                WmsOutboundNoticeOrderInfoToMaterialVO material = new WmsOutboundNoticeOrderInfoToMaterialVO();
                material.setMaterialName(detail.getMaterialName());
                material.setMaterialCode(detail.getMaterialCode());
                material.setMaterialSpecification(detail.getMaterialSpecification());
                material.setAccount(detail.getExistingCount().subtract(detail.getAllocationCount()).subtract(detail.getPickingCount()));
                material.setBatchCode(detail.getBatchCode());
                material.setMaterialProductionDate(detail.getMaterialProductionDate());
                material.setCustomField1(detail.getCustomField1());
                material.setCustomField2(detail.getCustomField2());
                material.setCustomField3(detail.getCustomField3());
                material.setInWarehouseNumber(detail.getInWarehouseNumber());
                material.setWeight(detail.getWeight());
                material.setVolume(detail.getVolume());
                material.setWarehouseLocationId(detail.getWarehouseLocationId());
                material.setWarehouseLocationCode(detail.getWarehouseLocationCode());
                material.setInventoryDetailId(detail.getId());
                materialList.add(material);

            });
        }
        return materialList;
    }

    @Override
    public BasePage<WmsOutboundNoticeOrderInfoToMaterialVO> selectInvenDetail(WmsOutboundNoticeOrderInfoToMaterialVO material, Integer currentPage, Integer pageSize) {
        InventoryDetail inventoryDetail = new InventoryDetail();
        if (StringUtils.isNotBlank(material.getInWarehouseNumber())){
            inventoryDetail.setInWarehouseNumber(material.getInWarehouseNumber());
        }
        if (StringUtils.isNotBlank(material.getWarehouseCode())){
            inventoryDetail.setWarehouseCode(material.getWarehouseCode());
        }
        if (StringUtils.isNotBlank(material.getOwerCode())){
            inventoryDetail.setOwerCode(material.getOwerCode());
        }
        if (StringUtils.isNotBlank(material.getWarehouseLocationCode())){
            inventoryDetail.setWarehouseLocationCode(material.getWarehouseLocationCode());
        }
        if (StringUtils.isNotBlank(material.getContainerCode())){
            inventoryDetail.setContainerCode(material.getContainerCode());
        }
        if (StringUtils.isNotBlank(material.getMaterialCode())){
            inventoryDetail.setMaterialCode(material.getMaterialCode());
        }
        if (StringUtils.isNotBlank(material.getMaterialName())){
            inventoryDetail.setMaterialName(material.getMaterialName());
        }
        IPage<InventoryDetail> iPage = iInventoryDetailService.selectMaterialPage(inventoryDetail,currentPage,pageSize,null);
        BasePage<WmsOutboundNoticeOrderInfoToMaterialVO> basePage = new BasePage<>();
        BeanUtils.copyProperties(iPage,basePage);
        List<InventoryDetail> inventoryDetailList = iPage.getRecords();
        if (CollUtil.isNotEmpty(inventoryDetailList)){
            List<WmsOutboundNoticeOrderInfoToMaterialVO> materialList = new ArrayList<>();
            for (InventoryDetail inventoryDetails:inventoryDetailList){
                if (inventoryDetails.getUsableCount().compareTo(BigDecimal.ZERO)<=0){
                    continue;
                }
                WmsOutboundNoticeOrderInfoToMaterialVO materialVO = new WmsOutboundNoticeOrderInfoToMaterialVO();
                materialVO.setOrderNumber(material.getOrderNumber());
                materialVO.setInventoryDetailId(inventoryDetails.getId());
                materialVO.setWarehouseLocationId(inventoryDetails.getWarehouseLocationId());
                materialVO.setWarehouseLocationCode(inventoryDetails.getWarehouseLocationCode());
                materialVO.setMaterialCode(inventoryDetails.getMaterialCode());
                materialVO.setMaterialName(inventoryDetails.getMaterialName());
                materialVO.setMaterialSpecification(inventoryDetails.getMaterialSpecification());
                materialVO.setAccount(inventoryDetails.getUsableCount());
                materialVO.setUnit(inventoryDetails.getUnit());
                materialVO.setWeight(inventoryDetails.getWeight());
                materialVO.setVolume(inventoryDetails.getVolume());
                materialVO.setContainerId(inventoryDetails.getContainerId());
                materialVO.setContainerCode(inventoryDetails.getContainerCode());
                materialVO.setBatchCode(inventoryDetails.getBatchCode());
                materialVO.setMaterialProductionDate(inventoryDetails.getMaterialProductionDate());
                materialVO.setCustomField1(inventoryDetails.getCustomField1());
                materialVO.setCustomField2(inventoryDetails.getCustomField2());
                materialVO.setCustomField3(inventoryDetails.getCustomField3());
                //入仓号
                materialVO.setInWarehouseNumber(inventoryDetails.getInWarehouseNumber());
                //仓库
                materialVO.setWarehouseCode(inventoryDetails.getWarehouseCode());
                materialVO.setWarehouseName(inventoryDetails.getWarehouseName());
                //货主
                materialVO.setOwerCode(inventoryDetails.getOwerCode());
                materialVO.setOwerName(inventoryDetails.getOwerName());
                //数量
                materialVO.setExistingCount(inventoryDetails.getExistingCount());
                materialVO.setAllocationCount(inventoryDetails.getAllocationCount());
                materialVO.setPickingCount(inventoryDetails.getPickingCount());
                if (inventoryDetails.getUpdateTime() == null){
                    materialVO.setInWarehouseTime(inventoryDetails.getCreateTime());
                }else {
                    materialVO.setInWarehouseTime(inventoryDetails.getUpdateTime());
                }
                materialVO.setInWarehouseTime(inventoryDetails.getInWarehouseTime());
                materialList.add(materialVO);
            }
            if (CollUtil.isNotEmpty(materialList)){
                basePage.setRecords(materialList);
            }
        }
        return basePage;
    }

    @Override
    public String getInWarehouseNumbers(String orderNumber) {
        LambdaQueryWrapper<WmsOutboundNoticeOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfoToMaterial::getOrderNumber,orderNumber);
        List<WmsOutboundNoticeOrderInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            List<String> numberList = materialList.stream().filter(x->StringUtils.isNotBlank(x.getInWarehouseNumber())).map(x->x.getInWarehouseNumber()).distinct().collect(Collectors.toList());
            return StringUtils.join(numberList, StrUtil.C_COMMA);
        }
        return null;
    }

    /**
     * @description 批量保存
     * @author  ciro
     * @date   2021/12/22 16:41
     * @param: addList  新增集合
     * @param: orderNumber  出库通知单编号
     * @param: isAdd  是否新增
     * @return: void
     **/
    private void addBatch(List<WmsOutboundNoticeOrderInfoToMaterialVO> addList,String orderNumber,boolean isAdd){
        List<WmsOutboundNoticeOrderInfoToMaterial> list = new ArrayList<>();
        addList.forEach(materials -> {
            WmsOutboundNoticeOrderInfoToMaterial material = new WmsOutboundNoticeOrderInfoToMaterial();
            BeanUtils.copyProperties(materials,material);
            material.setOrderNumber(orderNumber);
            list.add(material);
        });
        if (isAdd){
            this.saveBatch(list);
        }else {
            this.updateBatchById(list);
        }
    }






}
