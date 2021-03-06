package com.jayud.tms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.LogisticsTrackVO;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.feign.*;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.model.bo.*;
import com.jayud.tms.model.enums.ScmOrderStatusEnum;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.model.vo.supplier.QuerySupplierBill;
import com.jayud.tms.model.vo.supplier.QuerySupplierBillInfo;
import com.jayud.tms.model.vo.supplier.SupplierBill;
import com.jayud.tms.model.vo.supplier.SupplierBillInfo;
import com.jayud.tms.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * ?????????????????? ???????????????
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTransportServiceImpl extends ServiceImpl<OrderTransportMapper, OrderTransport> implements IOrderTransportService {

    private static Logger logger = LoggerFactory.getLogger(OrderTransportServiceImpl.class);

    @Autowired
    IOrderTakeAdrService orderTakeAdrService;
    @Autowired
    IDeliveryAddressService deliveryAddressService;
    @Autowired
    IOrderTransportService orderTransportService;
    @Autowired
    IOrderSendCarsService orderSendCarsService;
    @Autowired
    IScmOrderService scmOrderService;
    @Autowired
    OmsClient omsClient;
    @Autowired
    FileClient fileClient;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FinanceClient financeClient;
    @Autowired
    private CustomsClient customsClient;

    @Override
    public boolean createOrderTransport(InputOrderTransportForm form) {
        OrderTransport orderTransport = ConvertUtil.convert(form, OrderTransport.class);
        if (orderTransport == null) {
            return false;
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms1 = form.getTakeAdrForms1();
        for (InputOrderTakeAdrForm orderTakeAdrForm1 : orderTakeAdrForms1) {
            orderTakeAdrForm1.setOprType(Integer.valueOf(CommonConstant.VALUE_1));
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms2 = form.getTakeAdrForms2();
        for (InputOrderTakeAdrForm orderTakeAdrForm2 : orderTakeAdrForms2) {
            orderTakeAdrForm2.setOprType(Integer.valueOf(CommonConstant.VALUE_2));
        }

        List<InputOrderTakeAdrForm> orderTakeAdrForms = new ArrayList<>();
        orderTakeAdrForms.addAll(orderTakeAdrForms1);
        orderTakeAdrForms.addAll(orderTakeAdrForms2);


        if (orderTransport.getId() != null) {//??????
            //?????????,?????????????????????????????????
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no", form.getOrderNo());
            //????????????/????????????
            List<OrderTakeAdr> list = this.orderTakeAdrService.list(queryWrapper);
            orderTakeAdrService.remove(queryWrapper);//??????????????????
            //??????????????????
            Set<Long> deliveryIds = list.stream().map(OrderTakeAdr::getDeliveryId).collect(Collectors.toSet());
            this.deliveryAddressService.removeByIds(deliveryIds);
            orderTransport.setUpdatedTime(LocalDateTime.now());
            orderTransport.setUpdatedUser(form.getLoginUser());
        } else {//??????
            //???????????????
//            String orderNo = StringUtils.loadNum(CommonConstant.T, 12);
//            while (true) {
//                if (!isExistOrder(orderNo)) {//??????
//                    orderNo = StringUtils.loadNum(CommonConstant.T, 12);
//                } else {
//                    break;
//                }
//            }
//            orderTransport.setOrderNo(orderNo);
            orderTransport.setCreatedUser(form.getLoginUser());
        }
        JSONArray jsonArray = new JSONArray();
        for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
            //?????????????????????????????????,?????????????????????????????????
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setId(inputOrderTakeAdrForm.getDeliveryId());
            deliveryAddress.setCustomerId(inputOrderTakeAdrForm.getCustomerId());
            deliveryAddress.setContacts(inputOrderTakeAdrForm.getContacts());
            deliveryAddress.setPhone(inputOrderTakeAdrForm.getPhone());
            deliveryAddress.setAddress(inputOrderTakeAdrForm.getAddress());
            deliveryAddress.setProvince(inputOrderTakeAdrForm.getProvince());
            deliveryAddress.setCity(inputOrderTakeAdrForm.getCity());
            deliveryAddress.setArea(inputOrderTakeAdrForm.getArea());

            deliveryAddressService.saveOrUpdate(deliveryAddress);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", deliveryAddress.getId());
            jsonObject.put("address", deliveryAddress.getAddress());
            jsonArray.add(jsonObject);

            OrderTakeAdr orderTakeAdr = ConvertUtil.convert(inputOrderTakeAdrForm, OrderTakeAdr.class);
            orderTakeAdr.setDeliveryId(deliveryAddress.getId());
            orderTakeAdr.setId(inputOrderTakeAdrForm.getTakeAdrId());
            orderTakeAdr.setTakeTime(inputOrderTakeAdrForm.getTakeTimeStr());
            orderTakeAdr.setOrderNo(orderTransport.getOrderNo());
            orderTakeAdr.setFile(StringUtils.getFileStr(inputOrderTakeAdrForm.getTakeFiles()));
            orderTakeAdr.setFileName(StringUtils.getFileNameStr(inputOrderTakeAdrForm.getTakeFiles()));


            orderTakeAdrService.saveOrUpdate(orderTakeAdr);

        }
        String tmps = redisUtils.get("tmsDispatchAddress");
        try {
            if (StringUtils.isEmpty(tmps)) {
                redisUtils.set("tmsDispatchAddress", jsonArray.toString());
            } else {
                JSONArray oldData = new JSONArray(tmps);
                oldData.addAll(jsonArray);
                redisUtils.set("tmsDispatchAddress", oldData);
            }
        }catch (Exception e){
            log.warn("??????????????????????????????????????? value="+tmps);
        }


        orderTransport.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
        orderTransport.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
        if (form.getIsGoodsEdit() == null || !form.getIsGoodsEdit()) {
            orderTransport.setStatus(OrderStatusEnum.TMS_T_0.getCode());
        }
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        if ("submit".equals(form.getCmd())) {
            this.msgPush(orderTransport);
        }
        return result;
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_NO, orderNo);
        List<OrderTransport> orderTransports = baseMapper.selectList(queryWrapper);
        if (orderTransports == null || orderTransports.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public InputOrderTransportVO getOrderTransport(String mainOrderNo) {
        InputOrderTransportVO inputOrderTransportVO = baseMapper.getOrderTransport(mainOrderNo);
        if (inputOrderTransportVO == null) {
            return new InputOrderTransportVO();
        }
        //????????????/????????????
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(inputOrderTransportVO.getOrderNo());
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();
        List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();
        Integer totalAmount = 0;//?????????
        Double totalWeight = 0.0;//?????????
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            inputOrderTakeAdrVO.setTakeFiles(StringUtils.getFileViews(inputOrderTakeAdrVO.getFile(), inputOrderTakeAdrVO.getFileName(), prePath));
            if (CommonConstant.VALUE_1.equals(String.valueOf(inputOrderTakeAdrVO.getOprType()))) {//??????
                orderTakeAdrForms1.add(inputOrderTakeAdrVO);
                Integer pieceAmount = inputOrderTakeAdrVO.getPieceAmount();
                Double weight = inputOrderTakeAdrVO.getWeight();
                if (inputOrderTakeAdrVO.getPieceAmount() == null) {
                    pieceAmount = 0;
                }
                if (inputOrderTakeAdrVO.getWeight() == null) {
                    weight = 0.0;
                }
                totalAmount = totalAmount + pieceAmount;
                totalWeight = totalWeight + weight;
            } else {
                orderTakeAdrForms2.add(inputOrderTakeAdrVO);  //??????
            }
        }
        inputOrderTransportVO.setTotalAmount(totalAmount);
        inputOrderTransportVO.setTotalWeight(totalWeight);
        inputOrderTransportVO.setOrderTakeAdrForms1(orderTakeAdrForms1);
        inputOrderTransportVO.setOrderTakeAdrForms2(orderTakeAdrForms2);
        return inputOrderTransportVO;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @Override
    public List<OrderTransport> getOrderTmsByCondition(OrderTransport orderTransport) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>(orderTransport);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public IPage<OrderTransportVO> findTransportOrderByPage(QueryOrderTmsForm form) {
        //??????????????????
//        if (CollectionUtils.isNotEmpty(form.getTakeTimeStr())) {
//            Set<String> orderNos = this.orderTakeAdrService.getOrderNosByTakeTime(form.getTakeTimeStr(), OrderTakeAdrTypeEnum.ONE.getCode());
//            if (CollectionUtils.isEmpty(orderNos)) {
//                orderNos.add("-1");
//            }
//            if (orderNos.size() >= form.getPageSize()) {
//                form.setSubOrderNos(orderNos.stream().limit(form.getPageSize()).collect(Collectors.toSet()));
//            }
//
//        }

        //??????????????????
        Page<OrderTransportVO> page = new Page<>(form.getPageNum(), form.getPageSize());

        //????????????????????????????????????
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), form.getAccountType()).getData();
//        StopWatch stopWatch = new StopWatch();
        // ????????????
//        stopWatch.start();
        IPage<OrderTransportVO> pageInfo = baseMapper.findTransportOrderByPage(page, form, dataControl);
        // ????????????
//        stopWatch.stop();
//        System.out.println("????????????????????????????????????(??????:???): " + stopWatch.getTotalTimeSeconds() + " ???.");

        if (pageInfo.getRecords().size() == 0) {
            return pageInfo;
        }

        String prePath = fileClient.getBaseUrl().getData().toString();

        List<String> subOrderNos = new ArrayList<>();
        List<Long> warehouseIds = new ArrayList<>();
        for (OrderTransportVO record : pageInfo.getRecords()) {
            subOrderNos.add(record.getOrderNo());
            warehouseIds.add(record.getWarehouseInfoId());
        }

        //??????
        Map<String, String> portInfoMap = this.omsClient.getPortInfoALL().getData();
        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(subOrderNos, null);
        //??????????????????
        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.ZGYS.getSignOne()).getData();
        Map<String, Object> costStatus = omsClient.getCostStatus(null, subOrderNos).getData();
        //????????????????????????
        Map<Long, Map<String, Object>> warehouseMap = this.omsClient.getWarehouseMapByIds(warehouseIds).getData();
        //??????
        Map<Long, String> departmentMap = this.oauthClient.findDepartment().getData().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        for (OrderTransportVO orderTransportVO : pageInfo.getRecords()) {
            orderTransportVO.setPortName(portInfoMap.get(orderTransportVO.getPortCode()));
            orderTransportVO.setCost(MapUtil.getBool(data, orderTransportVO.getOrderNo()));
            orderTransportVO.assemblyTakeAdrInfos(takeAdrsList, prePath);
            orderTransportVO.assemblyCostStatus(costStatus);
            orderTransportVO.assemblyWarehouse(warehouseMap);
            orderTransportVO.doFilterData(dataControl.getAccountType());
            orderTransportVO.setDepartment(departmentMap.get(orderTransportVO.getDepartmentId()));
        }

        return pageInfo;
    }

    @Override
    public SendCarPdfVO initPdfData(String orderNo, String classCode) {
        SendCarPdfVO sendCarPdfVO = baseMapper.initPdfData(orderNo, classCode);
        if (sendCarPdfVO == null) {
            return new SendCarPdfVO();
        }
        //?????????????????????????????????BUG-237
        sendCarPdfVO.setClearCustomsAddress("");
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(orderNo);
        List<TakeGoodsInfoVO> takeGoodsInfo1 = new ArrayList<>();
        List<TakeGoodsInfoVO> takeGoodsInfo2 = new ArrayList<>();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            inputOrderTakeAdrVO.setVehicleSize(sendCarPdfVO.getVehicleSize());
            inputOrderTakeAdrVO.setVehicleType(sendCarPdfVO.getVehicleType());
            inputOrderTakeAdrVO.setCntrNo(sendCarPdfVO.getCntrNo());
            if (CommonConstant.VALUE_1.equals(String.valueOf(inputOrderTakeAdrVO.getOprType()))) {//??????
                takeGoodsInfo1.add(ConvertUtil.convert(inputOrderTakeAdrVO, TakeGoodsInfoVO.class));
            } else {
                takeGoodsInfo2.add(ConvertUtil.convert(inputOrderTakeAdrVO, TakeGoodsInfoVO.class));
            }
        }
        //????????????
        sendCarPdfVO.setTakeInfo1(takeGoodsInfo1);
        //????????????/?????????/????????????/????????????
        OrderSendCarsVO orderSendCarsVO = orderSendCarsService.getOrderSendInfo(orderNo);
        if (orderSendCarsVO == null) {
            return sendCarPdfVO;
        }
        if (takeGoodsInfo2.size() > 1) {//?????????????????????
            if (orderSendCarsVO.getIsVirtual() == null || !orderSendCarsVO.getIsVirtual()) {
                sendCarPdfVO.setDeliveryContacts(orderSendCarsVO.getWarehouseContacts());
                String provinceName = orderSendCarsVO.getProvinceName() == null ? "" : orderSendCarsVO.getProvinceName();
                String cityName = orderSendCarsVO.getCityName() == null ? "" : orderSendCarsVO.getCityName();
                String address = orderSendCarsVO.getAddress() == null ? "" : orderSendCarsVO.getAddress();
                String detailedAddress = provinceName + cityName + address;
                sendCarPdfVO.setDeliveryAddress(detailedAddress +
                        " ?????????:" + orderSendCarsVO.getWarehouseContacts() + " " + orderSendCarsVO.getWarehouseNumber());
                sendCarPdfVO.setDeliveryPhone(orderSendCarsVO.getWarehouseNumber());
            } else {
                //???????????????????????????
                sendCarPdfVO.assembleTakeGoodsInfos2(takeGoodsInfo2);
            }
        } else if (takeGoodsInfo2.size() == 1) {
            TakeGoodsInfoVO takeGoodsInfoVO = takeGoodsInfo2.get(0);
            String provinceName = takeGoodsInfoVO.getStateName() == null ? "" : takeGoodsInfo2.get(0).getStateName();
            String cityName = takeGoodsInfoVO.getCityName() == null ? "" : takeGoodsInfo2.get(0).getCityName();
            String address = takeGoodsInfoVO.getAddress() == null ? "" : takeGoodsInfo2.get(0).getAddress();
            sendCarPdfVO.setDeliveryAddress(provinceName + cityName + address +
                    " ?????????:" + takeGoodsInfoVO.getContacts() + " " + takeGoodsInfoVO.getPhone());
            sendCarPdfVO.setDeliveryContacts(takeGoodsInfo2.get(0).getContacts());
            sendCarPdfVO.setDeliveryPhone(takeGoodsInfo2.get(0).getPhone());
        }
        sendCarPdfVO.setRemarks(orderSendCarsVO.getRemarks());
        //????????????,???????????????
        List<GoodsInfoVO> goodsInfoVOS = new ArrayList<>();
        Integer totalPieceAmount = 0;//?????????
        Double totalWeight = 0.0;//?????????
        Double totalVolume = 0.0;//?????????
        Integer totalPlateAmount = 0;//?????????
        for (TakeGoodsInfoVO takeGoodsInfoVO : takeGoodsInfo1) {
            totalPieceAmount = totalPieceAmount + takeGoodsInfoVO.getPieceAmount();
            Double weight = 0.0;
            Double volume = 0.0;
            Integer plateAmount = 0;
            if (takeGoodsInfoVO.getWeight() != null) {
                weight = takeGoodsInfoVO.getWeight();
            }
            if (takeGoodsInfoVO.getVolume() != null) {
                volume = takeGoodsInfoVO.getVolume();
            }
            if (takeGoodsInfoVO.getPlateAmount() != null) {
                plateAmount = takeGoodsInfoVO.getPlateAmount();
            }
            totalWeight = totalWeight + weight;
            totalVolume = totalVolume + volume;
            totalPlateAmount += plateAmount;
            GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
            goodsInfoVO.setGoodsDesc(takeGoodsInfoVO.getGoodsDesc());
            goodsInfoVO.setPieceAmount(takeGoodsInfoVO.getPieceAmount());
            goodsInfoVO.setPlateAmount(takeGoodsInfoVO.getPlateAmount());
            goodsInfoVO.setWeight(takeGoodsInfoVO.getWeight());
            goodsInfoVO.setVolume(takeGoodsInfoVO.getVolume());
            goodsInfoVOS.add(goodsInfoVO);
        }
        sendCarPdfVO.setGoddsInfos(goodsInfoVOS);
        //?????????/?????????/?????????
        sendCarPdfVO.setTotalPieceAmount(totalPieceAmount);
        sendCarPdfVO.setTotalWeight(totalWeight);
        sendCarPdfVO.setTotalVolume(totalVolume);
        sendCarPdfVO.setTotalPlateAmount(totalPlateAmount);
        return sendCarPdfVO;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param form
     * @return
     */
    @Override
    public List<DriverOrderTransportVO> getDriverOrderTransport(QueryDriverOrderTransportForm form) {
        //?????????
        List<String> status = Arrays.asList(OrderStatusEnum.TMS_T_4.getCode(),
                OrderStatusEnum.TMS_T_5.getCode(),
                OrderStatusEnum.TMS_T_6.getCode(),
                OrderStatusEnum.TMS_T_7.getCode(),
                OrderStatusEnum.TMS_T_7_1.getCode(),
                OrderStatusEnum.TMS_T_8.getCode(),
                OrderStatusEnum.TMS_T_8_1.getCode(),
                OrderStatusEnum.TMS_T_9.getCode(),
                OrderStatusEnum.TMS_T_9_1.getCode(),
                OrderStatusEnum.TMS_T_9_2.getCode(),
                OrderStatusEnum.TMS_T_10.getCode(),
                OrderStatusEnum.TMS_T_11.getCode(),
                OrderStatusEnum.TMS_T_12.getCode(),
                OrderStatusEnum.TMS_T_13.getCode(),
                OrderStatusEnum.TMS_T_14.getCode(),
                OrderStatusEnum.TMS_T_15.getCode());

        List<DriverOrderTransportVO> list = this.baseMapper.getDriverOrderTransport(form, status);

        List<String> orderNoList = list.stream().filter(Objects::nonNull).map(DriverOrderTransportVO::getOrderNo).collect(Collectors.toList());
        //??????????????????/????????????
        if (CollectionUtils.isNotEmpty(orderNoList)) {
            List<DriverOrderTakeAdrVO> adrs = this.orderTakeAdrService.getDriverOrderTakeAdr(orderNoList, null);
            list.forEach(tmp -> {
                tmp.setStatus(OrderStatusEnum.getDesc(tmp.getStatus()));
                tmp.groupAddr(adrs);
                tmp.setTakeOrders(form.getOrderIds() == null ? form.getExcludeOrderIds() : form.getOrderIds());
            });
            //???????????????????????????
            list.forEach(tmp -> {
                tmp.assemblyAddr();
                tmp.assemblyGoodsName();
            });
        }

        return list;
    }

    /**
     * ????????????????????????
     */
    @Override
    public String getOrderTransportStatus(String orderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().select(OrderTransport::getStatus).eq(OrderTransport::getOrderNo, orderNo);
        return this.getOne(condition).getStatus();
    }

    /**
     * ??????????????????
     *
     * @param form
     */
    @Override
    public void doDriverFeedbackStatus(OprStatusForm form) {
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(UserOperator.getToken());

        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_TRANSPORT);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());

        String code = "";
        String desc = "";
        switch (form.getCmd()) {
            case CommonConstant.CAR_TAKE_GOODS://????????????
                code = OrderStatusEnum.TMS_T_5.getCode();
                desc = OrderStatusEnum.TMS_T_5.getDesc();
                break;
            case CommonConstant.CAR_WEIGH://????????????
                code = OrderStatusEnum.TMS_T_6.getCode();
                desc = OrderStatusEnum.TMS_T_6.getDesc();
                orderTransport.setCarWeighNum(form.getCarWeighNum());
                break;
            case CommonConstant.CAR_GO_CUSTOMS://????????????
                code = form.getStatus();
                if (OrderStatusEnum.TMS_T_9_1.getCode().equals(form.getStatus())) {
                    orderTransport.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                } else if (OrderStatusEnum.TMS_T_9_2.getCode().equals(form.getStatus())) {
                    orderTransport.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                }

                if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus())) {
                    //????????????????????????
                    desc = OrderStatusEnum.TMS_T_9.getDesc();
                }
                auditInfoForm.setAuditTypeDesc(CommonConstant.CAR_GO_CUSTOMS_DESC);
                break;
            case CommonConstant.CAR_ENTER_WAREHOUSE://????????????
                code = OrderStatusEnum.TMS_T_10.getCode();
                desc = OrderStatusEnum.TMS_T_10.getDesc();
                break;
            case CommonConstant.CAR_OUT_WAREHOUSE://????????????
                code = OrderStatusEnum.TMS_T_13.getCode();
                desc = OrderStatusEnum.TMS_T_13.getDesc();

                //???????????????:??????????????????????????????
                OprStatusForm tms11 = new OprStatusForm();
                tms11.setMainOrderId(form.getMainOrderId());
                tms11.setOrderId(form.getOrderId());
                tms11.setStatus(OrderStatusEnum.TMS_T_11.getCode());
                tms11.setStatusName(OrderStatusEnum.TMS_T_11.getDesc());
                omsClient.saveOprStatus(tms11);
                OprStatusForm tms12 = new OprStatusForm();
                tms12.setMainOrderId(form.getMainOrderId());
                tms12.setOrderId(form.getOrderId());
                tms12.setStatus(OrderStatusEnum.TMS_T_12.getCode());
                tms12.setStatusName(OrderStatusEnum.TMS_T_12.getDesc());
                omsClient.saveOprStatus(tms12);
                break;
            case CommonConstant.CAR_SEND: //????????????
                code = OrderStatusEnum.TMS_T_14.getCode();
                desc = OrderStatusEnum.TMS_T_14.getDesc();
                break;
            case CommonConstant.CONFIRM_SIGN_IN://????????????
                code = OrderStatusEnum.TMS_T_15.getCode();
                desc = OrderStatusEnum.TMS_T_15.getDesc();
                break;
        }

        orderTransport.setStatus(code);
        form.setStatus(code);
        form.setStatusName(desc);
        auditInfoForm.setAuditStatus(code);
        auditInfoForm.setAuditTypeDesc(desc);

        //??????????????????
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        omsClient.saveOprStatus(form);
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);

    }

    /**
     * ?????????????????????????????????????????????????????????,????????????????????????????????????????????????
     */
    @Override
    @Transactional
    public void driverCustomsClearanceVehicles(OprStatusForm form) {
        //????????????
        String cmd = form.getCmd();
        form.setCmd(cmd);
        this.doDriverFeedbackStatus(form);
        //????????????????????????????????????
        if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus())) {
            //??????????????????
            form.setCmd(CommonConstant.CAR_ENTER_WAREHOUSE);
            this.doDriverFeedbackStatus(form);
            //??????????????????
            form.setCmd(CommonConstant.CAR_OUT_WAREHOUSE);
            this.doDriverFeedbackStatus(form);
        }

    }

    @Override
    public StatisticsDataNumberVO statisticsDataNumber() {
        return baseMapper.statisticsDataNumber();
    }

    /**
     * ??????????????????????????????????????????
     */
    @Override
    public List<OrderTransport> getTmsOrderByMainOrderNos(List<String> mainOrders) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderTransport::getMainOrderNo, mainOrders);
        return this.baseMapper.selectList(condition);
    }


    /**
     * ??????????????????????????????????????????
     */
    @Override
    public List<OrderVO> getOrderTransportByMainOrderNo(List<String> mainOrders) {
        return this.baseMapper.getOrderTransportByMainOrderNo(mainOrders);
    }

    /**
     * ????????????????????????
     *
     * @param status
     * @param dataControl
     * @param datas
     * @return
     */
    @Override
    public Integer getNumByStatus(String status, DataControl dataControl, Map<String, Object> datas) {
        Integer num = 0;
        switch (status) {
            case "CostAudit":
                List<Long> legalIds = dataControl.getCompanyIds();
                List<OrderTransport> list = this.getByLegalEntityId(legalIds);
                if (CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(OrderTransport::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpensesNum(SubOrderSignEnum.ZGYS.getSignOne(), dataControl, orderNos).getData();
                break;
            case "zgysReceiverCheck":
                Map<String, Integer> costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            case "zgysPayCheck":
                costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            default:
                num = this.baseMapper.getNumByStatus(status, dataControl);
        }
        return num == null ? 0 : num;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param mainOrderNos
     * @return
     */
    @Override
    public List<OrderTransportInfoVO> getTmsOrderInfoByMainOrderNos(List<String> mainOrderNos) {
        return this.baseMapper.getTmsOrderInfoByMainOrderNos(mainOrderNos);
    }

    @Override
    public List<OrderTransport> getByLegalEntityId(List<Long> legalIds) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderTransport::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<OrderTransport> preconditionsGoCustomsAudit() {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTransport::getStatus, OrderStatusEnum.TMS_T_6.getCode());
        condition.lambda().or(e -> e.eq(OrderTransport::getStatus, OrderStatusEnum.TMS_T_5.getCode())
                .eq(OrderTransport::getIsVehicleWeigh, 0));
        return this.baseMapper.selectList(condition);
    }


    /**
     * ??????id??????????????????
     *
     * @param id
     * @return
     */
    @Override
    public OrderTransportInfoVO getDetailsById(Long id) {
        OrderTransportInfoVO orderTransportInfoVO = this.baseMapper.getTmsOrderInfoById(id);
        List<String> orderNos = new ArrayList<>();
        List<Long> vehicleIds = new ArrayList<>();
        if (orderTransportInfoVO == null) {
            return null;
        }
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        orderNos.add(orderTransportInfoVO.getOrderNo());
        OrderSendCarsInfoVO orderSendCars = orderTransportInfoVO.getOrderSendCars();
        if (orderSendCars != null) {
            vehicleIds.add(orderSendCars.getVehicleId());
            orderTransportInfoVO.getOrderSendCars().assemblyFiles(prePath);
        }
        //????????????/??????????????????
        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(orderNos, null);
        //??????????????????
        Object vehicleInfos = omsClient.getVehicleInfoByIds(vehicleIds).getData();
        //????????????
        String portName = this.omsClient.getPortNameByCode(orderTransportInfoVO.getPortCode()).getData();
        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(orderTransportInfoVO.getMainOrderNo()));
        //??????????????????
        Object driverInfos = this.omsClient.getDriverInfoByIds(Collections.singletonList(orderTransportInfoVO.getDriverInfoId())).getData();

        orderTransportInfoVO.assemblyAddr(takeAdrsList);  //????????????
        orderTransportInfoVO.assemblyVehicleInfos(vehicleInfos); //??????????????????
        orderTransportInfoVO.setPortName(portName);
        orderTransportInfoVO.assemblyMainOrderData(result.getData()); //?????????????????????
        orderTransportInfoVO.assemblyDriverInfo(driverInfos);

        AtomicReference<Double> totalWeight = new AtomicReference<>(0.0);
        AtomicReference<Integer> totalNum = new AtomicReference<>(0);
        orderTransportInfoVO.getPickUpAddress().forEach(e -> {
            e.sortData(prePath);
            totalWeight.updateAndGet(v -> v + e.getWeight());
            totalNum.updateAndGet(v -> v + e.getPieceAmount());
        });
        orderTransportInfoVO.setTotalWeight(totalWeight.get()).setTotalNum(totalNum.get());
        orderTransportInfoVO.getDeliveryAddress().forEach(e -> e.sortData(prePath));

        return orderTransportInfoVO;
    }

    /**
     * ???????????????
     *
     * @param userType
     * @return
     */
    @Override
    public Map<String, Object> getPendingOpt(String userType) {
        Map<String, Object> map = new HashMap<>();
        switch (UserTypeEnum.getEnum(userType)) {
            case SUPPLIER_TYPE:
                map = this.orderTransportService.getSupplyPendingOpt();
                break;
            case EMPLOYEE_TYPE:
                break;
        }
        return map;
    }

    @Override
    public Map<String, Object> getSupplyPendingOpt() {
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        Map<String, Object> map = new HashMap<>();
        if (dataControl.getCompanyIds() != null) {
            Long supplierId = dataControl.getCompanyIds().get(0);
            List<OrderTransport> tmsOrders = this.orderTransportService.getOrderTmsByCondition(new OrderTransport().setSupplierId(supplierId));
            Object payCost = this.omsClient.getSupplierCost(supplierId).getData();
            JSONArray jsonArray = new JSONArray(payCost);
            this.calculatePendingCostNum(tmsOrders, jsonArray, map);
        }
        return map;
    }


    /**
     * ????????????????????????
     *
     * @param tmsOrders
     * @param jsonArray
     * @param map
     */
    @Override
    public void calculatePendingCostNum(List<OrderTransport> tmsOrders, JSONArray jsonArray, Map<String, Object> map) {
        ConcurrentMap<String, Long> tmsMap = tmsOrders.stream().collect(Collectors.toConcurrentMap(OrderTransport::getOrderNo, OrderTransport::getId));
        Map<String, Object> notCost = new HashMap<>();
        Map<String, Object> abnormalCost = new HashMap<>();
        List<Long> abnormalIds = new ArrayList<>();
        List<String> employedOrder = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String orderNo = jsonObject.getStr("orderNo");
            //????????????
            if (OrderStatusEnum.COST_0.getCode().equals(jsonObject.getStr("status"))) {
                abnormalIds.add(tmsMap.get(orderNo));
            }
            employedOrder.add(orderNo);
        }
        employedOrder.forEach(tmsMap::remove);
        notCost.put("num", tmsMap.size());
        notCost.put("subOrderIds", tmsMap.values());
        abnormalCost.put("num", abnormalIds.size());
        abnormalCost.put("subOrderIds", abnormalIds);
        map.put("notCost", notCost);
        map.put("abnormalCost", abnormalCost);
    }

    /**
     * ???????????????????????????
     *
     * @param form
     * @param callbackParam
     * @return
     */
    @Override
    public IPage<SupplierBill> findSupplierBillByPage(QuerySupplierBill form, Map<String, Object> callbackParam) {
        //??????????????????
        Page<SupplierBill> page = new Page(form.getPageNum(), form.getPageSize());
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        if (CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
            return new Page<>();
        }
        form.setSupplierId(dataControl.getCompanyIds().get(0));
        //?????????????????????
        IPage<SupplierBill> pageInfo = this.baseMapper.findSupplierBillByPage(page, form);
        if (CollectionUtils.isEmpty(pageInfo.getRecords())) {
            return new Page<>();
        }
        List<String> month = new ArrayList<>();
        AtomicReference<Integer> orderTotalNum = new AtomicReference<>(0);
        pageInfo.getRecords().forEach(e -> {
            orderTotalNum.updateAndGet(v -> v + e.getOrderNum());
            month.add(e.getMonth());
        });
        List<OrderTakeAdr> takeAdrs = this.orderTakeAdrService.getByTakeTime(month, "%Y-%m");
        Set<String> orderNos = new HashSet<>();
        Map<String, Set<String>> group = new HashMap<>();

        for (OrderTakeAdr takeAdr : takeAdrs) {
            orderNos.add(takeAdr.getOrderNo());
            String key = DateUtils.LocalDateTime2Str(takeAdr.getTakeTime(), "yyyy-MM");
            Set<String> data = group.get(key);
            if (data == null) {
                Set<String> tmp = new HashSet<>();
                tmp.add(takeAdr.getOrderNo());
                group.put(key, tmp);
            } else {
                data.add(takeAdr.getOrderNo());
                group.put(key, data);
            }
        }
        //???????????????????????????
        Map<String, Map<String, BigDecimal>> payCostMap = this.omsClient.statisticalSupplierPayCostByOrderNos(form.getSupplierId(), new ArrayList<>(orderNos)).getData();
        Map<String, Map<String, BigDecimal>> cost = new HashMap<>();
        Map<String, BigDecimal> total = new HashMap<>();
        group.forEach((k, v) -> {
            Map<String, BigDecimal> tmp = new HashMap<>();
            for (String orderNo : v) {
                Map<String, BigDecimal> obj = payCostMap.get(orderNo);
                if (obj != null) {
                    obj.forEach((k1, v1) -> tmp.merge(k1, v1, BigDecimal::add));
                }
            }
            //??????
            tmp.forEach((k2, v2) -> total.merge(k2, v2, BigDecimal::add));
            cost.put(k, tmp);
        });
        pageInfo.getRecords().forEach(e -> e.assemblyCost(cost.get(e.getMonth())));
        callbackParam.put("total", total);
        callbackParam.put("orderTotalNum", orderTotalNum.get());
        return pageInfo;
    }


    /**
     * ?????????????????????????????????
     *
     * @param form
     * @return
     */
    @Override
    public IPage<SupplierBillInfo> findSupplierBillInfoByPage(QuerySupplierBillInfo form) {
        //????????????????????????????????????
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        if (CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
            return new Page<>();
        }
        form.setSupplierId(dataControl.getCompanyIds().get(0));
        //??????????????????
        Page<SupplierBillInfo> page = new Page<>(form.getPageNum(), 10000000);
        IPage<SupplierBillInfo> pageInfo = this.baseMapper.findSupplierBillInfoByPage(page, form);

        List<String> subOrderNos = new ArrayList<>();
        List<Long> warehouseIds = new ArrayList<>();
        for (SupplierBillInfo record : pageInfo.getRecords()) {
            subOrderNos.add(record.getOrderNo());
            warehouseIds.add(record.getWarehouseInfoId());
        }

        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(subOrderNos, null);
        //????????????????????????
        Map<Long, Map<String, Object>> warehouseMap = this.omsClient.getWarehouseMapByIds(warehouseIds).getData();
        //????????????
        Map<String, Map<String, BigDecimal>> payCostMap = this.omsClient.statisticalSupplierPayCostByOrderNos(form.getSupplierId(), new ArrayList<>(subOrderNos)).getData();
        //??????????????????
        List<InitComboxStrVO> initVehicle = this.omsClient.initVehicleBySupplier(form.getSupplierId(), 0).getData();
        Map<Long, String> initVehicleMap = initVehicle.stream().collect(Collectors.toMap(InitComboxStrVO::getId, InitComboxStrVO::getName));
        for (SupplierBillInfo supplierBillInfo : pageInfo.getRecords()) {
            supplierBillInfo.assemblyTakeAdrInfos(takeAdrsList);
            supplierBillInfo.assemblyWarehouse(warehouseMap);
            supplierBillInfo.assemblyCost(payCostMap.get(supplierBillInfo.getOrderNo()));
            supplierBillInfo.setPlateNumber(initVehicleMap.get(supplierBillInfo.getVehicleId()));
        }

        return pageInfo;
    }

    @Override
    public void msgPush(OrderTransport orderTransport) {
        Map<String, String> request = new HashMap<>();
        request.put("topic", KafkaMsgEnums.MESSAGE_PUSH_TASK.getTopic());
        request.put("key", KafkaMsgEnums.MESSAGE_PUSH_TASK.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("triggerStatus", orderTransport.getStatus());
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("recordId", orderTransport.getId());
        msg.put("sqlParam", sqlParam);
        msg.put("now", DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
        msg.put("cmd", "order");
        msg.put("subType", SubOrderSignEnum.ZGYS.getSignOne());
        msg.put("mainOrderNo", orderTransport.getMainOrderNo());
        msg.put("orderNo", orderTransport.getOrderNo());
        request.put("msg", JSONUtil.toJsonStr(msg));
        this.msgClient.consume(request);
    }

    @Override
    public Boolean isVirtualWarehouseByOrderNo(String orderNo) {
        return this.baseMapper.isVirtualWarehouseByOrderNo(orderNo) > 0;
    }

    @Override
    public InputOrderTransportVO getOrderDetails(String mainOrderNo, String orderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderNo)) {
            condition.lambda().eq(OrderTransport::getOrderNo, orderNo);
        }
        if (!StringUtils.isEmpty(mainOrderNo)) {
            condition.lambda().eq(OrderTransport::getMainOrderNo, mainOrderNo);
        }
        OrderTransport orderTransport = baseMapper.selectOne(condition);
        InputOrderTransportVO inputOrderTransportVO = ConvertUtil.convert(orderTransport, InputOrderTransportVO.class);
        if (inputOrderTransportVO == null) {
            return new InputOrderTransportVO();
        }
        //????????????/????????????
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(inputOrderTransportVO.getOrderNo());
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();
        List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();
        Integer totalAmount = 0;//?????????
        Double totalWeight = 0.0;//?????????
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            inputOrderTakeAdrVO.setTakeFiles(StringUtils.getFileViews(inputOrderTakeAdrVO.getFile(), inputOrderTakeAdrVO.getFileName(), prePath));
            if (CommonConstant.VALUE_1.equals(String.valueOf(inputOrderTakeAdrVO.getOprType()))) {//??????
                orderTakeAdrForms1.add(inputOrderTakeAdrVO);
                Integer pieceAmount = inputOrderTakeAdrVO.getPieceAmount();
                Double weight = inputOrderTakeAdrVO.getWeight();
                if (inputOrderTakeAdrVO.getPieceAmount() == null) {
                    pieceAmount = 0;
                }
                if (inputOrderTakeAdrVO.getWeight() == null) {
                    weight = 0.0;
                }
                totalAmount = totalAmount + pieceAmount;
                totalWeight = totalWeight + weight;
            } else {
                orderTakeAdrForms2.add(inputOrderTakeAdrVO);  //??????
            }
            inputOrderTakeAdrVO.assemblyGoodsInfo();
        }
        //????????????
        String depName = this.oauthClient.getDepNameById(inputOrderTransportVO.getDepartmentId()).getData();
        //????????????
        OrderSendCarsVO orderSendCars = this.orderSendCarsService.getDetails(inputOrderTransportVO.getOrderNo());
        //????????????
        Map<Long, Map<String, Object>> warehouseInfoMap = this.omsClient.getWarehouseMapByIds(Arrays.asList(inputOrderTransportVO.getWarehouseInfoId())).getData();
        //????????????
        Object customerInfos = this.omsClient.getCustomerByUnitCode(Arrays.asList(inputOrderTransportVO.getUnitCode())).getData();
        //????????????
        String portName = this.omsClient.getPortNameByCode(inputOrderTransportVO.getPortCode()).getData();
        //????????????
        Object customsInfo = this.omsClient.initGoCustomsAudit(mainOrderNo).getData();
        //??????????????????
        List<LogisticsTrackVO> logisticsTrackVOS = this.omsClient.getLogisticsTrackByOrderIds(Arrays.asList(orderTransport.getId()), Arrays.asList(OrderStatusEnum.TMS_T_9.getCode()), 2).getData();
        if (!CollectionUtils.isEmpty(logisticsTrackVOS)) {
            inputOrderTransportVO.setGoCustomsRemarks(logisticsTrackVOS.get(0).getRemarks());
        }

        //????????????
