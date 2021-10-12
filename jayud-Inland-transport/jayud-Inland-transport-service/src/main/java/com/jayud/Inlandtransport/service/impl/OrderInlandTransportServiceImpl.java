package com.jayud.Inlandtransport.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.mapper.OrderInlandTransportMapper;
import com.jayud.Inlandtransport.model.bo.*;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.*;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.AuditInfoForm;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jayud.common.enums.OrderStatusEnum.getInlandTPStatus;

/**
 * <p>
 * 内陆订单 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Service
@Slf4j
public class OrderInlandTransportServiceImpl extends ServiceImpl<OrderInlandTransportMapper, OrderInlandTransport> implements IOrderInlandTransportService {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderInlandSendCarsService orderInlandSendCarsService;

    //创建订单
    @Override
    @Transactional
    public String createOrder(AddOrderInlandTransportForm form) {
        LocalDateTime now = LocalDateTime.now();
        OrderInlandTransport inlandOrder = ConvertUtil.convert(form, OrderInlandTransport.class);
        //生成订单号
        String orderNo = null;
        //创建内陆运输订
        if (form.getId() == null) {
            //生成订单号
            orderNo = form.getOrderNo();
            inlandOrder.setOrderNo(orderNo).setCreateTime(now)
                    .setCreateUser(form.getCreateUser())
                    .setStatus(OrderStatusEnum.INLANDTP_NL_0.getCode());
            this.save(inlandOrder);
        } else {
            OrderInlandTransport tmp = this.getById(form.getId());
            orderNo = StringUtils.isEmpty(form.getOrderNo()) ? tmp.getOrderNo() : form.getOrderNo();
            //修改内陆运输订单
            inlandOrder.setStatus(OrderStatusEnum.INLANDTP_NL_0.getCode())
                    .setUpdateTime(now).setUpdateUser(form.getCreateUser());
            this.updateById(inlandOrder);
        }
        //地址数据合拼
        List<OrderDeliveryAddress> addressList = form.getPickUpAddressList();
        addressList.forEach(e -> e.setAddressType(OrderAddressEnum.PICK_UP.getCode()));
        if (form.getOrderDeliveryAddressList() != null) {
            form.getOrderDeliveryAddressList().forEach(e ->
                    e.setAddressType(OrderAddressEnum.DELIVERY.getCode()));
            addressList.addAll(form.getOrderDeliveryAddressList());
        }
        for (OrderDeliveryAddress orderDeliveryAddress : addressList) {
            orderDeliveryAddress.setBusinessId(inlandOrder.getId())
                    .setOrderNo(orderNo)
                    .setBusinessType(BusinessTypeEnum.NL.getCode());
        }
        this.omsClient.addDeliveryAddress(addressList);
        return inlandOrder.getOrderNo();
    }

    @Override
    public IPage<OrderInlandTransportFormVO> findByPage(QueryOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<OrderInlandTransport> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<OrderInlandTransportFormVO> iPage = this.baseMapper.findByPage(page, form, legalIds);

        List<OrderInlandTransportFormVO> records = iPage.getRecords();
        if (iPage.getRecords().size() == 0) {
            return iPage;
        }

        List<Long> orderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
//        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();
        for (OrderInlandTransportFormVO record : records) {
            orderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
//            entityIds.add(record.getLegalEntityId());
            subOrderNos.add(record.getOrderNo());
            if (record.getSupplierId() != null) {
                supplierIds.add(record.getSupplierId());
            }

        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(orderIds, BusinessTypeEnum.NL.getCode()).getData();
        //查询订单地址
        List<OrderAddressVO> orderAddressList = this.omsClient.getOrderAddressByBusIds(orderIds, BusinessTypeEnum.NL.getCode()).getData();
        //查询费用状态
        Map<String, Object> costStatus = omsClient.getCostStatus(null, subOrderNos).getData();

        //查询法人主体
//        ApiResult legalEntityResult = null;
//        if (CollectionUtils.isNotEmpty(entityIds)) {
//            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
//        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(supplierIds)) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (OrderInlandTransportFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装地址信息
            record.assemblyAddressInfo(orderAddressList);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);
            //组装费用费用状态
            record.assemblyCostStatus(costStatus);
        }

        return iPage;
    }

    /**
     * 修改节点流程
     *
     * @param orderInlandTransport
     * @param form
     */
    @Override
    @Transactional
    public void updateProcessStatus(OrderInlandTransport orderInlandTransport, ProcessOptForm form) {
        orderInlandTransport.setId(form.getOrderId());
        orderInlandTransport.setUpdateTime(LocalDateTime.now());
        orderInlandTransport.setUpdateUser(UserOperator.getToken());
        orderInlandTransport.setStatus(form.getStatus());

        //修改派车信息
        updateSendCars(form);
        //更新状态节点状态
        this.baseMapper.updateById(orderInlandTransport);
        //节点操作记录
        this.processOptRecord(form);
        //完成订单状态
        finishOrderOpt(orderInlandTransport);
    }


    /**
     * 节点操作记录
     *
     * @param form
     */
    @Override
    public void processOptRecord(ProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.INLAND_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());
        auditInfoForm.setAuditUser(form.getOperatorUser());
        //轨迹
        OprStatusForm oprStatusForm = new OprStatusForm();
        oprStatusForm.setMainOrderId(form.getMainOrderId());
        oprStatusForm.setOrderId(form.getOrderId());
        oprStatusForm.setStatus(form.getStatus());
        oprStatusForm.setStatusName(form.getStatusName());
        oprStatusForm.setOperatorUser(form.getOperatorUser());
        oprStatusForm.setOperatorTime(form.getOperatorTime());
        oprStatusForm.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        oprStatusForm.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        oprStatusForm.setBusinessType(form.getBusinessType());
        oprStatusForm.setDescription(form.getDescription());
        oprStatusForm.setBusinessType(BusinessTypeEnum.NL.getCode());

        if (omsClient.saveOprStatus(oprStatusForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }
    }

    @Override
    @Transactional
    public void doDispatchOpt(ProcessOptForm form) {
        //保存派车信息
        SendCarForm sendCarForm = form.getSendCarForm();
        //修改派车
        OrderInlandSendCars orderInlandSendCars = ConvertUtil.convert(sendCarForm, OrderInlandSendCars.class);
        orderInlandSendCars.setCreateUser(UserOperator.getToken());
        orderInlandSendCars.setCreateTime(LocalDateTime.now());
        orderInlandSendCars.setTransportNo(this.orderInlandSendCarsService.createTransportNo(sendCarForm.getOrderNo()));
        this.orderInlandSendCarsService.saveOrUpdate(orderInlandSendCars);
        //同步修改内陆单车型和尺寸
        this.updateById(new OrderInlandTransport().setId(form.getOrderId())
                .setVehicleSize(orderInlandSendCars.getVehicleSize())
                .setVehicleType(orderInlandSendCars.getVehicleType()));
        this.updateProcessStatus(new OrderInlandTransport(), form);
    }


    @Override
    public List<OrderInlandTransport> getByCondition(OrderInlandTransport orderInlandTransport) {
        QueryWrapper<OrderInlandTransport> condition = new QueryWrapper<>(orderInlandTransport);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取订单详情
     *
     * @param subOrderId
     * @return
     */
    @Override
    public OrderInlandTransportDetails getOrderDetails(Long subOrderId) {

        //查询订单信息
        OrderInlandTransport orderInlandTransport = this.getById(subOrderId);
        OrderInlandTransportDetails details = ConvertUtil.convert(orderInlandTransport, OrderInlandTransportDetails.class);

        //查询派车信息
        List<OrderInlandSendCars> sendCars = this.orderInlandSendCarsService.getByCondition(new OrderInlandSendCars().setOrderId(subOrderId));
        if (CollectionUtil.isNotEmpty(sendCars)) {
            details.setOrderInlandSendCarsVO(ConvertUtil.convert(sendCars.get(0), OrderInlandSendCarsVO.class));
        }

        //查询提货/送货地址
        List<OrderDeliveryAddress> deliveryAddresses = this.omsClient.getDeliveryAddress(Collections.singletonList(subOrderId),
                BusinessTypeEnum.NL.getCode()).getData();
        details.assembleDeliveryAddress(deliveryAddresses);

        return details;
    }


    private void finishOrderOpt(OrderInlandTransport order) {
        if (OrderStatusEnum.INLANDTP_NL_6.getCode().equals(order.getStatus())) {
            this.updateById(new OrderInlandTransport().setId(order.getId())
                    .setProcessStatus(ProcessStatusEnum.COMPLETE.getCode()));
        }
    }

    public List<InitComboxStrVO> initStatus() {
        List<OrderStatusEnum> enums = getInlandTPStatus(true);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (OrderStatusEnum statusEnum : enums) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(statusEnum.getDesc());
            initComboxStrVO.setCode(statusEnum.getCode());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return initComboxStrVOS;
    }


    /**
     * 接单驳回
     */
    @Override
    public void orderReceiving(OrderInlandTransport orderInlandTransport, AuditInfoForm auditInfoForm, OrderRejectedOpt orderRejectedOpt) {
        OrderInlandTransport tmp = new OrderInlandTransport();
        tmp.setId(orderInlandTransport.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        omsClient.doMainOrderRejectionSignOpt(orderInlandTransport.getMainOrderNo(),
                orderInlandTransport.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
        this.updateById(tmp);
    }

    /**
     * 订单驳回
     */
    @Override
    @Transactional
    public void rejectedOpt(OrderInlandTransport orderInlandTransport, AuditInfoForm auditInfoForm,
                            OrderRejectedOpt orderRejectedOpt) {

        OrderInlandTransport tmp = new OrderInlandTransport();
        tmp.setId(orderInlandTransport.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //根据选择是否订舱驳回
        ApiResult result = new ApiResult();
        //删除物流轨迹表订舱数据
        switch (orderRejectedOpt.getRejectOptions()) {
            case 1://订单驳回
                result = omsClient.deleteLogisticsTrackByType(orderInlandTransport.getId(), BusinessTypeEnum.NL.getCode());
                //删除派车数据
                this.orderInlandSendCarsService.deleteByOrderNo(orderInlandTransport.getOrderNo());
                //执行主订单驳回标识
                omsClient.doMainOrderRejectionSignOpt(orderInlandTransport.getMainOrderNo(),
                        orderInlandTransport.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
                break;
            case 2://派车驳回
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(orderInlandTransport.getId());
                form.setStatus(orderRejectedOpt.getDeleteStatusList());
                result = this.omsClient.delSpecOprStatus(form);
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订舱轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        //更改为驳回状态
        this.updateById(tmp);
    }

    /**
     * 根据主单号集合查询子订单信息
     *
     * @param mainOrderNos
     * @return
     */
    @Override
    public List<OrderInlandTransport> getInlandOrderByMainOrderNos(List<String> mainOrderNos) {
        QueryWrapper<OrderInlandTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInlandTransport::getMainOrderNo, mainOrderNos);
        return this.baseMapper.selectList(condition);
    }


    /**
     * 根据子订单号集合查询子订单
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<OrderInlandTransport> getOrdersByOrderNos(List<String> orderNos) {
        QueryWrapper<OrderInlandTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInlandTransport::getOrderNo, orderNos);
        return this.baseMapper.selectList(condition);
    }


    @Override
    public List<OrderInlandTransportDetails> getInlandOrderInfoByMainOrderNos(List<String> mainOrderNos) {
        //查询订单信息
        List<OrderInlandTransportDetails> details = this.baseMapper.getOrderInfoByMainOrderNos(mainOrderNos);
        if (CollectionUtils.isEmpty(details)) {
            return new ArrayList<>();
        }
        List<Long> subOrderIds = details.stream().map(OrderInlandTransportDetails::getId).collect(Collectors.toList());

        //查询提货/送货地址
        List<OrderDeliveryAddress> deliveryAddresses = this.omsClient.getDeliveryAddress(subOrderIds,
                BusinessTypeEnum.NL.getCode()).getData();
        details.forEach(e -> e.assembleDeliveryAddress(deliveryAddresses));
        return details;
    }


    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds, Map<String, Object> datas) {
        Integer num = this.baseMapper.getNumByStatus(status, legalIds);
        switch (status) {
            case "CostAudit":
                List<OrderInlandTransport> list = this.getByLegalEntityId(legalIds);
                if (org.apache.commons.collections4.CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(OrderInlandTransport::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.NL.getSignOne(), legalIds, orderNos).getData();
                break;
            case "inlandReceiverCheck":
                Map<String, Integer> costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            case "inlandPayCheck":
                costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            default:
                num = this.baseMapper.getNumByStatus(status, legalIds);
        }

        return num == null ? 0 : num;
    }

    private void updateSendCars(ProcessOptForm form) {
        if (OrderStatusEnum.INLANDTP_NL_3.getCode().equals(form.getStatus())) {
            List<OrderInlandSendCars> sendCarsList = this.orderInlandSendCarsService.getByCondition(new OrderInlandSendCars().setOrderId(form.getOrderId()));
            OrderInlandSendCars sendCars = sendCarsList.get(0);
            this.orderInlandSendCarsService.updateById(new OrderInlandSendCars().setId(sendCars.getId())
                    .setDescribes(form.getDescribes()));
            form.setDescription(form.getDescribes());
        }
    }


    @Override
    public List<OrderInlandTransport> getByLegalEntityId(List<Long> legalIds) {
        QueryWrapper<OrderInlandTransport> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInlandTransport::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }

    @Override
    @Transactional
    public void editGoods(OrderInlandTransportDetails from) {
        OrderInlandTransport orderInlandTransport = new OrderInlandTransport().setId(from.getId())
                .setVehicleSize(from.getVehicleSize());

        List<OrderDeliveryAddress> pickUpAddressList = from.getPickUpAddressList();
        List<OrderDeliveryAddress> orderDeliveryAddressList = from.getOrderDeliveryAddressList();
        pickUpAddressList.addAll(orderDeliveryAddressList);
        ApiResult result = this.omsClient.addDeliveryAddress(pickUpAddressList);
        if (result.isOk()) {
            this.updateById(orderInlandTransport);
        }
    }

    /**
     * 获取内陆订单list
     * @param pickUpTimeStart 提货时间Start
     * @param pickUpTimeEnd 提后时间End
     * @param orderNo 订单号
     * @return
     */
    @Override
    public List<OrderInlandTransportFormVO> getOrderInlandTransportList(String pickUpTimeStart, String pickUpTimeEnd, String orderNo) {
        return baseMapper.getOrderInlandTransportList(pickUpTimeStart, pickUpTimeEnd, orderNo);
    }

}
