package com.jayud.msg.feign;

import com.jayud.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 嗅探到财务应收应付后丢给金蝶接口
 *
 * @author william
 * @description
 * @Date: 2020-09-19 11:23
 */
@FeignClient("jayud-finance-web")
public interface FinanceClient {

    /**
     * 推送应收单到金蝶
     * @param msg
     * @return
     */
    @PostMapping("/api/finance/kingdee/receivable/push")
    public CommonResult saveReceivableBill(@RequestBody Map msg);
}
