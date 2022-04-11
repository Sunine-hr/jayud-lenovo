package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.jayud.wms.model.bo.LargeScreen.WarehouseForm;
import com.jayud.wms.model.enums.InboundQualityStatusEnum;
import com.jayud.wms.model.enums.OutboundOrdertSatus;
import com.jayud.wms.model.enums.OutboundShippingReviewSatusEnum;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.LargeScreen.OrderCountVO;
import com.jayud.wms.model.vo.LargeScreen.OrderMsgVO;
import com.jayud.wms.model.vo.LargeScreen.WareLocationUseStatusVO;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.wms.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ciro
 * @date 2022/4/11 10:22
 * @description: 大屏看板
 */
@Service
public class LargeScreenServiceImpl implements LargeScreenService {

    @Autowired
    private IWarehouseLocationService warehouseLocationService;

    @Autowired
    private IInventoryDetailService inventoryDetailService;

    @Autowired
    private IReceiptNoticeService receiptNoticeService;

    @Autowired
    private IReceiptService receiptService;

    @Autowired
    private IQualityInspectionService qualityInspectionService;

    @Autowired
    private IWmsOutboundNoticeOrderInfoService wmsOutboundNoticeOrderInfoService;

    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;

    @Autowired
    private IWmsOutboundShippingReviewInfoService wmsOutboundShippingReviewInfoService;



    @Override
    public WareLocationUseStatusVO getWarehouseLocationUserStatus(WarehouseForm warehouseForm) {
        Map<String,Boolean> locationStatusMap = new HashMap<>();
        InventoryDetail inventoryDetails = new InventoryDetail();
        BeanUtils.copyProperties(warehouseForm,inventoryDetails);
        List<InventoryDetail> inventoryDetailList = inventoryDetailService.selectList(inventoryDetails);
        if (CollUtil.isEmpty(inventoryDetailList)){
            inventoryDetailList.forEach(detail->{
                //判断分配量、拣货量是否都为0
                if (detail.getAllocationCount().compareTo(BigDecimal.ZERO) == 0 && detail.getPickingCount().compareTo(BigDecimal.ZERO) == 0){
                    locationStatusMap.put(detail.getWarehouseLocationCode(),true);
                }else {
                    locationStatusMap.put(detail.getWarehouseLocationCode(),false);
                }
            });
        }
        WarehouseLocation warehouseLocations = new WarehouseLocation();
        BeanUtils.copyProperties(warehouseForm,warehouseLocations);
        List<WarehouseLocationVO> locationList = warehouseLocationService.selectList(warehouseLocations);
        Integer useCount = 0;
        Integer unUseCount = 0;
        if (CollUtil.isEmpty(locationList)){
            if (locationStatusMap.isEmpty()){
                unUseCount = locationList.size();
            }else {
                for (WarehouseLocationVO location : locationList){
                    if (locationStatusMap.containsKey(location.getCode())){
                        if (locationStatusMap.get(location.getCode())){
                            unUseCount++;
                        }else {
                            useCount++;
                        }
                    }else {
                        unUseCount ++;
                    }
                }
            }
        }
        WareLocationUseStatusVO returnMsg = new WareLocationUseStatusVO();
        returnMsg.setUseCount(useCount);
        returnMsg.setUnUseCount(unUseCount);
        return returnMsg;
    }

    @Override
    public OrderCountVO getFinishOrderCount(WarehouseForm warehouseForm) {
        OrderCountVO orderCount = new OrderCountVO();
        warehouseForm.setIsFinish(true);
        getInBoundFinishOrderCount(warehouseForm,orderCount);
        getInBoundQualityCount(warehouseForm,orderCount);
        getOutBoundFinishOrderCount(warehouseForm,orderCount);
        getOutBoundReviewCount(warehouseForm,orderCount);
        return orderCount;
    }

    @Override
    public OrderCountVO getUnFinishOrderCount(WarehouseForm warehouseForm) {
        OrderCountVO orderCount = new OrderCountVO();
        warehouseForm.setIsFinish(false);
        getInBoundUnFinishOrderCount(warehouseForm,orderCount);
        getInBoundQualityCount(warehouseForm,orderCount);
        getOutBoundUnFinishOrderCount(warehouseForm,orderCount);
        getOutBoundReviewCount(warehouseForm,orderCount);
        return orderCount;
    }

    @Override
    public List<OrderMsgVO> getOrderMsg(WarehouseForm warehouseForm) {
        return null;
    }


