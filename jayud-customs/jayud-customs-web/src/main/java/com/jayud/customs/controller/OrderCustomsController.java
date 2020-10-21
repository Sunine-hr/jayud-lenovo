package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderOprCmdEnum;
import com.jayud.common.enums.OrderStatusEnum;
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
    public CommonResult<List<InputSubOrderCustomsForm>> createOrderNo(@RequestBody Map<String,Object> param) {
        List<InputSubOrderCustomsForm> stringList = new ArrayList<>();
        int num = Integer.valueOf(MapUtil.getStr(param,"num"));
        String code = "BG";//报关类型
        for (int i = 0; i < num; i++) {//产品类别code+随机数
            String result = StringUtils.loadNum(code, 12);
            //校验子订单号是否存在,false=存在
            boolean isExist = orderCustomsService.isExistOrder(result);
            if(!isExist){
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


    @ApiOperation(value = "报关接单,mainOrderId/orderId/operatorUser必传")
    @PostMapping(value = "/confirmOrder")
    public CommonResult confirmOrder(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || "".equals(form.getOrderId()) ||
          form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
          form.getOperatorUser() == null || "".equals(form.getOperatorUser())){
            return CommonResult.error(400,"参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_1.getCode());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(OrderStatusEnum.CUSTOMS_C_1.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_1.getDesc());
        omsClient.saveOprStatus(form);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "报关打单/重新打单:录入委托号,mainOrderId/orderId/operatorUser/operatorTime/entrustNo必传")
    @PostMapping(value = "/inputEntrustNo")
    public CommonResult inputEntrustNo(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || "".equals(form.getOrderId()) ||
                form.getCmd() == null || "".equals(form.getCmd()) ||
                form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
                form.getEntrustNo() == null || "".equals(form.getEntrustNo())){
            return CommonResult.error(400,"参数不合法");
        }
        if("issueOrder".equals(form.getCmd())){//报关打单，重新打单时不用填操作人操作时间
            if(form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                    form.getOperatorTime() == null || "".equals(form.getOperatorTime())){
                return CommonResult.error(400,"参数不合法");
            }
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setEntrustNo(form.getEntrustNo());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        if("reIssueOrder".equals(form.getCmd())){
            form.setOperatorUser(loginUser);
            form.setOperatorTime(DateUtils.getLocalToStr(LocalDateTime.now()));
            //把C_2/C_3/C_4/的记录删除
            DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
            delOprStatusForm.setMainOrderId(form.getMainOrderId());
            delOprStatusForm.setOrderId(form.getOrderId());
            List<String> strs = new ArrayList<>();
            strs.add(OrderStatusEnum.CUSTOMS_C_2.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_3.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
            delOprStatusForm.setStatus(strs);
            omsClient.deleteOprStatus(delOprStatusForm);
        }
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_2.getDesc());
        form.setEntrustNo(form.getEntrustNo());
        omsClient.saveOprStatus(form);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "报关复核,mainOrderId/orderId/operatorUser/operatorTime必传")
    @PostMapping(value = "/toCheckOrder")
    public CommonResult auditOrderRelease(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || "".equals(form.getOrderId()) ||
                form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
                form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                form.getOperatorTime() == null || "".equals(form.getOperatorTime())){
            return CommonResult.error(400,"参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_3.getCode());
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setStatus(OrderStatusEnum.CUSTOMS_C_3.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_3.getDesc());
        omsClient.saveOprStatus(form);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "申报/放行确认")
    @PostMapping(value = "/oprOrder")
    public CommonResult oprOrder(@RequestBody OprStatusForm form) {
        if(form.getCmd() == null || "".equals(form.getCmd()) ||
                form.getOrderId() == null || "".equals(form.getOrderId()) ||
                form.getMainOrderId() == null || "".equals(form.getMainOrderId())){
            return CommonResult.error(400, "参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        if("declare".equals(form.getCmd())) {//申报
            if (form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                    form.getOperatorTime() == null || "".equals(form.getOperatorTime())) {
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_4.getDesc());
        }else if("releaseConfirm".equals(form.getCmd())){//放行确认
            if(OrderStatusEnum.CUSTOMS_C_5.getCode().equals(form.getStatus()) ||
                    OrderStatusEnum.CUSTOMS_C_5_1.getCode().equals(form.getStatus())){
                orderCustoms.setStatus(form.getStatus());
                if(OrderStatusEnum.CUSTOMS_C_5.getCode().equals(form.getStatus())){
                    form.setStatus(form.getStatus());
                    form.setStatusName(OrderStatusEnum.CUSTOMS_C_5.getDesc());
                }
            }else {
                return CommonResult.error(400, "参数不合法");
            }
        } else if("auditFailEdit".equals(form.getCmd())){//审核不通过-编辑完成
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_4.getDesc());
            //需删除C_4/C_5的状态记录
            DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
            delOprStatusForm.setMainOrderId(form.getMainOrderId());
            delOprStatusForm.setOrderId(form.getOrderId());
            List<String> strs = new ArrayList<>();
            strs.add(OrderStatusEnum.CUSTOMS_C_5.getCode());
            strs.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
            delOprStatusForm.setStatus(strs);
            omsClient.deleteOprStatus(delOprStatusForm);
        } else if("goCustomsSuccess".equals(form.getCmd())){//通关完成
            if(form.getGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            orderCustoms.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6.getDesc());
            form.setDescription(form.getDescription());
        }else if("customsCheck".equals(form.getCmd())){//通关查验
            if(form.getPreGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            orderCustoms.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6_1.getDesc());
            form.setDescription(form.getDescription());
        }else if("customsExcep".equals(form.getCmd())){//通关其他异常
            if(form.getPreGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            orderCustoms.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_6_2.getDesc());
            form.setDescription(form.getDescription());
        }
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        if(!StringUtil.isNullOrEmpty(form.getStatus())){
            omsClient.saveOprStatus(form);//只保存操作成功，既能推动流程的状态,便于流程节点的展示
        }
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(OrderOprCmdEnum.getDesc(form.getCmd()));
        auditInfoForm.setExtId(form.getOrderId());
        omsClient.saveAuditInfo(auditInfoForm);
        if (!result) {
            return CommonResult.error(400, "操作失败");
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "子订单处理流程")
    @PostMapping(value = "/handleSubProcess")
    public CommonResult handleSubProcess(@RequestBody @Valid HandleSubProcessForm form) {
        List<OrderStatusVO> orderStatusVOS = omsClient.handleSubProcess(form).getData();
        return CommonResult.success(orderStatusVOS);
    }




}

