package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.WmsOutboundNoticeOrderInfoMapper;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfo;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeDictVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoService;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoToMaterialService;
import com.jayud.wms.service.IWmsOutboundOrderInfoService;
import com.jayud.wms.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库通知单 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class WmsOutboundNoticeOrderInfoServiceImpl extends ServiceImpl<WmsOutboundNoticeOrderInfoMapper, WmsOutboundNoticeOrderInfo> implements IWmsOutboundNoticeOrderInfoService {

    @Autowired
    private IWmsOutboundNoticeOrderInfoToMaterialService wmsOutboundNoticeOrderInfoToMaterialService;
    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;


    @Autowired
    private WmsOutboundNoticeOrderInfoMapper wmsOutboundNoticeOrderInfoMapper;

    @Autowired
    private CodeUtils codeUtils;

    @Autowired
    private AuthClient authClient;

    @Override
    public IPage<WmsOutboundNoticeOrderInfoVO> selectPage(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO,
                                                          Integer currentPage,
                                                          Integer pageSize,
                                                          HttpServletRequest req){
//        if (wmsOutboundNoticeOrderInfoVO.getOwerIdList().isEmpty()||wmsOutboundNoticeOrderInfoVO.getOwerIdList().isEmpty()){
//            return new Page<>();
//        }
        wmsOutboundNoticeOrderInfoVO.setTenantCode(CurrentUserUtil.getUserTenantCode());
        Page<WmsOutboundNoticeOrderInfoVO> page=new Page<WmsOutboundNoticeOrderInfoVO>(currentPage,pageSize);
        IPage<WmsOutboundNoticeOrderInfoVO> pageList= wmsOutboundNoticeOrderInfoMapper.pageList(page, wmsOutboundNoticeOrderInfoVO);
        if (CollUtil.isNotEmpty(pageList.getRecords())){
            pageList.getRecords().forEach(info->{
                info.setInWarehouseNumber(wmsOutboundNoticeOrderInfoToMaterialService.getInWarehouseNumbers(info.getOrderNumber()));
            });
        }
        return pageList;
    }

    @Override
    public List<WmsOutboundNoticeOrderInfoVO> selectList(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO){
        return wmsOutboundNoticeOrderInfoMapper.list(wmsOutboundNoticeOrderInfoVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundNoticeOrderInfoMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundNoticeOrderInfoForExcel(paramMap);
    }

    @Override
    public WmsOutboundNoticeOrderInfoVO queryByCode(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO) {
        List<WmsOutboundNoticeOrderInfoVO> list = wmsOutboundNoticeOrderInfoMapper.list(wmsOutboundNoticeOrderInfoVO);
        if (!list.isEmpty()){
            wmsOutboundNoticeOrderInfoVO = list.get(0);
            //获取物料信息
            WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO = new WmsOutboundNoticeOrderInfoToMaterialVO();
            wmsOutboundNoticeOrderInfoToMaterialVO.setOrderNumber(wmsOutboundNoticeOrderInfoVO.getOrderNumber());
            List<WmsOutboundNoticeOrderInfoToMaterialVO> materialVOList = wmsOutboundNoticeOrderInfoToMaterialService.selectList(wmsOutboundNoticeOrderInfoToMaterialVO);
            wmsOutboundNoticeOrderInfoVO.setThisMaterialList(materialVOList);
            return wmsOutboundNoticeOrderInfoVO;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult saveInfo(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO) {
        if (CollectionUtil.isEmpty(wmsOutboundNoticeOrderInfoVO.getThisMaterialList())){
            return BaseResult.error("物料不能为空！");
        }
        if (!checkMaterial(wmsOutboundNoticeOrderInfoVO.getThisMaterialList())){
            return BaseResult.error("物料详情有误！");
        }
        boolean isAdd = true;
        if (ObjectUtil.isNull(wmsOutboundNoticeOrderInfoVO.getId())){
            wmsOutboundNoticeOrderInfoVO.setOrderNumber(codeUtils.getCodeByRule(CodeConStants.OUTBOUND_NOTICE_ORDER_NUMBER));
        }else{
            isAdd = false;
            if (wmsOutboundOrderInfoService.isChangeOrder(wmsOutboundNoticeOrderInfoVO.getOrderNumber())){
                return BaseResult.error(SysTips.IS_CHANGE_ORDER_ERROR);
            }
        }
        BaseResult result = BaseResult.ok();
        if (CollUtil.isNotEmpty(wmsOutboundNoticeOrderInfoVO.getThisMaterialList())) {
            result = wmsOutboundNoticeOrderInfoToMaterialService.saveMaterial(wmsOutboundNoticeOrderInfoVO);
        }
        if (result.isSuccess()){
            if (isAdd){
                this.save(wmsOutboundNoticeOrderInfoVO);
            }else {
                this.updateById(wmsOutboundNoticeOrderInfoVO);
            }
        }else {
            return result;
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delById(long id,String orderNumber) {
        this.removeById(id);
        wmsOutboundNoticeOrderInfoToMaterialService.delByOrderNumber(orderNumber);
    }

    @Override
    public void changeAllAccount(String orderNumber) {
        WmsOutboundNoticeOrderInfoToMaterialVO material = new WmsOutboundNoticeOrderInfoToMaterialVO();
        material.setOrderNumber(orderNumber);
        List<WmsOutboundNoticeOrderInfoToMaterialVO> materialList = wmsOutboundNoticeOrderInfoToMaterialService.selectList(material);
        //数量
        BigDecimal allAccount = new BigDecimal(0);
        //重量
        BigDecimal allHeight = new BigDecimal(0);
        //体积
        BigDecimal allVolume = new BigDecimal(0);
        for (WmsOutboundNoticeOrderInfoToMaterialVO materialVO : materialList){
            allAccount = allAccount.add(materialVO.getAccount());
            allHeight = allHeight.add(materialVO.getAccount().multiply(materialVO.getWeight()));
            allVolume = allVolume.add(materialVO.getAccount().multiply(materialVO.getVolume()));
        }
        LambdaQueryWrapper<WmsOutboundNoticeOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfo::getOrderNumber,orderNumber);
        lambdaQueryWrapper.eq(WmsOutboundNoticeOrderInfo::getIsDeleted,false);
        WmsOutboundNoticeOrderInfo info = this.getOne(lambdaQueryWrapper);
        if (info != null){
            info.setAllCount(allAccount);
            info.setAllHeight(allHeight);
            info.setAllVolume(allVolume);
            this.updateById(info);
        }
    }

    @Override
    public WmsOutboundNoticeDictVO getDictmsg() {
        WmsOutboundNoticeDictVO wmsOutboundNoticeDictVO = new WmsOutboundNoticeDictVO();
        BaseResult<List<SysDictItem>> documentResult = authClient.selectItemByDictCode("outboundNoticeDocumentType");
        if (documentResult.isSuccess()){
            wmsOutboundNoticeDictVO.setOrderTypeDict(documentResult.getResult());
        }
        BaseResult<List<SysDictItem>> unitResult = authClient.selectItemByDictCode("outboundNoticeOrderUnitType");
        if (unitResult.isSuccess()){
            wmsOutboundNoticeDictVO.setUnitDict(unitResult.getResult());
        }
        BaseResult<List<SysDictItem>> carTypeResult = authClient.selectItemByDictCode("outboundNoticeOrderCarType");
        if (carTypeResult.isSuccess()){
            wmsOutboundNoticeDictVO.setCarTypeDict(carTypeResult.getResult());
        }
        BaseResult<List<SysDictItem>> noticeOrderStatusTypeResult = authClient.selectItemByDictCode("outboundNoticeOrderStatusType");
        if (noticeOrderStatusTypeResult.isSuccess()){
            wmsOutboundNoticeDictVO.setStatusTypeDict(noticeOrderStatusTypeResult.getResult());
        }
        BaseResult<List<SysDictItem>> orderStatusTypeResult = authClient.selectItemByDictCode("outboundOrderStatusType");
        if (orderStatusTypeResult.isSuccess()){
            wmsOutboundNoticeDictVO.setOrderStatusTypeDict(orderStatusTypeResult.getResult());
        }
        BaseResult<List<SysDictItem>> serviceTypeResult = authClient.selectItemByDictCode("outboundOrderServiceType");
        if (serviceTypeResult.isSuccess()){
            wmsOutboundNoticeDictVO.setServiceTypeDict(serviceTypeResult.getResult());
        }
        BaseResult<List<SysDictItem>> reviewStatusTypeResult = authClient.selectItemByDictCode("shippingReviewOrderStatusType");
        if (reviewStatusTypeResult.isSuccess()){
            wmsOutboundNoticeDictVO.setReviewStatusTypeDict(reviewStatusTypeResult.getResult());
        }
        return wmsOutboundNoticeDictVO;
    }

    private boolean checkMaterial(List<WmsOutboundNoticeOrderInfoToMaterialVO> materialVOList){
        for (int i=0;i< materialVOList.size();i++){
            WmsOutboundNoticeOrderInfoToMaterialVO vo = materialVOList.get(i);
            if (StringUtils.isBlank(vo.getUnit())||vo.getAccount() == null){
                return false;
            }
        }
        return true;
    }

}
