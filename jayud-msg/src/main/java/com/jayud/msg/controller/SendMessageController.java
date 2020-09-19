package com.jayud.msg.controller;

import com.jayud.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 提供接口供外部调用，向kafka推送数据
 *
 * @author william
 * @description
 * @Date: 2020-09-19 12:07
 */
@RestController
@RequestMapping("/api/msgCenter")
@Api(tags = "kafka操作")
public class SendMessageController {
    @PostMapping("/send")
    @ApiOperation("向kafka推送数据")
    public CommonResult sendMessage(@RequestBody Map<String, String> param) {
        return CommonResult.success();
    }
}
