package com.jayud.oms.controller;

import com.jayud.common.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序
 */
@RestController
@RequestMapping("/miniApp")
@Api(tags = "微信小程序")
public class MiniAppController {

    @PostMapping("/test")
    public CommonResult test() {
        return CommonResult.success("成功进入方法！");
    }
}