    /**
     * @description 获取入库通知单、入库单完成数量
     * @author  ciro
     * @date   2022/4/11 11:40
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getInBoundFinishOrderCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<Receipt> receiptList = getReciptList(warehouseForm);
        int finishCount = 0;
        if (CollUtil.isEmpty(receiptList)){
            finishCount = receiptList.stream().filter(x->x.getStatus().equals(ReceiptStatusEnum.THREE.getCode())).collect(Collectors.toList()).size();
        }
        orderCountVO.setInBoundNoticeOrderFinishCount(finishCount);
        orderCountVO.setInBoundNoticeOrderFinishCount(finishCount);
    }

    /**
     * @description 获取入库通知单、入库单未完成数量
     * @author  ciro
     * @date   2022/4/11 15:04
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getInBoundUnFinishOrderCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<ReceiptNotice> noticeList = getReciptNoticeList(warehouseForm);
        List<Receipt> receiptList = getReciptList(warehouseForm);
        //未完成入库通知单数量
        int unfinishNoticeOrderCount = 0;
        //未完成入库单数量
        int unfinishOrderCount = 0;
        List<String> finishNoticeList = new ArrayList<>();
        if (CollUtil.isEmpty(receiptList)){
            finishNoticeList = receiptList.stream().filter(x->x.getStatus().equals(ReceiptStatusEnum.THREE.getCode())).map(x->x.getReceiptNum()).collect(Collectors.toList());
            unfinishOrderCount = receiptList.stream().filter(x->!x.getStatus().equals(ReceiptStatusEnum.THREE.getCode())).map(x->x.getReceiptNum()).collect(Collectors.toList()).size();
        }
        if (CollUtil.isNotEmpty(noticeList)){
            List<String> finalFinishNoticeList = finishNoticeList;
            unfinishNoticeOrderCount = noticeList.stream().filter(x-> !finalFinishNoticeList.contains(x.getReceiptNoticeNum())).collect(Collectors.toList()).size();
        }
        orderCountVO.setInBoundOrderUnFinishCount(unfinishOrderCount);
        orderCountVO.setInBoundNoticeOrderUnFinishCount(unfinishNoticeOrderCount);
    }

    /**
     * @description 获取入库-质检完成数量
     * @author  ciro
     * @date   2022/4/11 13:32
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getInBoundQualityCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<QualityInspection> qualityInspectionList = getQualityList(warehouseForm);
        int qualityFinishCount = 0;
        int qualityUnFinishCount = 0;
        if (CollUtil.isNotEmpty(qualityInspectionList)){
            if (warehouseForm.getIsFinish()) {
                qualityFinishCount = qualityInspectionList.stream().filter(x -> x.getStatus().equals(InboundQualityStatusEnum.FINISH.getCode())).collect(Collectors.toList()).size();
            }else {
                qualityUnFinishCount = qualityInspectionList.stream().filter(x -> !x.getStatus().equals(InboundQualityStatusEnum.FINISH.getCode())).collect(Collectors.toList()).size();
            }
        }
        orderCountVO.setInBoundQualityFinishCount(qualityFinishCount);
        orderCountVO.setInBoundQualityUnFinishCount(qualityUnFinishCount);
    }

    /**
     * @description 获取出库通知单、出库单完成数量
     * @author  ciro
     * @date   2022/4/11 13:50
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getOutBoundFinishOrderCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<WmsOutboundOrderInfoVO> infoList = getOutboundOrderList(warehouseForm);
        int orderFinishCount = 0;
        if (CollUtil.isNotEmpty(infoList)){
            orderFinishCount = infoList.stream().filter(x->x.getOrderStatusType().equals(OutboundOrdertSatus.ISSUED.getType())).collect(Collectors.toList()).size();
        }
        orderCountVO.setOutoundNoticeOrderFinishCount(orderFinishCount);
        orderCountVO.setOutBoundOrderFinishCount(orderFinishCount);
    }

    /**
     * @description 获取未完成出库通知单、出库单数量
     * @author  ciro
     * @date   2022/4/11 14:44
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getOutBoundUnFinishOrderCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<WmsOutboundNoticeOrderInfoVO> noticeList = getOutboundNoticeList(warehouseForm);
        List<WmsOutboundOrderInfoVO> infoList = getOutboundOrderList(warehouseForm);
        //未完成出库通知单数量
        int unfinishNoticeCount = 0;
        //未完成出库单数量
        int unfinishOrderCount = 0;
        //完成出库通知单集合
        List<String> finishNoticeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(infoList)){
            finishNoticeList = infoList.stream().filter(x->x.getOrderStatusType().equals(OutboundOrdertSatus.ISSUED)).map(x->x.getNoticeOrderNumber()).collect(Collectors.toList());
            unfinishOrderCount = infoList.stream().filter(x->!x.getOrderStatusType().equals(OutboundOrdertSatus.ISSUED)).collect(Collectors.toList()).size();
        }
        if (CollUtil.isNotEmpty(noticeList)){
            List<String> finalFinishNoticeList = finishNoticeList;
            if (CollUtil.isNotEmpty(finishNoticeList)) {
                unfinishNoticeCount = noticeList.stream().filter(x -> !finalFinishNoticeList.contains(x.getOrderNumber())).collect(Collectors.toList()).size();
            }else {
                unfinishNoticeCount = noticeList.size();
            }
        }
        orderCountVO.setOutBoundOrderUnFinishCount(unfinishOrderCount);
        orderCountVO.setOutoundNoticeOrderUnFinishCount(unfinishNoticeCount);
    }


    /**
     * @description 获取出库-发运复核完成数量
     * @author  ciro
     * @date   2022/4/11 14:01
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getOutBoundReviewCount(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<WmsOutboundShippingReviewInfo> infoList = getOutboundReviewList(warehouseForm);
        int reviewFinishCount = 0;
        int reviewUnFinishCount = 0;
        if (CollUtil.isNotEmpty(infoList)){
            if (warehouseForm.getIsFinish()) {
                reviewFinishCount = infoList.stream().filter(x -> x.getOrderStatusType().equals(OutboundShippingReviewSatusEnum.REVIEWED.getStatus())).collect(Collectors.toList()).size();
            }else {
                reviewUnFinishCount = infoList.stream().filter(x -> !x.getOrderStatusType().equals(OutboundShippingReviewSatusEnum.REVIEWED.getStatus())).collect(Collectors.toList()).size();
            }
        }
        orderCountVO.setOutBoundShippingReviewFinishCount(reviewFinishCount);
        orderCountVO.setOutBoundShippingReviewUnFinishCount(reviewUnFinishCount);
    }

    /**
     * @description 获取入库通知单
     * @author  ciro
     * @date   2022/4/11 15:03
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.po.ReceiptNotice>
     **/
    private List<ReceiptNotice> getReciptNoticeList(WarehouseForm warehouseForm){
        ReceiptNotice receiptNotices = new ReceiptNotice();
        BeanUtils.copyProperties(warehouseForm,receiptNotices);
        List<ReceiptNotice> noticeList = receiptNoticeService.selectList(receiptNotices);
        return noticeList;
    }

