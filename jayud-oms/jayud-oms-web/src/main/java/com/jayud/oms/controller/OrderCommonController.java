package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.oms.model.bo.InputCostForm;
import com.jayud.oms.model.vo.InputCostVO;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/orderCommon")
@Api(tags = "订单通用接口")
public class OrderCommonController {

    @Autowired
    private IOrderInfoService orderInfoService;

    //1.状态流处理 TODO



    @ApiOperation(value = "录入费用")
    @PostMapping(value = "/saveOrUpdateCost")
    public CommonResult saveOrUpdateCost(@RequestBody InputCostForm form) {
        boolean result = orderInfoService.saveOrUpdateCost(form);
        if(!result){
            return CommonResult.error(400,"调用失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "费用详情,id= 主订单ID")
    @PostMapping(value = "/getCostDetail")
    public CommonResult getCostDetail(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        InputCostVO inputCostVO = orderInfoService.getCostDetail(Long.parseLong(id));
        return CommonResult.success(inputCostVO);
    }






}

