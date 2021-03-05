package com.jayud.Inlandtransport.service.impl;

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
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.AuditInfoForm;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderAddressEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        inlandOrder.setVehicleType(1);//吨车
        //创建内陆运输订单
        if (form.getId() == null) {
            //生成订单号
            String orderNo = form.getOrderNo();
            inlandOrder.setOrderNo(orderNo).setCreateTime(now)
                    .setCreateUser(form.getCreateUser())
                    .setStatus(OrderStatusEnum.INLANDTP_NL_0.getCode());
            this.save(inlandOrder);
        } else {
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
                    .setOrderNo(inlandOrder.getOrderNo())
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
        return this.baseMapper.findByPage(page, form, legalIds);
    }

    /**
     * 修改节点流程
     *
     * @param orderInlandTransport
     * @param form
     */
    @Override
    public void updateProcessStatus(OrderInlandTransport orderInlandTransport, ProcessOptForm form) {
        orderInlandTransport.setId(form.getOrderId());
        orderInlandTransport.setUpdateTime(LocalDateTime.now());
        orderInlandTransport.setUpdateUser(UserOperator.getToken());
        orderInlandTransport.setStatus(form.getStatus());

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
        OrderInlandSendCars orderInlandSendCars = ConvertUtil.convert(sendCarForm, OrderInlandSendCars.class);
        orderInlandSendCars.setCreateUser(UserOperator.getToken());
        orderInlandSendCars.setCreateTime(LocalDateTime.now());
        this.orderInlandSendCarsService.save(orderInlandSendCars);

        this.updateProcessStatus(new OrderInlandTransport(), form);
    }


    @Override
    public List<OrderInlandTransport> getByCondition(OrderInlandTransport orderInlandTransport) {
        QueryWrapper<OrderInlandTransport> condition = new QueryWrapper<>();
        return this.baseMapper.selectList(condition);
    }


    private void finishOrderOpt(OrderInlandTransport order) {
        if (OrderStatusEnum.INLANDTP_NL_6.getCode().equals(order.getStatus())) {
            this.updateById(new OrderInlandTransport().setId(order.getId())
                    .setProcessStatus(ProcessStatusEnum.COMPLETE.getCode()));
        }
    }

}
