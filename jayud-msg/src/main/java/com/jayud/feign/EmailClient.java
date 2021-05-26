package com.jayud.feign;


import com.jayud.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 调用邮件服务
 */
@FeignClient(value = "jayud-email-web")
public interface EmailClient {

    /**
     * 发送邮件
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/email/send", method = RequestMethod.POST)
    CommonResult sendEmail(@RequestBody String msg);
}
