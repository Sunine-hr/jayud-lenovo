package com.jayud.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 调用报关模块服务
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

    /**
     * 推送应收单到报关
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/api/finance/oms/yunbaoguan/receivable/push", method = RequestMethod.POST)
    Boolean saveReceivableBill(@RequestBody String msg);
}
