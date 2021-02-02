package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderOprCmdEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
import com.jayud.customs.model.vo.OrderStatusVO;
import com.jayud.customs.service.IOrderCustomsService;
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
@RequestMapping("/orderCustoms")
@Api(tags = "纯报关接口")
public class OrderCustomsController {

    @Autowired
    IOrderCustomsService orderCustomsService;

    @Autowired
    OmsClient omsClient;

    @ApiOperation(value = "确认:生成报关子订单号,num=生成报关单数")
    @PostMapping(value = "/createOrderNo")
    public CommonResult<List<InputSubOrderCustomsForm>> createOrderNo(@RequestBody Map<String, Object> param) {
        List<InputSubOrderCustomsForm> stringList = new ArrayList<>();
        int num = Integer.valueOf(MapUtil.getStr(param, "num"));
        String code = "BG";//报关类型
        for (int i = 0; i < num; i++) {//产品类别code+随机数
            String result = StringUtils.loadNum(code, 12);
            //校验子订单号是否存在,false=存在
            boolean isExist = orderCustomsService.isExistOrder(result);
            if (!isExist) {
                i = i - 1;
            }
            InputSubOrderCustomsForm inputSubOrderCustomsForm = new InputSubOrderCustomsForm();
            inputSubOrderCustomsForm.setOrderNo(result);
            stringList.add(inputSubOrderCustomsForm);
        }
        return CommonResult.success(stringList);
    }

    @ApiOperation(value = "报关接单列表/放行异常列表/放行确认/审核不通过/订单列表/报关打单/复核/申报")
    @PostMapping("/findCustomsOrderByPage")
    public CommonResult<CommonPageResult<CustomsOrderInfoVO>> findCustomsOrderByPage(@RequestBody QueryCustomsOrderInfoForm form) {
        IPage<CustomsOrderInfoVO> pageList = orderCustomsService.findCustomsOrderByPage(form);
        CommonPageResult<CustomsOrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


    @ApiOperation(value = "报关接单")
    @PostMapping(value = "/confirmOrder")
    public CommonResult confirmOrder(@RequestBody OprStatusForm form) {
        if (form.getOrderId() == null || form.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(form.getOperatorUser())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_1.getCode());
        orderCustoms.setJiedanTime(LocalDateTime.now());
        orderCustoms.setJiedanUser(form.getOperatorUser());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(UserOperator.getToken());

        //保存操作节点
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(OrderStatusEnum.CUSTOMS_C_1.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_1.getDesc());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(OrderStatusEnum.CUSTOMS_C_1.getCode());
        auditInfoForm.setAuditTypeDesc(OrderOprCmdEnum.CONFIRM_ORDER.getDesc());
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "报关打单/重新打单:录入委托号")
    @PostMapping(value = "/inputEntrustNo")
    public CommonResult inputEntrustNo(@RequestBody OprStatusForm form) {
        //参数校验
        if (form.getOrderId() == null || StringUtil.isNullOrEmpty(form.getCmd()) ||
                form.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(form.getEntrustNo())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        if (OrderOprCmdEnum.ISSUE_ORDER.getCode().equals(form.getCmd())) {//重新打单时不用填操作人操作时间
            if (StringUtil.isNullOrEmpty(form.getOperatorUser())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
        }
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setEntrustNo(form.getEntrustNo());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(UserOperator.getToken());
        orderCustoms.setEntrustNo(form.getEntrustNo());
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());

        //记录操作状态
        if (OrderOprCmdEnum.RE_ISSUE_ORDER.getCode().equals(form.getCmd())) {
            //把C_2/C_3/C_4/的记录删除
            DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
            delOprStatusForm.setOrderId(form.getOrderId());
            List<String> strs = new ArrayList<>();
            strs.add(OrderStatusEnum.CUSTOMS_C_2.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_3.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
            delOprStatusForm.setStatus(strs);
            omsClient.delSpecOprStatus(delOprStatusForm);
        }
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_2.getDesc());
        form.setEntrustNo(form.getEntrustNo());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        auditInfoForm.setAuditTypeDesc(OrderStatusEnum.CUSTOMS_C_2.getDesc());
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "报关复核/报关二复")
    @PostMapping(value = "/toCheckOrder")
    public CommonResult auditOrderRelease(@RequestBody OprStatusForm form) {
        if (form.getOrderId() == null || form.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(form.getOperatorUser())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }

        //查询报关子订单
        OrderCustoms customsInfo = this.orderCustomsService.getById(form.getOrderId());
        if (!OrderStatusEnum.CUSTOMS_C_2.getCode().equals(customsInfo.getStatus()) &&
                !OrderStatusEnum.CUSTOMS_C_3.getCode().equals(customsInfo.getStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        //根据订单获取下个节点信息
        OrderStatusEnum statusEnum = OrderStatusEnum.getCustomsOrderNextStatus(customsInfo.getStatus());


        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setStatus(statusEnum.getCode());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(UserOperator.getToken());

        //保存操作节点
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(statusEnum.getCode());
        form.setStatusName(statusEnum.getDesc());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(statusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(statusEnum.getDesc());
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "报关申报/报关放行/放行确认/审核不通过-编辑完成/通关完成/通关查验/通关其他异常")
    @PostMapping(value = "/oprOrder")
    public CommonResult oprOrder(@RequestBody OprStatusForm form) {
        if (StringUtil.isNullOrEmpty(form.getCmd()) || form.getOrderId() == null ||
                form.getMainOrderId() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        form.setBusinessType(BusinessTypeEnum.BG.getCode());

        //查询订单信息
        OrderCustoms customsInfo = this.orderCustomsService.getById(form.getOrderId());
        OrderStatusEnum statusEnum = OrderStatusEnum.getCustomsOrderNextStatus(customsInfo.getStatus());
        form.checkParam(statusEnum);

        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());

        Boolean flag = true;//是否是流程节点中操作状态
        if (OrderOprCmdEnum.DECLARE.getCode().equals(form.getCmd())) {//报关申报
            if (StringUtil.isNullOrEmpty(form.getOperatorUser())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_4.getDesc());
        } else if (OrderOprCmdEnum.CUSTOMS_CLEARANCE.getCode().equals(form.getCmd())) { //报关放行
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_10.getCode());
            orderCustoms.setYunCustomsNo(form.getYunCustomsNo());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_10.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_10.getDesc());
        } else if (OrderOprCmdEnum.RELEASE_CONFIRM.getCode().equals(form.getCmd())) {//放行确认
            if (OrderStatusEnum.CUSTOMS_C_5.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.CUSTOMS_C_5_1.getCode().equals(form.getStatus())) {
                orderCustoms.setStatus(form.getStatus());
                if (OrderStatusEnum.CUSTOMS_C_5.getCode().equals(form.getStatus())) {
                    form.setStatus(form.getStatus());
                    form.setStatusName(OrderStatusEnum.CUSTOMS_C_5.getDesc());
                } else {
                    flag = false;
                }
            } else {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
        } else if (OrderOprCmdEnum.AUDIT_FAIL_EDIT.getCode().equals(form.getCmd())) {//审核不通过-编辑完成
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_4.getDesc());
            //需删除C_4/C_5的状态记录
            DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
            delOprStatusForm.setOrderId(form.getOrderId());
            List<String> strs = new ArrayList<>();
            strs.add(OrderStatusEnum.CUSTOMS_C_5.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
            delOprStatusForm.setStatus(strs);
            omsClient.delSpecOprStatus(delOprStatusForm);
        } else if (OrderOprCmdEnum.GO_CUSTOMS_SUCCESS.getCode().equals(form.getCmd())) {//通关完成
            if (form.getGoCustomsTime() == null) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            orderCustoms.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(), DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6.getDesc());
        } else if (OrderOprCmdEnum.CUSTOMS_CHECK.getCode().equals(form.getCmd())) {//通关查验
            if (form.getPreGoCustomsTime() == null) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            orderCustoms.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(), DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6_1.getDesc());
        } else if (OrderOprCmdEnum.CUSTOMS_EXCEP.getCode().equals(form.getCmd())) {//通关其他异常
            if (form.getPreGoCustomsTime() == null) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            orderCustoms.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(), DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6_2.getDesc());
            flag = false;
        }
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(UserOperator.getToken());
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);

        //只保存操作成功，既能推动流程的状态,便于流程节点的展示
        if (flag) {
            form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
            form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));

