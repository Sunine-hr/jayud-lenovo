package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.bo.InputSubOrderCustomsForm;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

