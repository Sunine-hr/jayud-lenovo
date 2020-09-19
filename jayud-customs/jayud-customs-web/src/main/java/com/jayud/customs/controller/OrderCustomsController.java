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
import com.jayud.customs.model.bo.QueryCustomsOrderInfoForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
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
    public CommonResult<List<String>> createOrderNo(@RequestBody Map<String,Object> param) {
        List<String> stringList = new ArrayList<>();
        int num = Integer.valueOf(MapUtil.getStr(param,"num"));
        String code = "BG";//报关类型
        for (int i = 0; i < num; i++) {//产品类别code+随机数
            String result = StringUtils.loadNum(code, 12);
            //校验子订单号是否存在,false=存在
            boolean isExist = orderCustomsService.isExistOrder(result);
            if(!isExist){
                i = i - 1;
            }
            stringList.add(result);
        }
        return CommonResult.success(stringList);
    }

    @ApiOperation(value = "编辑回显,id=主订单ID")
    @PostMapping(value = "/editOrderCustomsView")
    public CommonResult<InputOrderCustomsVO> editOrderCustomsView(Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        InputOrderCustomsVO inputOrderCustomsVO = orderCustomsService.editOrderCustomsView(Long.parseLong(id));
        return CommonResult.success(inputOrderCustomsVO);
    }

    @ApiOperation(value = "报关接单列表/报关放行/放行异常列表/放行确认/审核不通过")
    @PostMapping("/findCustomsOrderByPage")
    public CommonResult<CommonPageResult<CustomsOrderInfoVO>> findCustomsOrderByPage(@RequestBody QueryCustomsOrderInfoForm form) {
        IPage<CustomsOrderInfoVO> pageList = orderCustomsService.findCustomsOrderByPage(form);
        CommonPageResult<CustomsOrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


    @ApiOperation(value = "确认接单,id=子订单ID optUser=管理员CODE")
    @PostMapping(value = "/confirmOrder")
    public CommonResult confirmOrder(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        String optUser = MapUtil.getStr(param,"optUser");
        if(id == null || "".equals(id)){
            return CommonResult.error(400,"参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(Long.valueOf(id));
        orderCustoms.setStatus(Integer.valueOf(OrderStatusEnum.CUSTOMS_1.getCode()));
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        orderCustoms.setUserName(loginUser);
        orderCustoms.setOptName(optUser);
        orderCustoms.setOptTime(LocalDateTime.now());
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "录入委托号,id=子订单ID inputEntrustNo=委托号 entrustNote=编辑委托号的备注")
    @PostMapping(value = "/inputEntrustNo")
    public CommonResult inputEntrustNo(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        String inputEntrustNo = MapUtil.getStr(param,"inputEntrustNo");
        String entrustNote = MapUtil.getStr(param,"entrustNote");
        if(id == null || "".equals(id)){
            return CommonResult.error(400,"参数不合法");
        }
        String loginUser = orderCustomsService.getLoginUser();
        OrderCustoms orderCustoms = new OrderCustoms();
        orderCustoms.setId(Long.valueOf(id));
        orderCustoms.setEntrustNo(inputEntrustNo);
        orderCustoms.setEntrustNo(entrustNote);
        orderCustoms.setUpdatedTime(LocalDateTime.now());
        orderCustoms.setUpdatedUser(loginUser);
        boolean result = orderCustomsService.saveOrUpdate(orderCustoms);
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "审核,id=子订单ID  status=审核状态，3通过4驳回 remarks=审核意见")
    @PostMapping(value = "/auditOrderRelease")
    public CommonResult auditOrderRelease(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        String status = MapUtil.getStr(param,"status");
        String remarks = MapUtil.getStr(param,"remarks");
        if(id == null || "".equals(id) || status == null || "".equals(status)){
            return CommonResult.error(400,"参数不合法");
        }
        boolean result =false;
        if("3".equals(status) || "4".equals(status)){
            String loginUser = orderCustomsService.getLoginUser();
            OrderCustoms orderCustoms = new OrderCustoms();
            orderCustoms.setId(Long.valueOf(id));
            orderCustoms.setStatus(Integer.valueOf(status));
            orderCustoms.setRemarks(remarks);
            orderCustoms.setUpdatedTime(LocalDateTime.now());
            orderCustoms.setUpdatedUser(loginUser);
            result = orderCustomsService.saveOrUpdate(orderCustoms);
        }
        if(!result){
            return CommonResult.error(400,"操作失败");
        }
        return CommonResult.success();
    }



}

