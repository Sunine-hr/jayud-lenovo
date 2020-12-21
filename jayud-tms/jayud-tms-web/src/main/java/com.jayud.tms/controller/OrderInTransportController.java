package com.jayud.tms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.feign.FreightAirApiClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.*;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import com.jayud.tms.model.vo.OrderStatusVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTransportService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/orderTransport")
@Api(tags = "中港订单接口")
public class OrderInTransportController {

    @Autowired
    IOrderTransportService orderTransportService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    OmsClient omsClient;

    @Autowired
    IOrderSendCarsService orderSendCarsService;
    @Autowired
    private FreightAirApiClient freightAirApiClient;


    /**
     * 该接口针对填操作人操作时间的操作
     * @param form
     * @return
     */
    @ApiOperation(value = "确认接单/车辆提货/车辆过磅/香港清关/车辆入仓/车辆出仓/车辆派送/确认签收")
    @PostMapping(value = "/oprOrderTransport")
    public CommonResult oprOrderTransport(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || form.getMainOrderId() == null ||
                (StringUtil.isNullOrEmpty(form.getOperatorUser()) && !CommonConstant.HK_CLEAR_CUSTOMS.equals(form.getCmd())) ||
                StringUtil.isNullOrEmpty(form.getCmd())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        form.setBusinessType(BusinessTypeEnum.ZGYS.getCode());
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

        if(CommonConstant.COMFIRM_ORDER.equals(form.getCmd())){//确认接单
            orderTransport.setStatus(OrderStatusEnum.TMS_T_1.getCode());
            orderTransport.setJiedanTime(LocalDateTime.now());
            orderTransport.setJiedanUser(form.getOperatorUser());

            form.setStatus(OrderStatusEnum.TMS_T_1.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_1.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_1.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_1.getDesc());
        }else if(CommonConstant.CAR_TAKE_GOODS.equals(form.getCmd())){//车辆提货
            orderTransport.setStatus(OrderStatusEnum.TMS_T_5.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_5.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_5.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_5.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_5.getDesc());
        }else if(CommonConstant.CAR_WEIGH.equals(form.getCmd())) {//车辆过磅
            if(form.getCarWeighNum() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setCarWeighNum(form.getCarWeighNum());
            orderTransport.setStatus(OrderStatusEnum.TMS_T_6.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_6.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_6.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_6.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_6.getDesc());
        }else if(CommonConstant.HK_CLEAR_CUSTOMS.equals(form.getCmd())) {//香港清关
            //参数校验
            if(form.getDriverInfoId() == null || StringUtil.isNullOrEmpty(form.getSeamlessNo()) ||
               StringUtil.isNullOrEmpty(form.getClearCustomsNo()) || form.getVehicleId() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setDriverInfoId(form.getDriverInfoId());
            orderTransport.setVehicleId(form.getVehicleId());
            orderTransport.setSeamlessNo(form.getSeamlessNo());
            orderTransport.setClearCustomsNo(form.getClearCustomsNo());//清关完成标识,有数据表示清关完成

            form.setStatus(OrderStatusEnum.HK_CLEAR_1.getCode());
            form.setStatusName(OrderStatusEnum.HK_CLEAR_1.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.HK_CLEAR_1.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.HK_CLEAR_1.getDesc());
        }else if(CommonConstant.CAR_ENTER_WAREHOUSE.equals(form.getCmd())){//车辆入仓
            orderTransport.setStatus(OrderStatusEnum.TMS_T_10.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_10.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_10.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_10.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_10.getDesc());
        }else if(CommonConstant.CAR_OUT_WAREHOUSE.equals(form.getCmd())){//车辆出仓
            orderTransport.setStatus(OrderStatusEnum.TMS_T_13.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_13.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_13.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_13.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_13.getDesc());

            //车辆出仓后:中转仓卸货装货已完成
            OprStatusForm tms11 = new OprStatusForm();
            tms11.setMainOrderId(form.getMainOrderId());
            tms11.setOrderId(form.getOrderId());
            tms11.setStatus(OrderStatusEnum.TMS_T_11.getCode());
            tms11.setStatusName(OrderStatusEnum.TMS_T_11.getDesc());
            tms11.setBusinessType(BusinessTypeEnum.ZGYS.getCode());
            omsClient.saveOprStatus(tms11);
            OprStatusForm tms12 = new OprStatusForm();
            tms12.setMainOrderId(form.getMainOrderId());
            tms12.setOrderId(form.getOrderId());
            tms12.setStatus(OrderStatusEnum.TMS_T_12.getCode());
            tms12.setStatusName(OrderStatusEnum.TMS_T_12.getDesc());
            tms11.setBusinessType(BusinessTypeEnum.ZGYS.getCode());
            omsClient.saveOprStatus(tms12);
        }else if(CommonConstant.CAR_SEND.equals(form.getCmd())){//车辆派送
            orderTransport.setStatus(OrderStatusEnum.TMS_T_14.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_14.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_14.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_14.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_14.getDesc());
        }else if(CommonConstant.CONFIRM_SIGN_IN.equals(form.getCmd())){//确认签收
            orderTransport.setStatus(OrderStatusEnum.TMS_T_15.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_15.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_15.getDesc());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_15.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_15.getDesc());
        }
        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        omsClient.saveOprStatus(form);
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(),ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


    /**
     * 该接口针对填各种审核状态的操作
     * @param form
     * @return
     */
    @ApiOperation(value = "通关前审核/通关前复核/车辆通关")
    @PostMapping(value = "/auditOrderTransport")
    public CommonResult auditOrderTransport(@RequestBody OprStatusForm form) {
        if (form.getMainOrderId() == null || StringUtil.isNullOrEmpty(form.getStatus()) ||
                StringUtil.isNullOrEmpty(form.getCmd()) ||
                (!CommonConstant.GO_CUSTOMS_AUDIT.equals(form.getCmd()) && form.getOrderId() == null)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(UserOperator.getToken());

        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_TRANSPORT);

        if (CommonConstant.GO_CUSTOMS_AUDIT.equals(form.getCmd())) {//通关前审核：操作是在主订单处，但是是针对主订单下的中港子订单
            if (!(OrderStatusEnum.TMS_T_7.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_7_1.getCode().equals(form.getStatus())) ||
                    StringUtil.isNullOrEmpty(form.getMainOrderNo())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            //根据主订单获取中港子订单号
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq(SqlConstant.MAIN_ORDER_NO,form.getMainOrderNo());
            OrderTransport transport = orderTransportService.getOne(queryWrapper);
            if(transport == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(form.getStatus());
            orderTransport.setId(transport.getId());
            if (OrderStatusEnum.TMS_T_7.getCode().equals(form.getStatus())) {
                //记录操作成功状态
                form.setOrderId(transport.getId());
                form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
                form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
                form.setStatusName(OrderStatusEnum.TMS_T_7.getDesc());
                omsClient.saveOprStatus(form);
            }
            auditInfoForm.setExtId(transport.getId());
            auditInfoForm.setAuditTypeDesc(CommonConstant.GO_CUSTOMS_AUDIT_DESC);
        } else if (CommonConstant.GO_CUSTOMS_CHECK.equals(form.getCmd())) {//通关前复核
            if (!(OrderStatusEnum.TMS_T_8.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_8_1.equals(form.getStatus()))) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(form.getStatus());
            if (OrderStatusEnum.TMS_T_8.getCode().equals(form.getStatus())) {
                //记录操作成功状态
                form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
                form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
                form.setStatusName(OrderStatusEnum.TMS_T_8.getDesc());
                omsClient.saveOprStatus(form);
            }
            auditInfoForm.setAuditTypeDesc(CommonConstant.GO_CUSTOMS_CHECK_DESC);
        }else if(CommonConstant.CAR_GO_CUSTOMS.equals(form.getCmd())){//车辆通关
            if (!(OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_9_1.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_9_2.getCode().equals(form.getStatus()))) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(form.getStatus());
            orderTransport.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));

            if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus())) {
                //记录操作成功状态
                form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
                form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
                form.setStatusName(OrderStatusEnum.TMS_T_9.getDesc());
                omsClient.saveOprStatus(form);
            }
            auditInfoForm.setAuditTypeDesc(CommonConstant.CAR_GO_CUSTOMS_DESC);
        }
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


    /**
     * 运输派车
     * @param form
     * @return
     */
    @ApiOperation(value = "运输派车")
    @PostMapping(value = "/transportSendCar")
    public CommonResult transportSendCar(@RequestBody @Valid SendCarForm form) {
        if(StringUtil.isNullOrEmpty(form.getCmd())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderSendCars orderSendCars = ConvertUtil.convert(form,OrderSendCars.class);
        //只有柜车才有柜号
        if(form.getVehicleType() == 1){//吨车
            form.setCntrNo(null);
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(UserOperator.getToken());

        OprStatusForm oprStatusForm = new OprStatusForm();
        oprStatusForm.setMainOrderId(form.getMainOrderId());
        oprStatusForm.setOrderId(form.getOrderId());
        oprStatusForm.setBusinessType(BusinessTypeEnum.ZGYS.getCode());

        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        if(CommonConstant.SEND_CAR.equals(form.getCmd()) || CommonConstant.EDIT_CAR.equals(form.getCmd())){
            //参数校验
            if((CommonConstant.EDIT_CAR.equals(form.getCmd()) && form.getId() == null) ||
              form.getOrderId() == null || form.getMainOrderId() == null ||
              StringUtil.isNullOrEmpty(form.getTransportNo()) || StringUtil.isNullOrEmpty(form.getOrderNo()) ||
              form.getVehicleSize() == null || form.getVehicleType() == null ||
              form.getVehicleId() == null || form.getDriverInfoId() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            //当运输派车后在驳回时,重新编辑,再次走流程时会出现两条派车记录,原来那条作废
            if(CommonConstant.SEND_CAR.equals(form.getCmd())) {
                QueryWrapper removeWrapper = new QueryWrapper();
                removeWrapper.eq("order_no", form.getOrderNo());
                orderSendCarsService.remove(removeWrapper);
            }
            //保存派车信息
            orderSendCars.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
            orderSendCars.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
            orderSendCars.setStatus(OrderStatusEnum.TMS_T_2.getCode());
            if(CommonConstant.SEND_CAR.equals(form.getCmd())) {
                orderSendCars.setCreatedUser(UserOperator.getToken());
            }else{
                orderSendCars.setUpdatedTime(LocalDateTime.now());
                orderSendCars.setUpdatedUser(UserOperator.getToken());
            }
            //更新订单状态
            orderTransport.setVehicleSize(form.getVehicleSize());
            orderTransport.setVehicleType(form.getVehicleType());
            orderTransport.setStatus(OrderStatusEnum.TMS_T_2.getCode());

            //记录操作状态
            oprStatusForm.setStatus(OrderStatusEnum.TMS_T_2.getCode());
            oprStatusForm.setStatusName(OrderStatusEnum.TMS_T_2.getDesc());
            oprStatusForm.setDescription(form.getDescribes());

            //记录审核信息
            auditInfoForm.setExtDesc(SqlConstant.ORDER_TRANSPORT);
            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_2.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_2.getDesc());
            auditInfoForm.setAuditComment(form.getDescribes());


        }else if(CommonConstant.AUDIT_CAR.equals(form.getCmd())){//运输审核
            if(!(OrderStatusEnum.TMS_T_3.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_3_1.getCode().equals(form.getStatus())) || form.getId() == null ||
                    form.getOrderId() == null || form.getMainOrderId() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            //更新派车信息
            orderSendCars.setStatus(form.getStatus());
            orderSendCars.setUpdatedTime(LocalDateTime.now());
            orderSendCars.setUpdatedUser(UserOperator.getToken());
            //更新订单状态
            orderTransport.setStatus(form.getStatus());
            //记录操作状态
            if(OrderStatusEnum.TMS_T_3.getCode().equals(form.getStatus())) {
                oprStatusForm.setStatus(OrderStatusEnum.TMS_T_3.getCode());
                oprStatusForm.setStatusName(OrderStatusEnum.TMS_T_3.getDesc());
                oprStatusForm.setDescription(form.getAuditComment());
            }
            //记录审核信息
            auditInfoForm.setExtId(form.getId());
            auditInfoForm.setExtDesc(SqlConstant.ORDER_SEND_CARS);
            auditInfoForm.setAuditStatus(form.getStatus());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_3.getDesc());
            auditInfoForm.setAuditComment(form.getAuditComment());

            //审核通过推送派车信息
            if (OrderStatusEnum.TMS_T_3.getCode().equals(form.getStatus())) {
                this.orderSendCarsService.sendCarsMessagePush(form);
            }

        }
        orderSendCarsService.saveOrUpdate(orderSendCars);
        if(!OrderStatusEnum.TMS_T_3_1.getCode().equals(form.getStatus()) ||
                CommonConstant.EDIT_CAR.equals(form.getCmd())) {
            omsClient.saveOprStatus(oprStatusForm);
        }
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(),ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }

    /**
     * 运输审核信息回显
     */
    @ApiOperation(value = "运输审核信息回显,参数=order_no=中港子订单号")
    @PostMapping(value = "/getOrderSendCars")
    public CommonResult<OrderSendCarsVO> getOrderSendCars(@RequestBody  Map<String,Object> param){
        String orderNo = MapUtil.getStr(param,SqlConstant.ORDER_NO);
        if(StringUtil.isNullOrEmpty(orderNo)){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderSendCarsVO orderSendCars = orderSendCarsService.getOrderSendInfo(orderNo);
        if(orderSendCars == null){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        return CommonResult.success(orderSendCars);
    }


    /**
     * 生成派车单号
     */
    @ApiOperation(value = "生成派车单号")
    @PostMapping(value = "/createTransportNo")
    public CommonResult createTransportNo(){
        String transportNo = StringUtils.loadNum(CommonConstant.P,12);
        while (true){
            if(!isExistTransportNo(transportNo)){//重复
                transportNo = StringUtils.loadNum(CommonConstant.P,12);
            }else {
                break;
            }
        }
        return CommonResult.success(transportNo);
    }



    /**
     * 派车单号是否存在
     * @param transportNo
     * @return
     */
    private boolean isExistTransportNo(String transportNo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.TRANSPORT_NO,transportNo);
        List<OrderSendCars> orderSendCars = orderSendCarsService.list(queryWrapper);
        if(orderSendCars == null || orderSendCars.size() == 0) {
            return true;
        }
        return false;
    }

    @ApiOperation(value = "运输接单cmd=T_1/运输派车T_2/运输审核T_3/驳回重新调度T_3_1/确认派车T_4" +
            "/车辆提货T_5/车辆过磅T_6/通关前复核T_8/车辆通关T_9" +
            "/香港清关clearCustoms/车辆入仓T_10/车辆出仓T_13/车辆派送T_14/确认签收T_15/订单列表list/费用审核costAudit")
    @PostMapping("/findTransportOrderByPage")
    public CommonResult<CommonPageResult<OrderTransportVO>> findTransportOrderByPage(@RequestBody QueryOrderTmsForm form) {
        IPage<OrderTransportVO> pageList = orderTransportService.findTransportOrderByPage(form);
        CommonPageResult<OrderTransportVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    /**
     * 该接口针对填各种驳回
     * @param form
     * @return
     */
    @ApiOperation(value = "确认接单驳回cmd=T_1_1,确认派车驳回cmd=T_2_1,运输审核驳回cmd=T_3_2,确认派车驳回cmd=T_4_1")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody RejectOrderForm form) {
        if (form.getOrderId() == null || StringUtil.isNullOrEmpty(form.getCmd())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(UserOperator.getToken());

        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_TRANSPORT);
        Integer rejectOptions = form.getRejectOptions() == null ? 1 : form.getRejectOptions();
        //删除操作流程记录
        OrderTransport orderTransport1 = orderTransportService.getById(form.getOrderId());
        if(orderTransport1 == null){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<String> deleteStatus = new ArrayList<>();
        if (OrderStatusEnum.TMS_T_1_1.getCode().equals(form.getCmd())) {//确认接单驳回
            orderTransport.setStatus(OrderStatusEnum.TMS_T_1_1.getCode());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_1_1.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_1_1.getDesc());
        } else if (OrderStatusEnum.TMS_T_2_1.getCode().equals(form.getCmd())) {//确认派车驳回
            orderTransport.setStatus(OrderStatusEnum.TMS_T_2_1.getCode());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_2_1.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_2_1.getDesc());
        }else if(OrderStatusEnum.TMS_T_3_2.getCode().equals(form.getCmd())){//运输审核驳回
            orderTransport.setStatus(OrderStatusEnum.TMS_T_3_2.getCode());

            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_3_2.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_3_2.getDesc());
        } else if (OrderStatusEnum.TMS_T_4_1.getCode().equals(form.getCmd())) {//确认派车驳回
            String cmd = form.getCmd();
            //TODO 驳回推送需要做的
            if (rejectOptions == 2) {//派车驳回
                cmd = OrderStatusEnum.TMS_T_3_1.getCode();
                deleteStatus.add(OrderStatusEnum.TMS_T_3.getCode());
                //推送派车驳回消息
                if (this.orderSendCarsService.dispatchRejectionMsgPush(orderTransport1)) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
            }
            orderTransport.setStatus(cmd);
            auditInfoForm.setAuditStatus(cmd);
            auditInfoForm.setAuditTypeDesc(cmd);
        }
        omsClient.saveAuditInfo(auditInfoForm);
        if (rejectOptions == 1) { //驳回到订单编辑
            //删除这个订单下所有物流轨迹,重新走流程
            this.omsClient.deleteLogisticsTrackByType(form.getOrderId(), BusinessTypeEnum.ZGYS.getCode());
            //删除派车信息
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.eq("order_no", orderTransport1.getOrderNo());
            orderSendCarsService.remove(removeWrapper);
        } else {
            DelOprStatusForm deleteOpr = new DelOprStatusForm();
            deleteOpr.setOrderId(form.getOrderId());
            deleteOpr.setStatus(deleteStatus);
            //删除特定流程
            this.omsClient.delSpecOprStatus(deleteOpr);
        }

        boolean result = orderTransportService.updateById(orderTransport);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "子订单处理流程")
    @PostMapping(value = "/handleSubProcess")
    public CommonResult handleSubProcess(@RequestBody @Valid HandleSubProcessForm form) {
        List<OrderStatusVO> orderStatusVOS = omsClient.handleSubProcess(form).getData();
        return CommonResult.success(orderStatusVOS);
    }

    @ApiOperation(value = "确认派车,mainOrderId/orderId需传参数")
    @PostMapping(value = "/confirmSendCar")
    public CommonResult confirmSendCar(@RequestBody OprStatusForm form) {
        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(UserOperator.getToken());
        orderTransport.setStatus(OrderStatusEnum.TMS_T_4.getCode());

        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_4.getCode());
        auditInfoForm.setAuditComment(OrderStatusEnum.TMS_T_4.getDesc());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_TRANSPORT);
        auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_4.getDesc());

        form.setStatusName(OrderStatusEnum.TMS_T_4.getDesc());
        form.setStatus(OrderStatusEnum.TMS_T_4.getCode());
        form.setBusinessType(BusinessTypeEnum.ZGYS.getCode());

        omsClient.saveOprStatus(form);
        omsClient.saveAuditInfo(auditInfoForm);
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


}

