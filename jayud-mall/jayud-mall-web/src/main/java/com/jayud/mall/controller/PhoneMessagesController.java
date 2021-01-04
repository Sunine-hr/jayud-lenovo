package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PhoneForm;
import com.jayud.mall.service.IPhoneMessagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/phonemessages")
@Api(tags = "C004-C端-手机短信接口")
@ApiSort(value = 4)
public class PhoneMessagesController {

    @Autowired
    IPhoneMessagesService phoneMessagesService;

    @ApiOperation(value = "发送消息")
    @PostMapping("/sendMessage")
    @ApiOperationSupport(order = 1)
    public CommonResult sendMessage(@Valid @RequestBody PhoneForm form){
        return phoneMessagesService.sendMessage(form);
    }

}
