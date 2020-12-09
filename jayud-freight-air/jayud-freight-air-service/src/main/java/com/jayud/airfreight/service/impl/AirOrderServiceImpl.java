package com.jayud.airfreight.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.airfreight.feign.MsgClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.mapper.AirOrderMapper;
import com.jayud.airfreight.model.po.Goods;
import com.jayud.airfreight.model.po.OrderAddress;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.IAirOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.service.IGoodsService;
import com.jayud.airfreight.service.IOrderAddressService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 空运订单表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
@Service
@Slf4j
public class AirOrderServiceImpl extends ServiceImpl<AirOrderMapper, AirOrder> implements IAirOrderService {

    @Autowired
    private IOrderAddressService orderAddressService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IAirBookingService airBookingService;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private VivoServiceImpl vivoService;

    //创建订单
    @Override
    @Transactional
    public void createOrder(AddAirOrderForm addAirOrderForm) {

        LocalDateTime now = LocalDateTime.now();
        AirOrder airOrder = ConvertUtil.convert(addAirOrderForm, AirOrder.class);
        //创建空运单
        if (addAirOrderForm.getId() == null) {
            //生成订单号
            String orderNo = generationOrderNo();
            airOrder.setOrderNo(orderNo).setCreateTime(now)
                    .setStatus(OrderStatusEnum.AIR_A_0.getCode());
            this.save(airOrder);
        } else {
            //修改空运单
            airOrder.setStatus(OrderStatusEnum.AIR_A_0.getCode())
                    .setUpdateTime(now).setUpdateUser(addAirOrderForm.getCreateUser());
            this.updateById(airOrder);
        }

        List<AddOrderAddressForm> orderAddressForms = addAirOrderForm.getOrderAddressForms();
        List<OrderAddress> orderAddresses = new ArrayList<>();
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            OrderAddress orderAddress = ConvertUtil.convert(orderAddressForm, OrderAddress.class);
            orderAddress.setBusinessType(BusinessTypeEnum.KY.getCode())
                    .setBusinessId(airOrder.getId())
                    .setCreateTime(orderAddressForm.getId() == null ? now : null);
            orderAddresses.add(orderAddress);
        }
        //批量保存用户地址
        this.orderAddressService.saveOrUpdateBatch(orderAddresses);

        List<AddGoodsForm> goodsForms = addAirOrderForm.getGoodsForms();
        List<Goods> goodsList = new ArrayList<>();
        for (AddGoodsForm goodsForm : goodsForms) {
            Goods goods = ConvertUtil.convert(goodsForm, Goods.class);
            goods.setBusinessId(airOrder.getId())
                    .setBusinessType(BusinessTypeEnum.KY.getCode())
                    .setCreateTime(goodsForm.getId() == null ? now : null);
            goodsList.add(goods);
        }

