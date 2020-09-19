package com.jayud.receive;

import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "scmrt-mini-credit-admin-config", path = "/${msgUrl.creditAdmin:}")
public interface CreditMsgClient {
    /**
     * 发送消息
     */
    @PostMapping("/xdMsgInfo/save")
    ApiResult customer(@RequestParam("value") String value);
}

