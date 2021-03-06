package com.jayud.airfreight.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.feign.MsgClient;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.mapper.AirOrderMapper;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.enums.AirBookingStatusEnum;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirExceptionFeedback;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.*;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.IAirExceptionFeedbackService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author ?????????
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
    @Autowired
    private IAirExceptionFeedbackService airExceptionFeedbackService;

    @Autowired
    private OauthClient oauthClient;

    //????????????
    @Override
    @Transactional
    public void createOrder(AddAirOrderForm addAirOrderForm) {

        LocalDateTime now = LocalDateTime.now();
        AirOrder airOrder = ConvertUtil.convert(addAirOrderForm, AirOrder.class);
        //???????????????
        if (addAirOrderForm.getId() == null) {
            //???????????????
            //String orderNo = generationOrderNo();
            airOrder.setCreateTime(now)
                    .setCreateUser(UserOperator.getToken())
                    .setStatus(OrderStatusEnum.AIR_A_0.getCode());
            this.save(airOrder);
        } else {
            //???????????????
            airOrder.setStatus(OrderStatusEnum.AIR_A_0.getCode())
                    .setUpdateTime(now).setUpdateUser(UserOperator.getToken());
            this.updateById(airOrder);
        }

        List<AddOrderAddressForm> orderAddressForms = addAirOrderForm.getOrderAddressForms();
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            orderAddressForm.setOrderNo(airOrder.getOrderNo());
            orderAddressForm.setBusinessType(BusinessTypeEnum.KY.getCode());
            orderAddressForm.setBusinessId(airOrder.getId());
            orderAddressForm.setCreateTime(LocalDateTime.now());
        }
        //????????????????????????
        ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("????????????/??????????????????????????????,??????????????????={}", new JSONArray(orderAddressForms));
        }

        List<AddGoodsForm> goodsForms = addAirOrderForm.getGoodsForms();
        for (AddGoodsForm goodsForm : goodsForms) {
            goodsForm.setOrderNo(airOrder.getOrderNo());
            goodsForm.setBusinessId(airOrder.getId());
            goodsForm.setBusinessType(BusinessTypeEnum.KY.getCode());
        }
        //????????????????????????
        result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("????????????/????????????????????????,????????????={}", new JSONArray(goodsForms));
        }

    }

    /**
     * ???????????????
     */
    @Override
    public String generationOrderNo() {
        //???????????????
        String orderNo = StringUtils.loadNum(CommonConstant.A, 12);
        while (true) {
            if (isExistOrder(orderNo)) {//??????
                orderNo = StringUtils.loadNum(CommonConstant.A, 12);
            } else {
                break;
            }
        }
        return orderNo;
    }

    /**
     * ??????????????????
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    @Override
    public IPage<AirOrderFormVO> findByPage(QueryAirOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //????????????
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //????????????????????????????????????
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<AirOrder> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form, legalIds);
    }


    /**
     * ??????????????????
     */
    @Transactional
    @Override
    public void updateProcessStatus(AirOrder airOrder, AirProcessOptForm form) {
        airOrder.setId(form.getOrderId());
        airOrder.setUpdateTime(LocalDateTime.now());
        airOrder.setUpdateUser(UserOperator.getToken());
        airOrder.setStatus(form.getStatus());

        //????????????????????????
        this.baseMapper.updateById(airOrder);
        //??????????????????
        this.airProcessOptRecord(form);
        //??????????????????
        finishAirOrderOpt(airOrder);
    }

    /**
     * ????????????????????????
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

        //????????????
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.KY.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("??????????????????????????????");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("??????????????????????????????");
        }

    }

    /**
     * ????????????
     */
    @Override
    @Transactional
    public void doAirBookingOpt(AirProcessOptForm form) {
        AddAirBookingForm airBookingForm = form.getAirBooking();
        //????????????????????????,?????????????????????
        AirBooking oldAirBooking = this.airBookingService.getEnableByAirOrderId(form.getOrderId());
        airBookingForm.setId(oldAirBooking != null ? oldAirBooking.getId() : null);

        AirBooking airBooking = ConvertUtil.convert(airBookingForm, AirBooking.class);
        //??????????????????
        this.handleLadingBillFile(airBooking, form);
        //??????????????????
        setAirBookingStatus(airBooking);

        airBookingService.saveOrUpdateAirBooking(airBooking);
        updateProcessStatus(new AirOrder(), form);
        //????????????
        this.messagePush(airBooking.setAirOrderId(form.getOrderId()), form);
    }


    private void messagePush(AirBooking airBooking, AirProcessOptForm form) {
        //????????????
        this.bookingMessagePush(airBooking, form);
        //??????????????????
        this.billLadingInfoPush(form);
    }

    /**
     * ???????????????
     *
     * @param airOrder
     * @return
     */
    @Override
    public boolean isWarehousing(AirOrder airOrder) {
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            //??????????????????
            AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrder.getId());
            if ("1".equals(airBooking.getStatus())) {
                return false;
            }
        }

        return true;
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Override
    public AirOrder getByThirdPartyOrderNo(String thirdPartyOrderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getThirdPartyOrderNo, thirdPartyOrderNo);
        return this.getOne(condition);
    }

    /**
     * ????????????
     */
    @Override
    @Transactional
    public void rejectedOpt(AirOrder airOrder, AuditInfoForm auditInfoForm, AirCargoRejected airCargoRejected) {

        AirOrder tmp = new AirOrder();
        tmp.setId(airOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //??????????????????????????????
        ApiResult result = new ApiResult();
        //?????????????????????????????????
        switch (airCargoRejected.getRejectOptions()) {
            case 1://????????????
                result = omsClient.deleteLogisticsTrackByType(airOrder.getId(), BusinessTypeEnum.KY.getCode());
                //??????????????????
                this.airBookingService.updateByAirOrderId(airOrder.getId(),
                        new AirBooking().setStatus(AirBookingStatusEnum.DELETE.getCode()));

                //???????????????????????????
                omsClient.doMainOrderRejectionSignOpt(airOrder.getMainOrderNo(),
                        airOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
                break;
            case 2://????????????
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(airOrder.getId());
                form.setStatus(Collections.singletonList(OrderStatusEnum.AIR_A_2.getCode()));
                result = this.omsClient.delSpecOprStatus(form);
                tmp.setStatus(OrderStatusEnum.AIR_A_3_2.getCode());
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("????????????????????????????????????");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("??????????????????????????????");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        //?????????????????????
        this.updateById(tmp);
        //??????????????????
        this.bookingRejectedMsgPush(airOrder, airCargoRejected);
    }


    /**
     * ??????????????????
     */
    private boolean bookingRejectedMsgPush(AirOrder airOrder, AirCargoRejected airCargoRejected) {
        Map<String, Object> resultMap = null;
        switch (CreateUserTypeEnum.getEnum(airOrder.getCreateUserType())) {
            case VIVO:
                this.vivoService.bookingRejected(airOrder, airCargoRejected);
        }
        return true;
    }


    private void setAirBookingStatus(AirBooking airBooking) {
        if (airBooking.getId() != null) {
            return;
        }
        airBooking.setStatus(0);
//        AirOrder airOrder = this.getById(airBooking.getAirOrderId());
//        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
//            airBooking.setStatus("1");
//        } else {
//            airBooking.setStatus("0");
//        }
    }

    private void finishAirOrderOpt(AirOrder airOrder) {
        if (OrderStatusEnum.AIR_A_6.getCode().equals(airOrder.getStatus())) {
            //????????????????????????
            AirOrder tmp = this.getById(airOrder.getId());
            if (AirOrderTermsEnum.CIF.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.FOB.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.CFR.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.CPT.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.CNF.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.CIP.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.FCA.getCode().equals(tmp.getTerms())
                    || AirOrderTermsEnum.EXW.getCode().equals(tmp.getTerms())) {
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
     * ??????????????????
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
     * ??????????????????
     */
    private void billLadingInfoPush(AirProcessOptForm form) {
        if (!OrderStatusEnum.AIR_A_4.getCode().equals(form.getStatus())) {
            return;
        }
        AirOrder airOrder = this.getById(form.getOrderId());
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrder.getId());
            this.vivoService.billLadingInfoPush(airOrder, airBooking);
        }
    }


    /**
     * ????????????
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
     * ?????????????????????????????????
     */
    @Override
    public AirOrder getByMainOrderNo(String mainOrderNo) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getMainOrderNo, mainOrderNo);
        return this.getOne(condition);
    }

    /**
     * ?????????????????????????????????
     */
    @Override
    public boolean updateByOrderNo(String airOrderNo, AirOrder airOrder) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(AirOrder::getOrderNo, airOrderNo);
        return this.update(airOrder, condition);
    }

    /**
     * ??????????????????
     */
    @Override
    public AirOrderVO getAirOrderDetails(Long airOrderId) {
        Integer businessType = BusinessTypeEnum.KY.getCode();
        //??????????????????
        AirOrderVO airOrder = this.baseMapper.getAirOrder(airOrderId);
        //??????????????????
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusIds(Collections.singletonList(airOrderId), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????? airOrderId={}", airOrderId);
        }
        airOrder.setGoodsForms(result.getData());
        //??????????????????
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(airOrderId), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????? airOrderId={}", airOrderId);
        }
        //??????????????????
        for (OrderAddressVO address : resultOne.getData()) {
            airOrder.processingAddress(address);
        }
        //??????????????????
        AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrderId);
        AirBookingVO airBookingVO = ConvertUtil.convert(airBooking, AirBookingVO.class);
        airOrder.setAirBookingVO(airBookingVO);
        return airOrder;
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<AirOrder> getAirOrderInfo(AirOrder airOrder) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>(airOrder);
        return this.baseMapper.selectList(condition);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Override
    public List<AirOrder> getAirOrdersByOrderNos(List<String> airOrderNos) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().in(AirOrder::getOrderNo, airOrderNos);
        return this.baseMapper.selectList(condition);
    }

    /**
     * ???????????????
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

        //???????????????????????????
        omsClient.doMainOrderRejectionSignOpt(airOrder.getMainOrderNo(),
                airOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
        this.updateById(tmp);
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Override
    public Map<String, Object> getMainOrderByThirdOrderNo(String thirdPartyOrderNo) {
        AirOrder airOrder = this.getByThirdPartyOrderNo(thirdPartyOrderNo);
        if (airOrder == null) {
            return null;
        }
        ApiResult result = this.omsClient.getMainOrderByOrderNos(Collections.singletonList(airOrder.getMainOrderNo()));
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("??????????????????????????? mainOrderNo={}", airOrder.getMainOrderNo());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONArray objects = new JSONArray(result.getData());
        return JSONUtil.toBean(objects.getJSONObject(0), Map.class);
    }


    /**
     * ????????????
     */
    @Override
    @Transactional
    public void exceptionFeedback(AddAirExceptionFeedbackForm form) {
        AirExceptionFeedback airExceptionFeedback = ConvertUtil.convert(form, AirExceptionFeedback.class);
        airExceptionFeedback.setFileName(StringUtils.getFileNameStr(form.getFileViewList()))
                .setFilePath(StringUtils.getFileStr(form.getFileViewList()))
                .setCreateUser(UserOperator.getToken())
                .setCreateTime(LocalDateTime.now());
        this.airExceptionFeedbackService.saveOrUpdate(airExceptionFeedback);
        //??????????????????
        this.pushExceptionFeedbackInfo(form, airExceptionFeedback);
    }

    /**
     * ??????????????????????????????????????????
     */
    @Override
    public List<AirOrderInfoVO> getAirOrderByMainOrderNos(List<String> mainOrderNos) {
        return this.baseMapper.getByMainOrderNo(mainOrderNos);
//        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
//        condition.lambda().in(AirOrder::getMainOrderNo, mainOrderNos);
//        return this.baseMapper.selectList(condition);
    }


    /**
     * ?????????????????????????????????
     */
    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        Integer num = 0;
        switch (status) {
            case "airFeeCheck":
                List<AirOrder> list = this.getByLegalEntityId(legalIds);
                if (CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(AirOrder::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.KY.getSignOne(), legalIds, orderNos).getData();
                break;
            default:
                num = this.baseMapper.getNumByStatus(status, legalIds);
        }

        return num == null ? 0 : num;
    }

    @Override
    public List<AirOrder> getByLegalEntityId(List<Long> legalIds) {
        QueryWrapper<AirOrder> condition = new QueryWrapper<>();
        condition.lambda().in(AirOrder::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }


    private void pushExceptionFeedbackInfo(AddAirExceptionFeedbackForm form, AirExceptionFeedback airExceptionFeedback) {
        AirOrder airOrder = this.getById(airExceptionFeedback.getOrderId());
        if (CreateUserTypeEnum.VIVO.getCode().equals(airOrder.getCreateUserType())) {
            if (OrderStatusEnum.AIR_A_4.getCode().equals(airOrder.getStatus())
                    || OrderStatusEnum.AIR_A_5.getCode().equals(airOrder.getStatus())
                    || OrderStatusEnum.AIR_A_6.getCode().equals(airOrder.getStatus())
                    || OrderStatusEnum.AIR_A_7.getCode().equals(airOrder.getStatus())
                    || OrderStatusEnum.AIR_A_8.getCode().equals(airOrder.getStatus())) {
                this.vivoService.pushExceptionFeedbackInfo(airOrder, form, airExceptionFeedback);
            } else {
                throw new JayudBizException("??????????????????????????????????????????????????????");
            }

        }
    }


//    /**
//     * ????????????
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
