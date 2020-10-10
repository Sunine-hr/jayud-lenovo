package com.jayud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @RequestMapping(path = "/api/finance/kingdee/yunbaoguan/receivable/push", method = RequestMethod.POST)
    Boolean saveReceivableBill(@RequestBody String msg);

    /**
     * 推送应付单到金蝶
     * @param msg
     * @return
     */
    @RequestMapping(path = "/api/finance/kingdee/yunbaoguan/payable/push", method = RequestMethod.POST)
    Boolean savePayableBill(@RequestBody String msg);

}
