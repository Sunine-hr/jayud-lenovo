package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.bo.InputSubOrderCustomsForm;
import com.jayud.customs.model.bo.OprStatusForm;
import com.jayud.customs.model.bo.QueryCustomsOrderInfoForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
import com.jayud.customs.model.vo.FileView;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
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
    public CommonResult oprOrderCustoms(@RequestBody InputOrderCustomsForm form) {
        //参数校验
        boolean flag = true;
        if(form == null || form.getCmd() == null || "".equals(form.getCmd())){
            return CommonResult.error(400,"参数不合法");
        }
        //处理附件，前台提供数组
        List<InputSubOrderCustomsForm> subOrderCustomsForms = form.getSubOrders();
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
            if(form.getCustomerCode() == null || "".equals(form.getCustomerCode())
            || form.getCustomerName() == null || "".equals(form.getCustomerName())
            || form.getBizUid() == null
            || form.getBizUname() == null || "".equals(form.getBizUname())
            || form.getLegalCode() == null || "".equals(form.getLegalCode())
            || form.getLegalName() == null || "".equals(form.getLegalName())
            || form.getBizBelongDepart() == null
            || form.getReferenceNo() == null || "".equals(form.getReferenceNo())
            || form.getPortCode() == null || "".equals(form.getPortCode())
            || form.getPortName() == null || "".equals(form.getPortName())
            || form.getGoodsType() == null
            || form.getSubOrders() == null){
                flag = false;
            }
            //子订单参数校验
            if(form.getSubOrders() != null){
                for (InputSubOrderCustomsForm subOrderCustomsForm : form.getSubOrders()) {
                    if(subOrderCustomsForm.getOrderNo() == null || "".equals(subOrderCustomsForm.getOrderNo())
                    || subOrderCustomsForm.getTitle() == null || "".equals(subOrderCustomsForm.getTitle())
                    || subOrderCustomsForm.getUnitCode() == null || "".equals(subOrderCustomsForm.getUnitCode())
                    || subOrderCustomsForm.getUnitAccount() == null || "".equals(subOrderCustomsForm.getUnitAccount())
                    || subOrderCustomsForm.getDescription() == null || "".equals(subOrderCustomsForm.getDescription())) {
                        flag = false;
                        break;
                    }
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
    public CommonResult<InputOrderCustomsVO> editOrderCustomsView(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        InputOrderCustomsVO inputOrderCustomsVO = orderCustomsService.editOrderCustomsView(Long.parseLong(id));
        return CommonResult.success(inputOrderCustomsVO);
    }

    @ApiOperation(value = "报关接单列表/放行异常列表/放行确认/审核不通过/订单列表/报关打单/复核/申报")
    @PostMapping("/findCustomsOrderByPage")
    public CommonResult<CommonPageResult<CustomsOrderInfoVO>> findCustomsOrderByPage(@RequestBody QueryCustomsOrderInfoForm form) {
        IPage<CustomsOrderInfoVO> pageList = orderCustomsService.findCustomsOrderByPage(form);
        CommonPageResult<CustomsOrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


    @ApiOperation(value = "确认接单,mainOrderId/orderId/operatorUser必传")
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

    @ApiOperation(value = "录入委托号,mainOrderId/orderId/operatorUser/operatorTime必传,inputEntrustNo=委托号")
    @PostMapping(value = "/inputEntrustNo")
    public CommonResult inputEntrustNo(@RequestBody OprStatusForm form,@RequestBody Map<String,Object> param) {
        String inputEntrustNo = MapUtil.getStr(param,"inputEntrustNo");
        if(form.getOrderId() == null || "".equals(form.getOrderId()) ||
                form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
                form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                form.getOperatorTime() == null || "".equals(form.getOperatorTime()) ||
                inputEntrustNo == null || "".equals(inputEntrustNo)){
            return CommonResult.error(400,"参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(form.getOrderId());
        orderCustoms.setEntrustNo(inputEntrustNo);
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        orderCustoms.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        //记录操作状态
        form.setStatus(OrderStatusEnum.CUSTOMS_C_2.getCode());
        form.setStatusName(OrderStatusEnum.CUSTOMS_C_2.getDesc());
        omsClient.saveOprStatus(form);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "复核,mainOrderId/orderId/operatorUser/operatorTime/status必传,status=C_3 通过 or C_3_1 驳回")
    @PostMapping(value = "/toCheckOrder")
    public CommonResult auditOrderRelease(@RequestBody OprStatusForm form) {
        if(form.getOrderId() == null || "".equals(form.getOrderId()) ||
                form.getMainOrderId() == null || "".equals(form.getMainOrderId()) ||
                form.getOperatorUser() == null || "".equals(form.getOperatorUser()) ||
                form.getOperatorTime() == null || "".equals(form.getOperatorTime())){
            return CommonResult.error(400,"参数不合法");
        }
        boolean result =false;
        if(OrderStatusEnum.CUSTOMS_C_3.getCode().equals(form.getStatus()) ||
                OrderStatusEnum.CUSTOMS_C_3_1.getCode().equals(form.getStatus())){
            String loginUser = orderCustomsService.getLoginUser();
            OrderCustoms orderCustoms = new OrderCustoms();
            orderCustoms.setId(form.getOrderId());
            orderCustoms.setStatus(form.getStatus());
            orderCustoms.setUpdatedTime(LocalDateTime.now());
            orderCustoms.setUpdatedUser(loginUser);
            result = orderCustomsService.saveOrUpdate(orderCustoms);

            //记录操作状态
            form.setStatus(form.getStatus());
            if(OrderStatusEnum.CUSTOMS_C_3.getCode().equals(form.getStatus())){
                form.setStatusName(OrderStatusEnum.CUSTOMS_C_3.getDesc());
            }else {
                form.setStatusName(OrderStatusEnum.CUSTOMS_C_3_1.getDesc());
            }
            omsClient.saveOprStatus(form);
        }
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