        //批量保存货物信息
        goodsService.saveOrUpdateBatch(goodsList);
    }

    /**
     * 生成订单号
     */
    @Override
    public String generationOrderNo() {
        //生成订单号
        String orderNo = StringUtils.loadNum(CommonConstant.A, 12);
        while (true) {
            if (isExistOrder(orderNo)) {//重复
                orderNo = StringUtils.loadNum(CommonConstant.A, 12);
            } else {
                break;
            }
        }
        return orderNo;
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    @Override
    public IPage<AirOrderFormVO> findByPage(QueryAirOrderForm form) {
        if (form.getStatus() == null) { //订单列表
            form.setProcessStatusList(Arrays.asList(AirProcessStatusEnum.PROCESSING.getCode()
                    , AirProcessStatusEnum.COMPLETE.getCode()));
        }else {
            form.setProcessStatusList(Collections.singletonList(AirProcessStatusEnum.PROCESSING.getCode()));
        }

        Page<AirOrder> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }


    /**
     * 更新流程状态
     */
    @Transactional
    @Override
    public void updateProcessStatus(AirOrder airOrder, AirProcessOptForm form) {
        airOrder.setId(form.getOrderId());
        airOrder.setUpdateTime(LocalDateTime.now());
        airOrder.setUpdateUser(UserOperator.getToken());
        airOrder.setStatus(form.getStatus());

        //更新状态节点状态
        this.baseMapper.updateById(airOrder);
        //节点操作记录
        this.airProcessOptRecord(form);
        //完成订单状态
        finishAirOrderOpt(airOrder);
    }

    /**
     * 空运流程操作记录
     */
    @Override
    public void airProcessOptRecord(AirProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.AIR_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());

        //文件拼接
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.KY.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    /**
     * 订舱操作
     */
    @Override
    @Transactional
    public void doAirBookingOpt(AirProcessOptForm form) {
        AddAirBookingForm airBookingForm = form.getAirBooking();
        AirBooking airBooking = ConvertUtil.convert(airBookingForm, AirBooking.class);
        //处理提单文件
        this.handleLadingBillFile(airBooking, form);
        //设置订舱状态
        setAirBookingStatus(airBooking);
        airBookingService.saveOrUpdateAirBooking(airBooking);
        updateProcessStatus(new AirOrder(), form);
        //消息推送
        this.messagePush(airBooking, form);
    }


    private void messagePush(AirBooking airBooking, AirProcessOptForm form) {
        //订舱推送
        this.bookingMessagePush(airBooking, form);
        //推送提单信息
        this.billLadingInfoPush(form);
    }

    /**
     * 是否能入仓
     *
     * @param airOrder
     * @return
     */
    @Override
    public boolean isWarehousing(AirOrder airOrder) {
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            //查询订舱状态
            AirBooking airBooking = this.airBookingService.getByAirOrderId(airOrder.getId());
            if ("1".equals(airBooking.getStatus())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 根据第三方唯一编码查询空运订单
     */
    @Override
    public AirOrder getByThirdPartyOrderNo(String thirdPartyOrderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getThirdPartyOrderNo, thirdPartyOrderNo);
        return this.getOne(condition);
    }

    /**
     * 订舱驳回
     */
    @Override
    @Transactional
    public void bookingRejected(AirOrder airOrder) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(airOrder.getId());
        auditInfoForm.setExtDesc(SqlConstant.AIR_ORDER);
        auditInfoForm.setAuditStatus((OrderStatusEnum.AIR_A_2_1.getCode()));
        auditInfoForm.setAuditTypeDesc(OrderStatusEnum.AIR_A_2_1.getDesc());
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        //删除物流轨迹表订舱数据
        DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
        delOprStatusForm.setOrderId(airOrder.getId());
        delOprStatusForm.setStatus(Collections.singletonList(OrderStatusEnum.AIR_A_2.getCode()));
        if (omsClient.delSpecOprStatus(delOprStatusForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订舱轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        //更改为驳回状态
        this.updateById(new AirOrder().setId(airOrder.getId()).setStatus(OrderStatusEnum.AIR_A_2_1.getCode()));
    }

    private void setAirBookingStatus(AirBooking airBooking) {
        if (airBooking.getId() != null) {
            return;
        }
        AirOrder airOrder = this.getById(airBooking.getAirOrderId());
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            airBooking.setStatus("1");
        } else {
            airBooking.setStatus("0");
        }
    }

    private void finishAirOrderOpt(AirOrder airOrder) {
        if (OrderStatusEnum.AIR_A_6.getCode().equals(airOrder.getStatus())) {
            //查询空运订单信息
            AirOrder tmp = this.getById(airOrder.getId());
            if (AirOrderTermsEnum.CIF.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.FOB.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.CFR.getCode().equals(tmp.getTerms())) {
                this.updateById(new AirOrder().setId(tmp.getId()).setProcessStatus(1));
                return;
            }
        }

        if (OrderStatusEnum.AIR_A_8.getCode().equals(airOrder.getStatus())) {
            this.updateById(new AirOrder().setId(airOrder.getId()).setProcessStatus(1));
        }
    }

    private void handleLadingBillFile(AirBooking airBooking, AirProcessOptForm form) {
        if (OrderStatusEnum.AIR_A_4.getCode().equals(form.getStatus())) {
            airBooking.setFilePath(StringUtils.getFileStr(form.getFileViewList()))
                    .setFileName(StringUtils.getFileNameStr(form.getFileViewList()));
        }
    }

    /**
     * 订舱消息推送
     */
    private void bookingMessagePush(AirBooking airBooking, AirProcessOptForm form) {
        if (!OrderStatusEnum.AIR_A_2.getCode().equals(form.getStatus())) {
            return;
        }
        AirOrder airOrder = this.getById(airBooking.getAirOrderId());
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            this.vivoService.bookingMessagePush(airOrder, airBooking);
        }
    }

    /**
     * 提单文件推送
     */
    private void billLadingInfoPush(AirProcessOptForm form) {
        if (!OrderStatusEnum.AIR_A_4.getCode().equals(form.getStatus())) {
            return;
        }
        AirOrder airOrder = this.getById(form.getOrderId());
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            AirBooking airBooking = this.airBookingService.getByAirOrderId(airOrder.getId());
            this.vivoService.billLadingInfoPush(airOrder, airBooking);
        }
    }


    /**
     * 跟踪推送
     *
     * @param airOrderId
     */
    @Override
    public void trackingPush(Long airOrderId) {
        AirOrder airOrder = this.getById(airOrderId);
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            this.vivoService.trackingPush(airOrder);
        }
    }


}
