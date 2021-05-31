package com.jayud.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * msg模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {
    /**
     * 修改报关邮件发送状态
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/changeCustomsIsSendMail")
    ApiResult changeCustomsIsSendMail(@RequestParam(value = "mainOrderNo") String mainOrderNo);

}
