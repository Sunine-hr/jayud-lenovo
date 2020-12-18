package com.jayud.airfreight.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.feign.MsgClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.mapper.AirOrderMapper;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.airfreight.model.enums.VivoRejectionStatusEnum;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.*;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.exception.VivoApiException;
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
import java.util.Map;

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

    //    @Autowired
//    private IOrderAddressService orderAddressService;
    //    @Autowired
//    private IGoodsService goodsService;
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
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            orderAddressForm.setBusinessType(BusinessTypeEnum.KY.getCode());
            orderAddressForm.setBusinessId(airOrder.getId());
        }
        //批量保存用户地址
        ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
        }

        List<AddGoodsForm> goodsForms = addAirOrderForm.getGoodsForms();
        for (AddGoodsForm goodsForm : goodsForms) {
            goodsForm.setBusinessId(airOrder.getId());
            goodsForm.setBusinessType(BusinessTypeEnum.KY.getCode());
        }
        //批量保存货物信息
        result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改商品信息失败,商品信息={}", new JSONArray(goodsForms));
        }

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
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
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
        //查询订舱是否存在,存在做更新操作
        AirBooking oldAirBooking = this.airBookingService.getByAirOrderId(form.getOrderId());
        airBookingForm.setId(oldAirBooking != null ? oldAirBooking.getId() : null);

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
    public void rejectedOpt(AirOrder airOrder, AuditInfoForm auditInfoForm, AirCargoRejected airCargoRejected) {

        AirOrder tmp = new AirOrder();
        tmp.setId(airOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //根据选择是否订舱驳回
        Integer rejectOptions = airCargoRejected.getRejectOptions() == null ? 1 : airCargoRejected.getRejectOptions();
        ApiResult result = new ApiResult();
        //删除物流轨迹表订舱数据
        switch (rejectOptions) {
            case 1://订单驳回
                result = omsClient.deleteLogisticsTrackByType(airOrder.getId(), BusinessTypeEnum.KY.getCode());
                break;
            case 2:
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(airOrder.getId());
                form.setStatus(Collections.singletonList(OrderStatusEnum.AIR_A_2.getCode()));
                result = this.omsClient.delSpecOprStatus(form);
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订舱轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

//        AuditInfoForm auditInfoForm = new AuditInfoForm();
//        auditInfoForm.setExtId(airOrder.getId());
//        auditInfoForm.setExtDesc(SqlConstant.AIR_ORDER);
//        auditInfoForm.setAuditStatus((OrderStatusEnum.AIR_A_2_1.getCode()));
//        auditInfoForm.setAuditTypeDesc(OrderStatusEnum.AIR_A_2_1.getDesc());
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        //更改为驳回状态
        this.updateById(tmp);
        //订舱驳回推送l
        this.bookingRejectedMsgPush(airOrder, airCargoRejected);
    }


    /**
     * 订舱驳回推送
     */
    private boolean bookingRejectedMsgPush(AirOrder airOrder, AirCargoRejected airCargoRejected) {
        Map<String, Object> resultMap = null;
        switch (CreateUserTypeEnum.getEnum(airOrder.getCreateUserType())) {
            case VIVO:
                //判断当前订单状态所处位置,以订舱为分界线,订舱之后的驳回都是传2,订舱之前是1
//                Integer node = Integer.valueOf(OrderStatusEnum.AIR_A_2.getCode().split("_")[1]);
//                Integer status = Integer.parseInt(airOrder.getStatus().split("_")[1]) >= node ? 2 : 1;
                Integer status = airCargoRejected.getRejectOptions() == null ? VivoRejectionStatusEnum.PENDING_SUBMITTED.getCode()
                        : airCargoRejected.getRejectOptions();
                resultMap = this.vivoService.forwarderBookingRejected(airOrder.getThirdPartyOrderNo(), status);
                if (resultMap == null) {
                    log.warn("请求vivo订舱驳回操作失败,返回响应为空,请联系客服");
                    throw new VivoApiException("请求vivo订舱驳回操作失败,返回响应为空,请联系客服");
                }
                if (1 != MapUtil.getInt(resultMap, "status")) {
                    log.warn("请求vivo订舱驳回操作失败 message={}", MapUtil.getStr(resultMap, "message"));
                    throw new VivoApiException("请求vivo订舱驳回操作失败 message=" + MapUtil.getStr(resultMap, "message"));
                }
        }
        return true;
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

    /**
     * 根据主订单查询空运订单
     */
    @Override
    public AirOrder getByMainOrderNo(String mainOrderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getMainOrderNo, mainOrderNo);
        return this.getOne(condition);
    }

    /**
     * 根据空运订单号修改空运
     */
    @Override
    public boolean updateByOrderNo(String airOrderNo, AirOrder airOrder) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getOrderNo, airOrderNo);
        return this.update(airOrder, condition);
    }

    /**
     * 空运订单详情
     */
    @Override
    public AirOrderVO getAirOrderDetails(Long airOrderId) {
        Integer businessType = BusinessTypeEnum.KY.getCode();
        //空运订单信息
        AirOrderVO airOrder = this.baseMapper.getAirOrder(airOrderId);
        //查询商品信息
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusIds(Collections.singletonList(airOrderId), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 airOrderId={}", airOrderId);
        }
        airOrder.setGoodsVOs(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(airOrderId), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}", airOrderId);
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            airOrder.processingAddress(address);
        }
        //查询订舱信息
        AirBooking airBooking = this.airBookingService.getByAirOrderId(airOrderId);
        AirBookingVO airBookingVO = ConvertUtil.convert(airBooking, AirBookingVO.class);
        airOrder.setAirBookingVO(airBookingVO);
        return airOrder;
    }

    /**
     * 查询空运订单信息
     */
    @Override
    public List<AirOrder> getAirOrderInfo(AirOrder airOrder) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>(airOrder);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据空运订单号集合查询空运订单信息
     */
    @Override
    public List<AirOrder> getAirOrdersByOrderNos(List<String> airOrderNos) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().in(AirOrder::getOrderNo, airOrderNos);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 订单单驳回
     */
    @Override
    public void orderReceiving(AirOrder airOrder, AuditInfoForm auditInfoForm, AirCargoRejected airCargoRejected) {
        AirOrder tmp = new AirOrder();
        tmp.setId(airOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        this.bookingRejectedMsgPush(airOrder, airCargoRejected);
        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
    }

//    /**
//     * 接单驳回
//     */
//    @Override
//    public void rejectionOrderReceiving(AirOrder airOrder, AuditInfoForm auditInfoForm) {
////        this.bookingRejectedMsgPush(airOrder);
//        omsClient.saveAuditInfo(auditInfoForm);
//        this.updateById(airOrder);
//    }

//    public void verifyRejectionResponseError(Map<String, Object> resultMap) {
//
//        if (1 != MapUtil.getInt(resultMap, "status")) {
//            throw new VivoApiException(MapUtil.getStr(resultMap, "message"));
//        }
//    }
}
