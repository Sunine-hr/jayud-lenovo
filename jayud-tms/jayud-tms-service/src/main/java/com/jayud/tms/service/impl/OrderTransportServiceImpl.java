package com.jayud.tms.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.feign.FileClient;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.model.bo.*;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.IOrderTransportService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 中港运输订单 服务实现类
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
    OmsClient omsClient;

    @Autowired
    FileClient fileClient;

    @Autowired
    OauthClient oauthClient;


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

        if (orderTransport.getId() != null) {//修改
            //修改时,先把以前的收货信息清空
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no", form.getOrderNo());
            orderTakeAdrService.remove(queryWrapper);//删除货物信息
            orderTransport.setUpdatedTime(LocalDateTime.now());
            orderTransport.setUpdatedUser(form.getLoginUser());
        } else {//新增
            //生成订单号
//            String orderNo = StringUtils.loadNum(CommonConstant.T, 12);
//            while (true) {
//                if (!isExistOrder(orderNo)) {//重复
//                    orderNo = StringUtils.loadNum(CommonConstant.T, 12);
//                } else {
//                    break;
//                }
//            }
//            orderTransport.setOrderNo(orderNo);
            orderTransport.setCreatedUser(form.getLoginUser());
        }
        for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
            //有的地址是创建订单填的,有的地址是从地址簿选的
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setId(inputOrderTakeAdrForm.getDeliveryId());
            deliveryAddress.setCustomerId(inputOrderTakeAdrForm.getCustomerId());
            deliveryAddress.setContacts(inputOrderTakeAdrForm.getContacts());
            deliveryAddress.setPhone(inputOrderTakeAdrForm.getPhone());
            deliveryAddress.setAddress(inputOrderTakeAdrForm.getAddress());
            deliveryAddressService.saveOrUpdate(deliveryAddress);

            OrderTakeAdr orderTakeAdr = ConvertUtil.convert(inputOrderTakeAdrForm, OrderTakeAdr.class);
            orderTakeAdr.setDeliveryId(deliveryAddress.getId());
            orderTakeAdr.setId(inputOrderTakeAdrForm.getTakeAdrId());
            orderTakeAdr.setTakeTime(inputOrderTakeAdrForm.getTakeTimeStr());
            orderTakeAdr.setOrderNo(orderTransport.getOrderNo());
            orderTakeAdr.setFile(StringUtils.getFileStr(inputOrderTakeAdrForm.getTakeFiles()));
            orderTakeAdr.setFileName(StringUtils.getFileNameStr(inputOrderTakeAdrForm.getTakeFiles()));
            orderTakeAdrService.saveOrUpdate(orderTakeAdr);
        }
        orderTransport.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
        orderTransport.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
        if (form.getIsGoodsEdit() == null || !form.getIsGoodsEdit()) {
            orderTransport.setStatus(OrderStatusEnum.TMS_T_0.getCode());
        }
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
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
        //获取提货/送货地址
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(inputOrderTransportVO.getOrderNo());
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();
        List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();
        Integer totalAmount = 0;//总件数
        Double totalWeight = 0.0;//总重量
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            inputOrderTakeAdrVO.setTakeFiles(StringUtils.getFileViews(inputOrderTakeAdrVO.getFile(), inputOrderTakeAdrVO.getFileName(), prePath));
            if (CommonConstant.VALUE_1.equals(String.valueOf(inputOrderTakeAdrVO.getOprType()))) {//提货
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
                orderTakeAdrForms2.add(inputOrderTakeAdrVO);  //送货
            }
        }
        inputOrderTransportVO.setTotalAmount(totalAmount);
        inputOrderTransportVO.setTotalWeight(totalWeight);
        inputOrderTransportVO.setOrderTakeAdrForms1(orderTakeAdrForms1);
        inputOrderTransportVO.setOrderTakeAdrForms2(orderTakeAdrForms2);
        return inputOrderTransportVO;
    }

    /**
     * 根据条件获取中港信息
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
        //定义分页参数
        Page<OrderTransportVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("ot.id"));

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        IPage<OrderTransportVO> pageInfo = baseMapper.findTransportOrderByPage(page, form, legalIds);
        if (pageInfo.getRecords().size() == 0) {
            return pageInfo;
        }

        String prePath = fileClient.getBaseUrl().getData().toString();

        List<OrderTransportVO> pageList = pageInfo.getRecords();
        List<String> subOrderNos = pageList.stream().map(OrderTransportVO::getOrderNo).collect(Collectors.toList());
        //查询提货商品信息
//        List<OrderTakeAdr> orderTakeAdrs = this.orderTakeAdrService.getOrderTakeAdrByOrderNos(subOrderNos, OrderTakeAdrTypeEnum.ONE.getCode());
        //是否录用费用


//        List<OrderTransportVO> pageList = pageInfo.getRecords();
//        List<String> orderNo = pageList.stream().map(OrderTransportVO::getOrderNo).collect(Collectors.toList());
//        List<OrderTakeAdr> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrByOrderNos(orderNo, null);
        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(subOrderNos, null);
        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.ZGYS.getSignOne()).getData();
        Map<String, Object> costStatus = omsClient.getCostStatus(null, subOrderNos).getData();
        for (OrderTransportVO orderTransportVO : pageList) {
//            orderTransportVO.assemblyGoodsInfo(orderTakeAdrs);
//            orderTransportVO.assemblyTakeFiles(takeAdrsList, prePath);
            orderTransportVO.setCost(MapUtil.getBool(data, orderTransportVO.getOrderNo()));
            orderTransportVO.assemblyTakeAdrInfos(takeAdrsList, prePath);
            orderTransportVO.assemblyCostStatus(costStatus);
        }

        return pageInfo;
    }

    @Override
    public SendCarPdfVO initPdfData(String orderNo, String classCode) {
        SendCarPdfVO sendCarPdfVO = baseMapper.initPdfData(orderNo, classCode);
        if (sendCarPdfVO == null) {
            return new SendCarPdfVO();
        }
        //香港清关地址又说不要了BUG-237
        sendCarPdfVO.setClearCustomsAddress("");
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(orderNo);
        List<TakeGoodsInfoVO> takeGoodsInfo1 = new ArrayList<>();
        List<TakeGoodsInfoVO> takeGoodsInfo2 = new ArrayList<>();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            inputOrderTakeAdrVO.setVehicleSize(sendCarPdfVO.getVehicleSize());
            inputOrderTakeAdrVO.setVehicleType(sendCarPdfVO.getVehicleType());
            inputOrderTakeAdrVO.setCntrNo(sendCarPdfVO.getCntrNo());
            if (CommonConstant.VALUE_1.equals(String.valueOf(inputOrderTakeAdrVO.getOprType()))) {//提货
                takeGoodsInfo1.add(ConvertUtil.convert(inputOrderTakeAdrVO, TakeGoodsInfoVO.class));
            } else {
                takeGoodsInfo2.add(ConvertUtil.convert(inputOrderTakeAdrVO, TakeGoodsInfoVO.class));
            }
        }
        //提货信息
        sendCarPdfVO.setTakeInfo1(takeGoodsInfo1);
        //送货地址/联系人/联系电话/装车要求
        OrderSendCarsVO orderSendCarsVO = orderSendCarsService.getOrderSendInfo(orderNo);
        if (orderSendCarsVO == null) {
            return sendCarPdfVO;
        }
        if (takeGoodsInfo2.size() > 1) {//获取中转仓信息
            if (orderSendCarsVO.getIsVirtual() == null || !orderSendCarsVO.getIsVirtual()) {
                sendCarPdfVO.setDeliveryContacts(orderSendCarsVO.getWarehouseContacts());
                String provinceName = orderSendCarsVO.getProvinceName() == null ? "" : orderSendCarsVO.getProvinceName();
                String cityName = orderSendCarsVO.getCityName() == null ? "" : orderSendCarsVO.getCityName();
                String address = orderSendCarsVO.getAddress() == null ? "" : orderSendCarsVO.getAddress();
                String detailedAddress = provinceName + cityName + address;
                sendCarPdfVO.setDeliveryAddress(detailedAddress +
                        " 联系人:" + orderSendCarsVO.getWarehouseContacts() + " " + orderSendCarsVO.getWarehouseNumber());
                sendCarPdfVO.setDeliveryPhone(orderSendCarsVO.getWarehouseNumber());
            } else {
                //虚拟仓展示多个地址
                sendCarPdfVO.assembleTakeGoodsInfos2(takeGoodsInfo2);
            }
        } else if (takeGoodsInfo2.size() == 1) {
            TakeGoodsInfoVO takeGoodsInfoVO = takeGoodsInfo2.get(0);
            String provinceName = takeGoodsInfoVO.getStateName() == null ? "" : takeGoodsInfo2.get(0).getStateName();
            String cityName = takeGoodsInfoVO.getCityName() == null ? "" : takeGoodsInfo2.get(0).getCityName();
            String address = takeGoodsInfoVO.getAddress() == null ? "" : takeGoodsInfo2.get(0).getAddress();
            sendCarPdfVO.setDeliveryAddress(provinceName + cityName + address +
                    " 联系人:" + takeGoodsInfoVO.getContacts() + " " + takeGoodsInfoVO.getPhone());
            sendCarPdfVO.setDeliveryContacts(takeGoodsInfo2.get(0).getContacts());
            sendCarPdfVO.setDeliveryPhone(takeGoodsInfo2.get(0).getPhone());
        }
        sendCarPdfVO.setRemarks(orderSendCarsVO.getRemarks());
        //货物信息,取提货信息
        List<GoodsInfoVO> goodsInfoVOS = new ArrayList<>();
        Integer totalPieceAmount = 0;//总件数
        Double totalWeight = 0.0;//总重量
        Double totalVolume = 0.0;//总体积
        Integer totalPlateAmount = 0;//总板数
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
        //总件数/总重量/总体积
        sendCarPdfVO.setTotalPieceAmount(totalPieceAmount);
        sendCarPdfVO.setTotalWeight(totalWeight);
        sendCarPdfVO.setTotalVolume(totalVolume);
        sendCarPdfVO.setTotalPlateAmount(totalPlateAmount);
        return sendCarPdfVO;
    }

    /**
     * 分页查询司机的中港订单信息
     *
     * @param form
     * @return
     */
    @Override
    public List<DriverOrderTransportVO> getDriverOrderTransport(QueryDriverOrderTransportForm form) {
        //状态集
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
        //查询订单提货/送货地址
        if (CollectionUtils.isNotEmpty(orderNoList)) {
            List<DriverOrderTakeAdrVO> adrs = this.orderTakeAdrService.getDriverOrderTakeAdr(orderNoList, null);
            list.forEach(tmp -> {
                tmp.setStatus(OrderStatusEnum.getDesc(tmp.getStatus()));
                tmp.groupAddr(adrs);
                tmp.setTakeOrders(form.getOrderIds() == null ? form.getExcludeOrderIds() : form.getOrderIds());
            });
            //组装提货和送货地址
            list.forEach(tmp -> {
                tmp.assemblyAddr();
                tmp.assemblyGoodsName();
            });
        }

        return list;
    }

    /**
     * 获取中港订单状态
     */
    @Override
    public String getOrderTransportStatus(String orderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().select(OrderTransport::getStatus).eq(OrderTransport::getOrderNo, orderNo);
        return this.getOne(condition).getStatus();
    }

    /**
     * 司机反馈状态
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
            case CommonConstant.CAR_TAKE_GOODS://车辆提货
                code = OrderStatusEnum.TMS_T_5.getCode();
                desc = OrderStatusEnum.TMS_T_5.getDesc();
                break;
            case CommonConstant.CAR_WEIGH://车辆过磅
                code = OrderStatusEnum.TMS_T_6.getCode();
                desc = OrderStatusEnum.TMS_T_6.getDesc();
                orderTransport.setCarWeighNum(form.getCarWeighNum());
                break;
            case CommonConstant.CAR_GO_CUSTOMS://车辆通关
                code = form.getStatus();
                if (OrderStatusEnum.TMS_T_9_1.getCode().equals(form.getStatus())) {
                    orderTransport.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                } else if (OrderStatusEnum.TMS_T_9_2.getCode().equals(form.getStatus())) {
                    orderTransport.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                }

                if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus())) {
                    //记录操作成功状态
                    desc = OrderStatusEnum.TMS_T_9.getDesc();
                }
                auditInfoForm.setAuditTypeDesc(CommonConstant.CAR_GO_CUSTOMS_DESC);
                break;
            case CommonConstant.CAR_ENTER_WAREHOUSE://车辆入仓
                code = OrderStatusEnum.TMS_T_10.getCode();
                desc = OrderStatusEnum.TMS_T_10.getDesc();
                break;
            case CommonConstant.CAR_OUT_WAREHOUSE://车辆出仓
                code = OrderStatusEnum.TMS_T_13.getCode();
                desc = OrderStatusEnum.TMS_T_13.getDesc();

                //车辆出仓后:中转仓卸货装货已完成
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
            case CommonConstant.CAR_SEND: //车辆派送
                code = OrderStatusEnum.TMS_T_14.getCode();
                desc = OrderStatusEnum.TMS_T_14.getDesc();
                break;
            case CommonConstant.CONFIRM_SIGN_IN://确认签收
                code = OrderStatusEnum.TMS_T_15.getCode();
                desc = OrderStatusEnum.TMS_T_15.getDesc();
                break;
        }

        orderTransport.setStatus(code);
        form.setStatus(code);
        form.setStatusName(desc);
        auditInfoForm.setAuditStatus(code);
        auditInfoForm.setAuditTypeDesc(desc);

        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        omsClient.saveOprStatus(form);
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);

    }

    /**
     * 小程序司机车辆通关（补出仓和入仓数据）,送货地址只有一个时候才做这个操作
     */
    @Override
    @Transactional
    public void driverCustomsClearanceVehicles(OprStatusForm form) {
        //车辆通关
        String cmd = form.getCmd();
        form.setCmd(cmd);
        this.doDriverFeedbackStatus(form);
        //只有审核通过才走下面流程
        if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus())) {
            //车辆入仓数据
            form.setCmd(CommonConstant.CAR_ENTER_WAREHOUSE);
            this.doDriverFeedbackStatus(form);
            //车辆出仓数据
            form.setCmd(CommonConstant.CAR_OUT_WAREHOUSE);
            this.doDriverFeedbackStatus(form);
        }

    }

    @Override
    public StatisticsDataNumberVO statisticsDataNumber() {
        return baseMapper.statisticsDataNumber();
    }

    /**
     * 根据主订单号集合查询中港信息
     */
    @Override
    public List<OrderTransport> getTmsOrderByMainOrderNos(List<String> mainOrders) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderTransport::getMainOrderNo, mainOrders);
        return this.baseMapper.selectList(condition);
    }


    /**
     * 根据主订单号集合查询中港详情
     */
    @Override
    public List<OrderVO> getOrderTransportByMainOrderNo(List<String> mainOrders) {
        return this.baseMapper.getOrderTransportByMainOrderNo(mainOrders);
    }

    /**
     * 查询订单状态数量
     *
     * @param status
     * @param legalIds
     * @return
     */
    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        Integer num = 0;
        switch (status) {
            case "CostAudit":
                List<OrderTransport> list = this.getByLegalEntityId(legalIds);
                if (CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(OrderTransport::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.ZGYS.getSignOne(), legalIds, orderNos).getData();
                break;
            default:
                num = this.baseMapper.getNumByStatus(status, legalIds);
        }
        return num == null ? 0 : num;
    }

    /**
     * 根据主订单号集合查询中港详情信息
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

}