//        Object mainOrderInfos = this.omsClient.getMainOrderByOrderNos(Arrays.asList(mainOrderNo)).getData();
//        JSONObject mainOrderJson = new JSONArray(mainOrderInfos).getJSONObject(0);
//        if (mainOrderJson.getStr("selectedServer").contains(OrderStatusEnum.CKBG.getCode())) {
//            Object customsInfos = customsClient.getCustomsOrderByMainOrderNos(Arrays.asList(mainOrderNo)).getData();
//            inputOrderTransportVO.setEncode(new JSONArray(customsInfos).getJSONObject(0).getStr("encode"));
//        } else {
//            inputOrderTransportVO.setEncode(mainOrderJson.getStr("encode"));
//        }
        inputOrderTransportVO.assemblyCustomsInfo(customsInfo);
        inputOrderTransportVO.assemblySendCars(orderSendCars);
        inputOrderTransportVO.assemblyCustomerInfo(customerInfos);
        inputOrderTransportVO.assemblyWarehouseInfo(warehouseInfoMap);

        inputOrderTransportVO.setDepartmentName(depName);
        inputOrderTransportVO.setPortName(portName);
        inputOrderTransportVO.setTotalAmount(totalAmount);
        inputOrderTransportVO.setTotalWeight(totalWeight);
        inputOrderTransportVO.setOrderTakeAdrForms1(orderTakeAdrForms1);
        inputOrderTransportVO.setOrderTakeAdrForms2(orderTakeAdrForms2);
        return inputOrderTransportVO;
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public JSONObject getCustomerInfoByLoginUserName(Long companyId) {
//        String user = UserOperator.getToken();
//        if (StringUtils.isEmpty(user)) {
//            log.warn("????????????????????????");
//            throw new JayudBizException("????????????????????????");
//        }
//        //????????????id
//        ApiResult result = this.oauthClient.getSystemUserByName(user);
//        if (result.getCode() != HttpStatus.SC_OK) {
//            log.warn("???????????????????????????????????? message=" + result.getMsg());
//            throw new JayudBizException(ResultEnum.OPR_FAIL);
//        }
//        JSONObject systemUser = JSONUtil.parseObj(result.getData());
//        Long companyId = systemUser.getLong("companyId");

        ApiResult result = omsClient.getCustomerInfoVOById(companyId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject customerInfo = JSONUtil.parseObj(result.getData());
        return customerInfo;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param thirdPartyOrderNo
     * @return
     */
    @Override
    public OutOrderTransportVO getOutOrderTransportVOByThirdPartyOrderNo(String thirdPartyOrderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().select(OrderTransport::getMainOrderNo, OrderTransport::getId, OrderTransport::getOrderNo).in(OrderTransport::getThirdPartyOrderNo, thirdPartyOrderNo);
        OrderTransport orderTransport = this.getOne(condition, false);
        if (orderTransport == null) {
            return null;
        }

        OutOrderTransportVO outOrderTransportVO = ConvertUtil.convert(orderTransport, OutOrderTransportVO.class);
        if (outOrderTransportVO == null) {
            return null;
        }

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(orderTransport.getMainOrderNo()));
        if (result == null) {
            return null;
        }

        JSONArray mainOrders = new JSONArray(JSON.toJSONString(result.getData()));
        JSONObject json = mainOrders.getJSONObject(0);
        outOrderTransportVO.setMainOrderNo(json.getStr("orderNo"));
        outOrderTransportVO.setMainOrderId(json.getLong("id"));

        return outOrderTransportVO;
    }

    /**
     * ??????????????????????????????
     *
     * @param orderTransport
     * @param form
     * @param isRetry
     */
    @Override
    public void pushManifest(OrderTransport orderTransport, OprStatusForm form, boolean isRetry,Long orderId) {
        Integer createUserTypeById = orderTransportService.getCreateUserTypeById(orderTransport.getId());

        OrderTransport byId = orderTransportService.getById(orderId);
        if (createUserTypeById != CreateUserTypeEnum.SCM.getCode()) {
            return;
        }
        if (isRetry) {
            scmOrderService.refreshToken();
        }
        try {
            OrderTransport orderTransportTemp = this.getOne(new QueryWrapper<OrderTransport>().lambda()
                    .select(OrderTransport::getThirdPartyOrderNo)
                    .eq(OrderTransport::getId, orderTransport.getId()));

            new Thread(() -> {
                String res = null;

                if (CommonConstant.CAR_TAKE_GOODS.equals(form.getCmd())) {//????????????
                    res = scmOrderService.setManifest(ScmOrderStatusEnum.ASSEMBLY_VEHICLE.getCode(),
                            orderTransportTemp.getThirdPartyOrderNo(),byId.getUnitCode());
//                    if (MapUtil.getInt(res, "code") != 0) {
//                        logger.warn("????????????????????????????????????????????????{}", res.get("msg"));
//                    }
                    if(Integer.parseInt(res)!=0){
                        System.out.println("???????????? ???"+res);
                    }
                    res = scmOrderService.setManifest(ScmOrderStatusEnum.DEPART_VEHICLE.getCode(),
                            orderTransportTemp.getThirdPartyOrderNo(),byId.getUnitCode());
                } else if (CommonConstant.CAR_GO_CUSTOMS.equals(form.getCmd()) &&
                        form.getStatus().equals(OrderStatusEnum.TMS_T_9.getCode())) {//????????????
                    res = scmOrderService.setManifest(ScmOrderStatusEnum.THROUGH_CUSTOMS.getCode(),
                            orderTransportTemp.getThirdPartyOrderNo(),byId.getUnitCode());
                } else if (CommonConstant.CONFIRM_SIGN_IN.equals(form.getCmd())) {//????????????
                    res = scmOrderService.setManifest(ScmOrderStatusEnum.ARRIVED.getCode(),
                            orderTransportTemp.getThirdPartyOrderNo(),byId.getUnitCode());
                }
//                if(res==null){
//                    return CommonResult.error(400, "????????????");
//                }
                if(Integer.parseInt(res)!=0){
                    System.out.println("???????????? ???"+res);
                }
//                if (res != null && MapUtil.getInt(res, "code") != 0) {
//                    logger.warn("????????????????????????????????????????????????{}", res.get("msg"));
//                }

            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("????????????????????????????????????????????????{}", e.getMessage());
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param orderTransport
     */
    @Async
    @Override
    public void pushTransportationInformation(OrderTransport orderTransport,Long orderId) {
        Integer createUserTypeById = orderTransportService.getCreateUserTypeById(orderTransport.getId());
        OrderTransport byId = orderTransportService.getById(orderId);
        if (createUserTypeById != CreateUserTypeEnum.SCM.getCode()) {
            return;
        }

        try {
            new Thread(() -> {
                OrderTransport orderTransportTemp = this.getOne(new QueryWrapper<OrderTransport>().lambda()
                        .select(OrderTransport::getLegalName, OrderTransport::getThirdPartyOrderNo, OrderTransport::getOrderNo)
                        .eq(OrderTransport::getId, orderTransport.getId()));

                OrderSendCars orderSendCarsTemp = orderSendCarsService.getOne(new QueryWrapper<OrderSendCars>().lambda()
                        .select(OrderSendCars::getVehicleId, OrderSendCars::getDriverInfoId)
                        .eq(OrderSendCars::getOrderNo, orderTransportTemp.getOrderNo()), false);
                if (orderSendCarsTemp == null) {
                    logger.error("????????????????????????????????????????????????????????????????????????????????????");
                    return;
                }

                ScmTransportationInformationForm scmTransportationInformationForm = new ScmTransportationInformationForm();
                scmTransportationInformationForm.setTruckNo(orderTransportTemp.getThirdPartyOrderNo());

                //????????????
                ApiResult driver = this.omsClient.getDriverById(orderSendCarsTemp.getDriverInfoId());
                JSONObject driverObject = new JSONObject(driver.getData());
                scmTransportationInformationForm.setDriverName(driverObject.getStr("name"));
                scmTransportationInformationForm.setDriverTel(driverObject.getStr("phone"));
                // ??????
                ApiResult vehicleInfo = omsClient.getVehicleInfoById(orderSendCarsTemp.getVehicleId());
                JSONObject vehicleInfoObject = new JSONObject(vehicleInfo.getData());
                scmTransportationInformationForm.setHkTruckNo(vehicleInfoObject.getStr("hkNumber"));
                scmTransportationInformationForm.setCnTruckNo(vehicleInfoObject.getStr("plateNumber"));

                // ?????????????????????????????????????????????????????????????????????????????????
                scmTransportationInformationForm.setTruckCompany(orderTransportTemp.getLegalName());

                String string = null;
                try {
                    string = scmOrderService.acceptTransportationInformation(scmTransportationInformationForm,byId.getUnitCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if(string==null){
//                    return CommonResult.error(400, "????????????");
//                }
                if(Integer.parseInt(string)!=0){
                    System.out.println("???????????? ???"+string);
                    logger.warn("?????????????????????????????????????????? :" ,string);
                }
//                if (MapUtil.getInt(res, "code") != 0) {
//                    logger.warn("??????????????????????????????????????????????????????{}", res.get("msg"));
//                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("??????????????????????????????????????????????????????{}", e.getMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    @Override
    public Integer getCreateUserTypeById(Long id) {
        OrderTransport orderTransport = this.getOne(new QueryWrapper<OrderTransport>().lambda()
                .select(OrderTransport::getCreateUserType)
                .eq(OrderTransport::getId, id));
        return orderTransport.getCreateUserType();
    }

    @Override
    public List<OrderTransportVO> getOrderTransportList(String pickUpTimeStart, String pickUpTimeEnd, String orderNo) {
        return baseMapper.getOrderTransportList(pickUpTimeStart, pickUpTimeEnd, orderNo);
    }

    /**
     * ??????????????????????????????
     *
     * @param mainOrderNo
     * @return
     */
    @Override
    public OrderTransportVO getOrderTransportOne(String mainOrderNo) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no", mainOrderNo);

        OrderTransport orderTransport = this.getOne(queryWrapper);

        OrderTransportVO outOrderTransportVO = ConvertUtil.convert(orderTransport, OrderTransportVO.class);
        return outOrderTransportVO;
    }


}