    /**
     * @description 获取入库单
     * @author  ciro
     * @date   2022/4/11 15:03
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.po.Receipt>
     **/
    private List<Receipt> getReciptList(WarehouseForm warehouseForm){
        Receipt receipts = new Receipt();
        BeanUtils.copyProperties(warehouseForm,receipts);
        List<Receipt> receiptList = receiptService.selectList(receipts);
        return receiptList;
    }

    /**
     * @description 获取质检单
     * @author  ciro
     * @date   2022/4/11 15:05
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.po.QualityInspection>
     **/
    private List<QualityInspection> getQualityList(WarehouseForm warehouseForm){
        QualityInspection qualityInspections = new QualityInspection();
        BeanUtils.copyProperties(warehouseForm,qualityInspections);
        List<QualityInspection> qualityInspectionList = qualityInspectionService.selectList(qualityInspections);
        return qualityInspectionList;
    }

    /**
     * @description 获取出库通知单
     * @author  ciro
     * @date   2022/4/11 15:07
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO>
     **/
    private List<WmsOutboundNoticeOrderInfoVO> getOutboundNoticeList(WarehouseForm warehouseForm){
        WmsOutboundNoticeOrderInfoVO noticeInfos = new WmsOutboundNoticeOrderInfoVO();
        BeanUtils.copyProperties(warehouseForm,noticeInfos);
        List<WmsOutboundNoticeOrderInfoVO> noticeList = wmsOutboundNoticeOrderInfoService.selectList(noticeInfos);
        return noticeList;
    }

    /**
     * @description 获取出库单
     * @author  ciro
     * @date   2022/4/11 15:07
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.vo.WmsOutboundOrderInfoVO>
     **/
    private List<WmsOutboundOrderInfoVO> getOutboundOrderList(WarehouseForm warehouseForm){
        WmsOutboundOrderInfoVO infos = new WmsOutboundOrderInfoVO();
        BeanUtils.copyProperties(warehouseForm,infos);
        List<WmsOutboundOrderInfoVO> infoList = wmsOutboundOrderInfoService.selectList(infos);
        return infoList;
    }

    /**
     * @description 获取发运复核数据
     * @author  ciro
     * @date   2022/4/11 15:12
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    private List<WmsOutboundShippingReviewInfo> getOutboundReviewList(WarehouseForm warehouseForm){
        WmsOutboundShippingReviewInfo info = new WmsOutboundShippingReviewInfo();
        BeanUtils.copyProperties(warehouseForm,info);
        List<WmsOutboundShippingReviewInfo> infoList = wmsOutboundShippingReviewInfoService.selectList(info);
        return infoList;
    }

}
