package com.oms.controller;


import com.scmrt.common.ApiResult;
import com.scmrt.service.IBhAdInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 保函功能广告管理 前端控制器
 * </p>
 *
 * @author xiaoy
 * @since 2020-04-14
 */
@RestController
@RequestMapping("/customerInfo")
public class CustomerInfoController {

    @Autowired
    private IBhAdInfoService adInfoService;

    @ApiOperation(value = "获取广告", httpMethod = "GET")
    @GetMapping("/getAdInfo")
    public ApiResult getAdInfo(@RequestParam("userId") Long userId) {
        return ApiResult.ok(adInfoService.getAdInfo(userId));
    }


}

