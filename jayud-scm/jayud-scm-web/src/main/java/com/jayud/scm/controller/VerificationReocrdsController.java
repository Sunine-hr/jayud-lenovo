package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.service.IVerificationReocrdsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 核销列表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-08
 */
@RestController
@RequestMapping("/verificationReocrds")
@Api(tags = "收款核销管理")
public class VerificationReocrdsController {

    @Autowired
    private IVerificationReocrdsService verificationReocrdsService;

    @ApiOperation(value = "核销")
    @PostMapping(value = "/writeOff")
    public CommonResult writeOff(@RequestBody List<AddVerificationReocrdsForm> form){
        boolean result = verificationReocrdsService.writeOff(form);
        if(!result){
            return CommonResult.error(444,"核销失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "反核销")
    @PostMapping(value = "/cancelWriteOff")
    public CommonResult cancelWriteOff(@RequestBody QueryCommonForm form){
        boolean result = verificationReocrdsService.cancelWriteOff(form);
        if(!result){
            return CommonResult.error(444,"反核销失败");
        }
        return CommonResult.success();
    }

}

