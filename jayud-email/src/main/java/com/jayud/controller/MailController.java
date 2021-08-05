package com.jayud.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.model.po.Email;
import com.jayud.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "电子邮件接口")
@RequestMapping("/email")
public class MailController {
    @Autowired
    MailService mailService;

    @ApiOperation(value = "发送电子邮件带附件")
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public CommonResult sendMailWithAttachments(@RequestBody String msg) {
        Map param = JSONObject.parseObject(msg, Map.class);
        String reqMsg = MapUtil.getStr(param, "msg");
        log.info("接收到邮件信息：{}", reqMsg);

        //解析数据，获取应收的数据实体
        Map map = JSONObject.parseObject(reqMsg, Map.class);
        String email = MapUtil.getStr(map, "email");
        Email emailForm = JSONObject.parseObject(email, Email.class);
        CommonResult.success();
        Boolean result = mailService.sendMailWithAttachments(emailForm);

        return result ? CommonResult.success() : CommonResult.error(-1, "邮件发送失败");
    }


    @ApiOperation(value = "发送电子邮件")
    @RequestMapping(path = "/sendEmail", method = RequestMethod.POST)
    public CommonResult sendEmail(@RequestBody com.jayud.common.entity.Email email) {
        Boolean result = mailService.send(email);
        return result ? CommonResult.success() : CommonResult.error(-1, "邮件发送失败");
    }
}
