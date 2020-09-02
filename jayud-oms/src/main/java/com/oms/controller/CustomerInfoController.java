package com.oms.controller;


import com.jayud.service.ICustomerInfoService;
import com.jayud.common.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理 前端控制器
 */
@RestController
@RequestMapping("/customerInfo")
public class CustomerInfoController {

    @Autowired
    private ICustomerInfoService ICustomerInfoService;

    @ApiOperation(value = "获取用户基本信息", httpMethod = "GET")
    @GetMapping("/getAdInfo")
    public ApiResult getAdInfo(@RequestParam("userId") Long userId) {
        return ApiResult.ok("");
    }


}

