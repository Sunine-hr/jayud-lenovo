package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import com.jayud.finance.bo.AuditInfoForm;
import com.jayud.finance.bo.OprCostBillForm;
import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.InitComboxVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * finance模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

    /**
     * 应付暂存
     */
    @RequestMapping(value = "/api/oprCostBill")
    ApiResult<Boolean> oprCostBill(@RequestBody OprCostBillForm form);

    /**
     * 记录审核信息
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult<Boolean> saveAuditInfo(@RequestBody AuditInfoForm form);

    /**
     * 币种
     * @return
     */
    @RequestMapping(value = "api/initCurrencyInfo")
    ApiResult<List<InitComboxStrVO>> initCurrencyInfo();

    /**
     * 币种
     * @return
     */
    @RequestMapping(value = "api/initCurrencyInfo2")
    ApiResult<List<InitComboxVO>> initCurrencyInfo2();

    /**
     * 编辑保存确定
     * @param costIds
     * @param oprType 是应付还是应收
     * @return
     */
    @RequestMapping(value = "api/editSSaveConfirm")
    ApiResult<Boolean> editSaveConfirm(@RequestParam(value = "costIds") List<Long> costIds,@RequestParam("oprType") String oprType,
                                        @RequestParam("cmd") String cmd);


}
