package com.jayud.customs.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
import com.jayud.customs.model.vo.OrderCustomsVO;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "customs对外接口")
public class ExternalApiController {

    @Autowired
    IOrderCustomsService orderCustomsService;

    @Autowired
    RedisUtils redisUtils;


    @ApiOperation(value = "获取子订单信息")
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        int customsNum = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("main_order_no",mainOrderNo);
        List<OrderCustomsVO> orderCustomsVOS = orderCustomsService.findOrderCustomsByCondition(param);
        if(orderCustomsVOS != null){
            customsNum = orderCustomsVOS.size();
        }
        return ApiResult.ok(customsNum);
    }

    @ApiOperation(value = "获取子订单详情")
    @RequestMapping(value = "/api/getCustomsDetail")
    ApiResult getCustomsDetail(@RequestParam(value = "mainOrderId") Long mainOrderId){
        InputOrderCustomsVO inputOrderCustomsVO = orderCustomsService.getOrderCustomsDetail(mainOrderId);
        return ApiResult.ok(inputOrderCustomsVO);
    }


    /**
     * 获取当前登录用户
     * @return
     */
    @RequestMapping(value = "/api/getLoginUser")
    ApiResult getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return ApiResult.ok(loginUser);
    }


}









    



