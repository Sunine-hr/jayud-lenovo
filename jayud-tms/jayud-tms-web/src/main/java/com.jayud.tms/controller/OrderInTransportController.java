package com.jayud.tms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.AuditInfoForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.bo.SendCarForm;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTransport;
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


    /**
     * 该接口针对填操作人操作时间的操作
     * @param form
     * @return
     */
    @ApiOperation(value = "外部报关放行,确认接单")
    @PostMapping(value = "/oprOrderTransport")
    public CommonResult oprOrderTransport(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || form.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(form.getOperatorUser()) || StringUtil.isNullOrEmpty(form.getOperatorTime()) ||
                StringUtil.isNullOrEmpty(form.getCmd())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(getLoginUser());
        if(CommonConstant.OUT_CUSTOMS_RELEASE.equals(form.getCmd())){//外部报关放行
            //接单了就可以操作外部报关放行,通关前审核必须先操作外部报关放行,且该节点不再流程节点上显示
            orderTransport.setStatus(OrderStatusEnum.EXT_CUSTOMS_RELEASE.getCode());

            form.setStatus(OrderStatusEnum.EXT_CUSTOMS_RELEASE.getCode());
            form.setStatusName(OrderStatusEnum.EXT_CUSTOMS_RELEASE.getDesc());
        }else if(CommonConstant.COMFIRM_ORDER.equals(form.getCmd())){//确认接单
            orderTransport.setStatus(OrderStatusEnum.TMS_T_1.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_1.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_1.getDesc());
        }else if(CommonConstant.CAR_TAKE_GOODS.equals(form.getCmd())){//车辆提货
            orderTransport.setStatus(OrderStatusEnum.TMS_T_5.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_5.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_5.getDesc());
        }else if(CommonConstant.CAR_WEIGH.equals(form.getCmd())) {//车辆过磅
            if(form.getCarWeighNum() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(OrderStatusEnum.TMS_T_6.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_6.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_6.getDesc());
        }else if(CommonConstant.HK_CLEAR_CUSTOMS.equals(form.getCmd())) {//香港清关
            orderTransport.setStatus(OrderStatusEnum.HK_CLEAR_1.getCode());

            form.setStatus(OrderStatusEnum.HK_CLEAR_1.getCode());
            form.setStatusName(OrderStatusEnum.HK_CLEAR_1.getDesc());
        }else if(CommonConstant.CAR_ENTER_WAREHOUSE.equals(form.getCmd())){//车辆入仓
            orderTransport.setStatus(OrderStatusEnum.TMS_T_10.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_10.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_10.getDesc());
        }else if(CommonConstant.CAR_OUT_WAREHOUSE.equals(form.getCmd())){//车辆出仓
            orderTransport.setStatus(OrderStatusEnum.TMS_T_10.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_10.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_10.getDesc());
        }else if(CommonConstant.CAR_SEND.equals(form.getCmd())){//车辆派送
            orderTransport.setStatus(OrderStatusEnum.TMS_T_14.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_14.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_14.getDesc());
        }else if(CommonConstant.COMFIRM_SIGN_IN.equals(form.getCmd())){//确认签收
            orderTransport.setStatus(OrderStatusEnum.TMS_T_15.getCode());

            form.setStatus(OrderStatusEnum.TMS_T_15.getCode());
            form.setStatusName(OrderStatusEnum.TMS_T_15.getDesc());
        }
        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        omsClient.saveOprStatus(form);
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
    @ApiOperation(value = "通关前审核")
    @PostMapping(value = "/auditOrderTransport")
    public CommonResult auditOrderTransport(@RequestBody OprStatusForm form) {
        if (form.getOrderId() == null || form.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(form.getStatus()) || StringUtil.isNullOrEmpty(form.getCmd())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(getLoginUser());

        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditComment(form.getDescription());
        if (CommonConstant.GO_CUSTOMS_AUDIT.equals(form.getCmd())) {//通关前审核
            if (OrderStatusEnum.TMS_T_7.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_7_1.equals(form.getStatus())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(form.getStatus());
            if (OrderStatusEnum.TMS_T_7.getCode().equals(form.getStatus())) {
                //记录操作成功状态
                form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
                form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
                form.setStatusName(OrderStatusEnum.TMS_T_7.getDesc());
                omsClient.saveOprStatus(form);
            }
            auditInfoForm.setAuditTypeDesc(CommonConstant.GO_CUSTOMS_AUDIT_DESC);
        } else if (CommonConstant.GO_CUSTOMS_CHECK.equals(form.getCmd())) {//通关前复核
            if (OrderStatusEnum.TMS_T_8.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_8_1.equals(form.getStatus())) {
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
            if (OrderStatusEnum.TMS_T_9.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_9_1.equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_9_2.equals(form.getStatus())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderTransport.setStatus(form.getStatus());
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
        if(form.getIsHaveEncode() && StringUtil.isNullOrEmpty(form.getEncode())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderSendCars orderSendCars = ConvertUtil.convert(form,OrderSendCars.class);

        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(form.getOrderId());
        orderTransport.setUpdatedTime(LocalDateTime.now());
        orderTransport.setUpdatedUser(getLoginUser());

        OprStatusForm oprStatusForm = new OprStatusForm();
        oprStatusForm.setMainOrderId(form.getMainOrderId());
        oprStatusForm.setOrderId(form.getOrderId());

        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getId());
        if(CommonConstant.SEND_CAR.equals(form.getCmd()) || CommonConstant.EDIT_CAR.equals(form.getCmd())){
            //保存派车信息
            orderSendCars.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
            orderSendCars.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
            orderSendCars.setEncodeUrl(StringUtils.getFileStr(form.getEncodePics()));
            orderSendCars.setEncodeUrlName(StringUtils.getFileNameStr(form.getEncodePics()));
            orderSendCars.setStatus(OrderStatusEnum.TMS_T_2.getCode());
            if(CommonConstant.SEND_CAR.equals(form.getCmd())) {
                orderSendCars.setCreatedUser(getLoginUser());
            }else{
                orderSendCars.setUpdatedTime(LocalDateTime.now());
                orderSendCars.setUpdatedUser(getLoginUser());
            }
            //更新订单状态
            orderTransport.setStatus(OrderStatusEnum.TMS_T_2.getCode());

            //记录操作状态
            oprStatusForm.setStatus(OrderStatusEnum.TMS_T_2.getCode());
            oprStatusForm.setStatusName(OrderStatusEnum.TMS_T_2.getDesc());
            oprStatusForm.setDescription(form.getDescribes());

            //记录审核信息
            auditInfoForm.setAuditStatus(OrderStatusEnum.TMS_T_2.getCode());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_2.getDesc());
            auditInfoForm.setAuditComment(form.getDescribes());


        }else if(CommonConstant.AUDIT_CAR.equals(form.getCmd())){//运输审核
            if(!(OrderStatusEnum.TMS_T_3.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.TMS_T_3_1.getCode().equals(form.getStatus()))){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            //更新派车信息
            orderSendCars.setStatus(form.getStatus());
            orderSendCars.setUpdatedTime(LocalDateTime.now());
            orderSendCars.setUpdatedUser(getLoginUser());
            //更新订单状态
            orderTransport.setStatus(form.getStatus());
            //记录操作状态
            if(OrderStatusEnum.TMS_T_3.getCode().equals(form.getStatus())) {
                oprStatusForm.setStatus(OrderStatusEnum.TMS_T_3.getCode());
                oprStatusForm.setStatusName(OrderStatusEnum.TMS_T_3.getDesc());
                oprStatusForm.setDescription(form.getAuditComment());
            }
            //记录审核信息
            auditInfoForm.setAuditStatus(form.getStatus());
            auditInfoForm.setAuditTypeDesc(OrderStatusEnum.TMS_T_3.getDesc());
            auditInfoForm.setAuditComment(form.getAuditComment());
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
    public CommonResult<OrderSendCars> getOrderSendCars(@RequestBody  Map<String,Object> param){
        String orderNo = MapUtil.getStr(param,SqlConstant.ORDER_NO);
        if(StringUtil.isNullOrEmpty(orderNo)){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_NO,orderNo);
        OrderSendCars orderSendCars = orderSendCarsService.getOne(queryWrapper);
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

    @ApiOperation(value = "报关接单列表/放行异常列表/放行确认/审核不通过/订单列表/报关打单/复核/申报")
    @PostMapping("/findTransportOrderByPage")
    public CommonResult<CommonPageResult<OrderTransportVO>> findTransportOrderByPage(@RequestBody QueryOrderTmsForm form) {
        IPage<OrderTransportVO> pageList = orderTransportService.findTransportOrderByPage(form);
        CommonPageResult<OrderTransportVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


    /**
     * 当前登录用户
     * @return
     */
    private String getLoginUser(){
        return redisUtils.get("loginUser",100);
    }


}

