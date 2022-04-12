package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.QualityInspectionMapper;
import com.jayud.wms.mapper.ReceiptMapper;
import com.jayud.wms.mapper.WmsOutboundOrderInfoMapper;
import com.jayud.wms.mapper.WmsOutboundShippingReviewInfoMapper;
import com.jayud.wms.model.bo.LargeScreen.WarehouseForm;
import com.jayud.wms.model.enums.*;
import com.jayud.wms.model.enums.LargeScreen.OrderTypeEnum;
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
import java.util.*;
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

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private QualityInspectionMapper qualityInspectionMapper;

    @Autowired
    private WmsOutboundOrderInfoMapper wmsOutboundOrderInfoMapper;

    @Autowired
    private WmsOutboundShippingReviewInfoMapper wmsOutboundShippingReviewInfoMapper;



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
        orderCount.setCoutList(new ArrayList<>());
        getInBoundFinishOrderCount(warehouseForm,orderCount);
        getInBoundQualityMsg(warehouseForm,orderCount);
        getOutBoundFinishOrderCount(warehouseForm,orderCount);
        getOutBoundReviewMsg(warehouseForm,orderCount);
        return orderCount;
    }


    @Override
    public OrderCountVO getUnfinsihOrderMsg(WarehouseForm warehouseForm) {
        OrderCountVO orderCount = new OrderCountVO();
        orderCount.setMsgList(new ArrayList<>());
        orderCount.setCoutList(new ArrayList<>());
        getInBoundUnFinishOrderMsg(warehouseForm,orderCount);
        getInBoundQualityMsg(warehouseForm,orderCount);
        getOutBoundUnFinishOrderMsg(warehouseForm,orderCount);
        getOutBoundFinishOrderCount(warehouseForm,orderCount);
        return orderCount;
    }

    @Override
    public OrderCountVO getOrderLineMsg(WarehouseForm warehouseForm) {
        OrderCountVO orderCountVO = new OrderCountVO();
        int lastDay = DateUtil.dayOfMonth(Convert.toDate(warehouseForm.getYearMonth()+"-01"));
        orderCountVO.setUnfinishDateList(initLineDate(lastDay));
        orderCountVO.setInBoundOrderCountList(intiLineDataList(receiptMapper.selectFinishCountByTime(CurrentUserUtil.getUserTenantCode(),warehouseForm.getYearMonth()),orderCountVO.getUnfinishDateList().size()));
        orderCountVO.setInBoundQualityCountList(intiLineDataList(qualityInspectionMapper.selectFinishCountByTime(CurrentUserUtil.getUserTenantCode(),warehouseForm.getYearMonth()),orderCountVO.getUnfinishDateList().size()));
        orderCountVO.setOutBoundOrderCountList(intiLineDataList(wmsOutboundOrderInfoMapper.selectFinishCountByTime(CurrentUserUtil.getUserTenantCode(),warehouseForm.getYearMonth()),orderCountVO.getUnfinishDateList().size()));
        orderCountVO.setShippingReviewCountList(intiLineDataList(wmsOutboundShippingReviewInfoMapper.selectFinishCountByTime(CurrentUserUtil.getUserTenantCode(),warehouseForm.getYearMonth()),orderCountVO.getUnfinishDateList().size()));
        return orderCountVO;
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
        int finishCount = getFinishReceiptList(warehouseForm).size();
        initOrderCoutList(orderCountVO,OrderTypeEnum.RECEIPT_NOTICE.getDesc()+"完成总量",finishCount);
        initOrderCoutList(orderCountVO,OrderTypeEnum.RECEIPT.getDesc()+"完成总量",finishCount);
    }

    /**
     * @description 获取入库通知单、入库单未完成数据
     * @author  ciro
     * @date   2022/4/11 15:04
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getInBoundUnFinishOrderMsg(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<ReceiptNotice> noticeList = getReciptNoticeList(warehouseForm);
        List<Receipt> receiptList = getReciptList(warehouseForm);
        //未完成入库通知单信息
        List<ReceiptNotice> unfinishNoticeList = new ArrayList<>();
        //未完成入库单信息
        List<Receipt> unfinishOrderList = new ArrayList<>();
        List<String> finishNoticeList = new ArrayList<>();
        if (CollUtil.isEmpty(receiptList)){
            for (Receipt receipt:receiptList){
                if (receipt.getStatus().equals(ReceiptStatusEnum.SIX.getCode())){
                    finishNoticeList.add(receipt.getReceiptNoticeNum());
                } else {
                    unfinishOrderList.add(receipt);
                }
            }
        }
        if (CollUtil.isNotEmpty(noticeList)){
            unfinishNoticeList = getUnFinishnoticeList(noticeList,finishNoticeList);
        }
        orderCountVO.setInBoundNoticeOrderUnFinishCount(unfinishNoticeList.size());
        orderCountVO.setInBoundOrderUnFinishCount(unfinishOrderList.size());
        initOrderCoutList(orderCountVO,OrderTypeEnum.RECEIPT_NOTICE.getDesc(),unfinishNoticeList.size());
        initOrderCoutList(orderCountVO,OrderTypeEnum.RECEIPT.getDesc(),unfinishOrderList.size());
        List<OrderMsgVO> receiptMsgLists = initReceiptoOrderMsg(unfinishOrderList);
        List<OrderMsgVO> noticeMsgList = initReceiptNoticeToOrderMsg(unfinishNoticeList,receiptMsgLists);
        orderCountVO.getMsgList().addAll(noticeMsgList);
        orderCountVO.getMsgList().addAll(receiptMsgLists);
    }

    /**
     * @description 获取完成入库单数据
     * @author  ciro
     * @date   2022/4/11 17:56
     * @param: warehouseForm
     * @return: java.util.List<com.jayud.wms.model.po.Receipt>
     **/
    private List<Receipt> getFinishReceiptList(WarehouseForm warehouseForm){
        List<Receipt> receiptList = getReciptList(warehouseForm);
        List<Receipt> finishList = new ArrayList<>();
        if (CollUtil.isNotEmpty(receiptList)){
            finishList = receiptList.stream().filter(x->x.getStatus().equals(ReceiptStatusEnum.THREE.getCode())).collect(Collectors.toList());
        }
        return finishList;
    }

    /**
     * @description 获取未完成入库通知单数据
     * @author  ciro
     * @date   2022/4/11 17:50
     * @param: noticeList
     * @param: finishNoticeList
     * @return: java.util.List<com.jayud.wms.model.po.ReceiptNotice>
     **/
    private List<ReceiptNotice> getUnFinishnoticeList(List<ReceiptNotice> noticeList,List<String> finishNoticeList){
        List<ReceiptNotice> successNoticeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(finishNoticeList)){
            successNoticeList = noticeList.stream().filter(x->!finishNoticeList.contains(x.getReceiptNoticeNum())).collect(Collectors.toList());
        }
        return successNoticeList;
    }

    /**
     * @description 获取入库-质检数据
     * @author  ciro
     * @date   2022/4/11 13:32
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getInBoundQualityMsg(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<QualityInspection> qualityInspectionList = getQualityList(warehouseForm);
        int qualityFinishCount = 0;
        int qualityUnFinishCount = 0;
        if (CollUtil.isNotEmpty(qualityInspectionList)){
            if (warehouseForm.getIsFinish()) {
                qualityFinishCount = qualityInspectionList.stream().filter(x -> x.getStatus().equals(InboundQualityStatusEnum.FINISH.getCode())).collect(Collectors.toList()).size();
            }else {
                qualityInspectionList = qualityInspectionList.stream().filter(x -> !x.getStatus().equals(InboundQualityStatusEnum.FINISH.getCode())).collect(Collectors.toList());
                qualityUnFinishCount = qualityInspectionList.size();
                orderCountVO.getMsgList().addAll(initQualityOrderMsg(qualityInspectionList));
            }
        }
        if (warehouseForm.getIsFinish()){
            initOrderCoutList(orderCountVO,OrderTypeEnum.QUALITY.getDesc()+"完成总量",qualityFinishCount);
        }else {
            initOrderCoutList(orderCountVO,OrderTypeEnum.QUALITY.getDesc(),qualityUnFinishCount);
        }

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
        initOrderCoutList(orderCountVO,OrderTypeEnum.OUTBOUND_NOTICE.getDesc()+"完成总数",orderFinishCount);
        initOrderCoutList(orderCountVO,OrderTypeEnum.OUTBOUND_ORDER.getDesc()+"完成总数",orderFinishCount);
    }

    /**
     * @description 获取未完成出库通知单、出库单数据
     * @author  ciro
     * @date   2022/4/11 14:44
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getOutBoundUnFinishOrderMsg(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<WmsOutboundNoticeOrderInfoVO> noticeList = getOutboundNoticeList(warehouseForm);
        List<WmsOutboundOrderInfoVO> infoList = getOutboundOrderList(warehouseForm);
        //未完成出库通知单数据
        List<WmsOutboundNoticeOrderInfoVO> unfinishNoticeList = new ArrayList<>();
        //未完成出库单数据
        List<WmsOutboundOrderInfoVO> unfinishOrderList = new ArrayList<>();
        //完成出库通知单集合
        List<String> finishNoticeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(infoList)){
            finishNoticeList = infoList.stream().filter(x->x.getOrderStatusType().equals(OutboundOrdertSatus.ISSUED)).map(x->x.getNoticeOrderNumber()).collect(Collectors.toList());
            unfinishOrderList = infoList.stream().filter(x->!x.getOrderStatusType().equals(OutboundOrdertSatus.ISSUED)).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(noticeList)){
            List<String> finalFinishNoticeList = finishNoticeList;
            if (CollUtil.isNotEmpty(finishNoticeList)) {
                unfinishNoticeList = noticeList.stream().filter(x -> !finalFinishNoticeList.contains(x.getOrderNumber())).collect(Collectors.toList());
            }else {
                unfinishNoticeList = noticeList;
            }
        }
        initOrderCoutList(orderCountVO,OrderTypeEnum.OUTBOUND_NOTICE.getDesc(),unfinishNoticeList.size());
        initOrderCoutList(orderCountVO,OrderTypeEnum.OUTBOUND_ORDER.getDesc(),unfinishOrderList.size());
        List<OrderMsgVO> orderMsgList = initOutboundOrderMsg(unfinishOrderList);
        List<OrderMsgVO> noticeMsgList = initOutboundNoticeOrderMsg(unfinishNoticeList,orderMsgList);
        orderCountVO.getMsgList().addAll(noticeMsgList);
        orderCountVO.getMsgList().addAll(orderMsgList);

    }


    /**
     * @description 获取出库-发运复核完成数量
     * @author  ciro
     * @date   2022/4/11 14:01
     * @param: warehouseForm
     * @param: orderCountVO
     * @return: void
     **/
    private void getOutBoundReviewMsg(WarehouseForm warehouseForm,OrderCountVO orderCountVO){
        List<WmsOutboundShippingReviewInfo> infoList = getOutboundReviewList(warehouseForm);
        int reviewFinishCount = 0;
        int reviewUnFinishCount = 0;
        if (CollUtil.isNotEmpty(infoList)){
            if (warehouseForm.getIsFinish()) {
                reviewFinishCount = infoList.stream().filter(x -> x.getOrderStatusType().equals(OutboundShippingReviewSatusEnum.REVIEWED.getStatus())).collect(Collectors.toList()).size();
            }else {
                infoList = infoList.stream().filter(x -> !x.getOrderStatusType().equals(OutboundShippingReviewSatusEnum.REVIEWED.getStatus())).collect(Collectors.toList());
                reviewUnFinishCount = infoList.size();
                orderCountVO.getMsgList().addAll(initShippingReviewOrderMsg(infoList));
            }
        }
        if (warehouseForm.getIsFinish()) {
            initOrderCoutList(orderCountVO,OrderTypeEnum.SHIPPING_REVIEW.getDesc()+"完成总数",reviewFinishCount);
        }else {
            initOrderCoutList(orderCountVO,OrderTypeEnum.SHIPPING_REVIEW.getDesc(),reviewFinishCount);
        }
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

    /**
     * @description 初始化入仓通知单数据
     * @author  ciro
     * @date   2022/4/12 10:10
     * @param: noticeList
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    private List<OrderMsgVO> initReceiptNoticeToOrderMsg(List<ReceiptNotice> noticeList,List<OrderMsgVO> receiptList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        Map<String,OrderMsgVO> receiptMap = new HashMap<>();
        if (CollUtil.isNotEmpty(receiptList)){
            receiptMap = receiptList.stream().collect(Collectors.toMap(x->x.getNoticeNumber(),x->x));
        }
        if (CollUtil.isNotEmpty(noticeList)){
            for (ReceiptNotice notice:noticeList){
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(notice.getReceiptNoticeNum());
                msg.setOrderType(OrderTypeEnum.RECEIPT_NOTICE.getType());
                msg.setOrderType_text(OrderTypeEnum.RECEIPT_NOTICE.getDesc());
                //已转出库单状态
                if (receiptMap.containsKey(notice.getReceiptNoticeNum())){
                    OrderMsgVO recipt = receiptMap.get(notice.getReceiptNoticeNum());
                    msg.setOrderStatus(recipt.getOrderStatus());
                    msg.setOrderStatus_text(recipt.getOrderStatus_text());
                }else {
                    msg.setOrderStatus(notice.getStatus());
                    msg.setOrderStatus_text(ReceiptNoticeStatusEnum.getIntegerDesc(notice.getStatus()));
                }
                msg.setCreateTime(notice.getCreateTime());
                orderList.add(msg);
            }
        }
        return orderList;
    }

    /**
     * @description 初始化入库单数据
     * @author  ciro
     * @date   2022/4/12 10:11
     * @param: receiptList
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    private List<OrderMsgVO> initReceiptoOrderMsg(List<Receipt> receiptList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        if (CollUtil.isNotEmpty(receiptList)){
            receiptList.forEach(receipt->{
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(receipt.getReceiptNum());
                msg.setOrderType(OrderTypeEnum.RECEIPT.getType());
                msg.setOrderType_text(OrderTypeEnum.RECEIPT.getDesc());
                msg.setOrderStatus(receipt.getStatus());
                msg.setOrderStatus_text(ReceiptStatusEnum.getDesc(receipt.getStatus()));
                msg.setCreateTime(receipt.getCreateTime());
                msg.setNoticeNumber(receipt.getReceiptNoticeNum());
                orderList.add(msg);
            });
        }
        return orderList;
    }

    /**
     * @description 初始化质检单数据
     * @author  ciro
     * @date   2022/4/12 10:14
     * @param: qualityList
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    private List<OrderMsgVO> initQualityOrderMsg(List<QualityInspection> qualityList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        if (CollUtil.isNotEmpty(qualityList)){
            qualityList.forEach(quality->{
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(quality.getQcNo());
                msg.setOrderType(OrderTypeEnum.QUALITY.getType());
                msg.setOrderType_text(OrderTypeEnum.QUALITY.getDesc());
                msg.setOrderStatus(quality.getStatus());
                msg.setOrderStatus_text(InboundQualityStatusEnum.getDesc(quality.getStatus()));
                msg.setCreateTime(quality.getCreateTime());
                orderList.add(msg);
            });
        }
        return orderList;
    }

    /**
     * @description 初始化出库通知单数据
     * @author  ciro
     * @date   2022/4/12 10:59
     * @param: noticeList
     * @param: infoList
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    private List<OrderMsgVO> initOutboundNoticeOrderMsg(List<WmsOutboundNoticeOrderInfoVO> noticeList,List<OrderMsgVO> infoList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        Map<String,OrderMsgVO> infoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(infoList)){
            infoMap = infoList.stream().collect(Collectors.toMap(x->x.getNoticeNumber(),x->x));
        }
        if (CollUtil.isNotEmpty(noticeList)){
            for (WmsOutboundNoticeOrderInfoVO notice:noticeList){
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(notice.getOrderNumber());
                msg.setOrderType(OrderTypeEnum.OUTBOUND_NOTICE.getType());
                msg.setOrderType_text(OrderTypeEnum.OUTBOUND_NOTICE.getDesc());
                if (infoMap.containsKey(notice.getOrderNumber())){
                    OrderMsgVO orders = infoMap.get(notice.getOrderNumber());
                    msg.setOrderStatus(orders.getOrderStatus());
                    msg.setOrderStatus_text(orders.getOrderStatus_text());
                }else {
                    msg.setOrderStatus(Integer.parseInt(notice.getOrderStatusType()));
                    msg.setOrderStatus_text(OutboundNoticeOrdertSatus.getMsg(Integer.parseInt(notice.getOrderStatusType())));
                }
                msg.setCreateTime(notice.getCreateTime());
                orderList.add(msg);
            }
        }
        return orderList;
    }

   /**
    * @description 初始化出库单数据
    * @author  ciro
    * @date   2022/4/12 10:59
    * @param: infoList
    * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
    **/
    private List<OrderMsgVO> initOutboundOrderMsg(List<WmsOutboundOrderInfoVO> infoList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        if (CollUtil.isNotEmpty(infoList)){
            infoList.forEach(info->{
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(info.getOrderNumber());
                msg.setOrderType(OrderTypeEnum.OUTBOUND_ORDER.getType());
                msg.setOrderType_text(OrderTypeEnum.OUTBOUND_ORDER.getDesc());
                msg.setOrderStatus(Integer.parseInt(info.getOrderStatusType()));
                msg.setOrderStatus_text(OutboundOrdertSatus.getMsg(Integer.parseInt(info.getOrderStatusType())));
                msg.setCreateTime(info.getCreateTime());
                msg.setNoticeNumber(info.getNoticeOrderNumber());
                orderList.add(msg);
            });
        }
        return orderList;
    }


    /**
     * @description 初始化发运复核数据
     * @author  ciro
     * @date   2022/4/12 11:03
     * @param: infoList
     * @return: java.util.List<com.jayud.wms.model.vo.LargeScreen.OrderMsgVO>
     **/
    private List<OrderMsgVO> initShippingReviewOrderMsg(List<WmsOutboundShippingReviewInfo> infoList){
        List<OrderMsgVO> orderList = new ArrayList<>();
        if (CollUtil.isNotEmpty(infoList)){
            infoList.forEach(info->{
                OrderMsgVO msg = new OrderMsgVO();
                msg.setOrderNumber(info.getShippingReviewOrderNumber());
                msg.setOrderType(OrderTypeEnum.SHIPPING_REVIEW.getType());
                msg.setOrderType_text(OrderTypeEnum.SHIPPING_REVIEW.getDesc());
                msg.setOrderStatus(Integer.parseInt(info.getOrderStatusType()));
                msg.setOrderStatus_text(OutboundShippingReviewSatusEnum.getDesc(Integer.parseInt(info.getOrderStatusType())));
                msg.setCreateTime(info.getCreateTime());
                orderList.add(msg);
            });
        }
        return orderList;
    }

    /**
     * @description 初始化数量信息
     * @author  ciro
     * @date   2022/4/12 14:34
     * @param: orderCount
     * @param: typeName
     * @param: counts
     * @return: void
     **/
    private void initOrderCoutList(OrderCountVO orderCount,String typeName,Integer counts){
        Map msgMap = new HashMap();
        msgMap.put("name",typeName);
        msgMap.put("value",counts);
        orderCount.getCoutList().add(msgMap);
    }

    /**
     * @description 初始化曲线日期
     * @author  ciro
     * @date   2022/4/12 15:14
     * @param: lastDay
     * @return: java.util.List<java.lang.String>
     **/
    private List<String> initLineDate(int lastDay){
        List<String> dateList = new ArrayList<>();
        for (int i=1;i<=lastDay;i++){
            dateList.add(i+"日");
        }
        return dateList;
    }

    /**
     * @description 初始化曲线数据
     * @author  ciro
     * @date   2022/4/12 15:22
     * @param: lineList
     * @param: dateCount
     * @return: java.util.List<java.lang.Integer>
     **/
    private List<Integer> intiLineDataList(LinkedList<LinkedHashMap> lineList,int dateCount){
        List<Integer> dataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(lineList)){
            for (int i=0;i<dateCount;i++) {
                dataList.add(0);
            }
            for (LinkedHashMap dataMap : lineList){
                dataList.set(Integer.parseInt(dataMap.get("months").toString()),Integer.parseInt(dataMap.get("countNumber").toString()));
            }
        }
        return dataList;
    }

}