            omsClient.saveOprStatus(form);
        }

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(OrderOprCmdEnum.getDesc(form.getCmd()));
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);
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


    /**
     * 该接口针对填各种驳回
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "报关接单驳回:要么所有报关单驳回,要么不驳回,前台给出相应提示cmd=C_1_1")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody RejectOrderForm form) {
        if (form.getMainOrderNo() == null || StringUtil.isNullOrEmpty(form.getCmd())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        //根据主订单号查询所有的报关子订单
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO, form.getMainOrderNo());
        List<OrderCustoms> orderCustoms = orderCustomsService.list(queryWrapper);
        List<OrderCustoms> orderCustomsList = new ArrayList<>();
        for (OrderCustoms temp : orderCustoms) {
            OrderCustoms orderCustom = new OrderCustoms();
            orderCustom.setId(temp.getId());
            orderCustom.setUpdatedTime(LocalDateTime.now());
            orderCustom.setUpdatedUser(UserOperator.getToken());


            //记录审核信息
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtId(temp.getId());
            auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);

            //删除操作流程记录 这个节点暂时不需要删除

            if (OrderStatusEnum.CUSTOMS_C_1_1.getCode().equals(form.getCmd())) {//确认接单驳回
                orderCustom.setStatus(OrderStatusEnum.CUSTOMS_C_1_1.getCode());

                auditInfoForm.setAuditStatus(OrderStatusEnum.CUSTOMS_C_1_1.getCode());
                auditInfoForm.setAuditTypeDesc(OrderStatusEnum.CUSTOMS_C_1_1.getDesc());
                auditInfoForm.setAuditComment(form.getCause());
            }

            orderCustomsList.add(orderCustom);
            omsClient.saveAuditInfo(auditInfoForm);
        }
        boolean result = true;
        if (orderCustomsList.size() > 0) {
            result = orderCustomsService.updateBatchById(orderCustomsList);
        }
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


}

