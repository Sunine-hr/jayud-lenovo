package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
import com.jayud.customs.model.vo.FileView;
import com.jayud.customs.model.vo.InputOrderVO;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "暂存/提交")
    @PostMapping(value = "/oprOrderCustoms")
    public CommonResult oprOrderCustoms(@RequestBody InputOrderForm form) {
        //参数校验
        boolean flag = true;
        if(form == null || form.getCmd() == null || "".equals(form.getCmd())){
            return CommonResult.error(400,"参数不合法");
        }
        InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();
        if(inputMainOrderForm == null || inputOrderCustomsForm == null){
            return CommonResult.error(400,"参数不合法");
        }
        //处理附件，前台提供数组
        List<InputSubOrderCustomsForm> subOrderCustomsForms = inputOrderCustomsForm.getSubOrders();
        for(InputSubOrderCustomsForm inputSubOrderCustomsForm : subOrderCustomsForms){
            List<FileView> fileViews = inputSubOrderCustomsForm.getFileViews();
            StringBuffer buf = new StringBuffer();
            for (FileView fileView : fileViews) {
                buf.append(fileView.getRelativePath()).append(",");
            }
            if(!"".equals(String.valueOf(buf))) {
                inputSubOrderCustomsForm.setDescription(buf.substring(0, buf.length() - 1));
            }
        }
        if("submit".equals(form.getCmd())){
            if(inputMainOrderForm.getCustomerCode() == null || "".equals(inputMainOrderForm.getCustomerCode())
            || inputMainOrderForm.getCustomerName() == null || "".equals(inputMainOrderForm.getCustomerName())
            || inputMainOrderForm.getBizUid() == null
            || inputMainOrderForm.getBizUname() == null || "".equals(inputMainOrderForm.getBizUname())
            || inputMainOrderForm.getLegalName() == null || "".equals(inputMainOrderForm.getLegalName())
            || inputMainOrderForm.getBizBelongDepart() == null
            || inputMainOrderForm.getReferenceNo() == null || "".equals(inputMainOrderForm.getReferenceNo())
                    //报关单
            || inputOrderCustomsForm.getPortCode() == null || "".equals(inputOrderCustomsForm.getPortCode())
            || inputOrderCustomsForm.getPortName() == null || "".equals(inputOrderCustomsForm.getPortName())
            || inputOrderCustomsForm.getGoodsType() == null
            || inputOrderCustomsForm.getSubOrders() == null || inputOrderCustomsForm.getSubOrders().size() == 0){
                flag = false;
            }
            //子订单参数校验
            for (InputSubOrderCustomsForm subOrderCustomsForm : inputOrderCustomsForm.getSubOrders()) {
                if(subOrderCustomsForm.getOrderNo() == null || "".equals(subOrderCustomsForm.getOrderNo())
                        || subOrderCustomsForm.getTitle() == null || "".equals(subOrderCustomsForm.getTitle())
                    || subOrderCustomsForm.getUnitCode() == null || "".equals(subOrderCustomsForm.getUnitCode())
                    || subOrderCustomsForm.getFileViews().size() == 0) {
                    flag = false;
                    break;
                }
            }
            if(!flag){
                return CommonResult.error(400,"参数不合法");
            }
        }
        //功能逻辑
        boolean result = orderCustomsService.oprOrderCustoms(form);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

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

    @ApiOperation(value = "编辑回显,id=主订单ID")
    @PostMapping(value = "/editOrderCustomsView")
    public CommonResult<InputOrderVO> editOrderCustomsView(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        InputOrderVO inputOrderVO = orderCustomsService.editOrderCustomsView(Long.parseLong(id));
        return CommonResult.success(inputOrderVO);
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
        form.setOperatorTime(LocalDateTime.now());
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
                form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
                form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                form.getOperatorTime() == null || "".equals(form.getOperatorTime()) ||
                form.getEntrustNo() == null || "".equals(form.getEntrustNo())){
            return CommonResult.error(400,"参数不合法");
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
                form.setStatus(form.getStatus());
                if(OrderStatusEnum.CUSTOMS_C_5.getCode().equals(form.getStatus())){
                    form.setStatusName(OrderStatusEnum.CUSTOMS_C_5.getDesc());
                }else {
                    form.setStatusName(OrderStatusEnum.CUSTOMS_C_5_1.getDesc());
                }

            }else {
                return CommonResult.error(400, "参数不合法");
            }
        } else if("auditFailEdit".equals(form.getCmd())){//审核不通过-编辑完成
            if (form.getEntrustNo() == null || "".equals(form.getEntrustNo())){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_4.getCode());
            form.setStatusName(OrderStatusEnum.CUSTOMS_C_4.getDesc());
        } else if("goCustomsSuccess".equals(form.getCmd())){//通关完成
            if(form.getGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            orderCustoms.setGoCustomsTime(form.getGoCustomsTime());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6.getDesc());
            form.setDescription(form.getDescription());
        }else if("customsCheck".equals(form.getCmd())){//通关查验
            if(form.getPreGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            orderCustoms.setPreGoCustomsTime(form.getPreGoCustomsTime());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_1.getDesc());
            form.setDescription(form.getDescription());
        }else if("customsExcep".equals(form.getCmd())){//通关其他异常
            if(form.getPreGoCustomsTime() == null){
                return CommonResult.error(400, "参数不合法");
            }
            orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            orderCustoms.setPreGoCustomsTime(form.getPreGoCustomsTime());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getCode());
            form.setStatus(OrderStatusEnum.CUSTOMS_C_6_2.getDesc());
            form.setDescription(form.getDescription());
        }
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        omsClient.saveOprStatus(form);
        if (!result) {
            return CommonResult.error(400, "操作失败");
        }
        return CommonResult.success();
    }





}

