package com.jayud.customs.feign;

import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
//
//    /**
//     * 推送应收单到金蝶
//     *
//     * @param msg
//     * @return
//     */
//    @RequestMapping(path = "/api/finance/kingdee/yunbaoguan/receivable/push", method = RequestMethod.POST)
//    CommonResult saveReceivableBill(@RequestBody Map<String, String> msg);
//
//    /**
//     * 推送应付单到金蝶
//     *
//     * @param msg
//     * @return
//     */
//    @RequestMapping(path = "/api/finance/kingdee/yunbaoguan/payable/push", method = RequestMethod.POST)
//    CommonResult savePayableBill(@RequestBody Map<String, String> msg);


    /**
     * 直接删除应收数据
     *
     * @param param
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = "/api/finance/kingdee/yunbaoguan/receivable/delete")
    Boolean checkNRemoveReceivable(@RequestBody Map<String, String> param);

    /**
     * 直接删除应付数据
     *
     * @param param
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = "/api/finance/kingdee/yunbaoguan/payable/delete")
    Boolean checkNRemovePayable(@RequestBody Map<String, String> param);


}