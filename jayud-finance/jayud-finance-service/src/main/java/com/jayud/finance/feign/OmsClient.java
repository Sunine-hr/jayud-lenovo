package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import com.jayud.finance.bo.AuditInfoForm;
import com.jayud.finance.bo.OprCostBillForm;
import com.jayud.finance.bo.OrderCostForm;
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
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult<Boolean> saveAuditInfo(@RequestBody AuditInfoForm form);

    /**
     * 币种
     *
     * @return
     */
    @RequestMapping(value = "api/initCurrencyInfo")
    ApiResult<List<InitComboxStrVO>> initCurrencyInfo();

    /**
     * 币种
     *
     * @return
     */
    @RequestMapping(value = "api/initCurrencyInfo2")
    ApiResult<List<InitComboxVO>> initCurrencyInfo2();

    /**
     * 编辑保存确定
     *
     * @param costIds
     * @param oprType 是应付还是应收
     * @return
     */
    @RequestMapping(value = "api/editSaveConfirm")
    ApiResult<Boolean> editSaveConfirm(@RequestParam("costIds") List<Long> costIds, @RequestParam("oprType") String oprType,
                                       @RequestParam("cmd") String cmd);

    /**
     * 提交财务审核时，财务可能编辑费用类型
     *
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/oprCostGenreByCw")
    ApiResult<Boolean> oprCostGenreByCw(@RequestBody List<OrderCostForm> forms, @RequestParam("cmd") String cmd);


    /**
     * 开票审核通过之后，需要反推汇率和本币金额到费用录入表
     *
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/writeBackCostData")
    ApiResult writeBackCostData(@RequestBody List<OrderCostForm> forms, @RequestParam("cmd") String cmd);

    /**
     * 获取所有可用的费用类型
     *
     * @return
     */
    @RequestMapping(value = "api/findEnableCostGenre")
    ApiResult<List<InitComboxVO>> findEnableCostGenre();

    /**
     * 根据供应商名称查询供应商信息
     */
    @RequestMapping(value = "/api/getSupplierInfoByName")
    ApiResult getSupplierInfoByName(@RequestParam("name") String name);

    /**
     * 根据客户名称查询客户信息
     */
    @RequestMapping(value = "/api/getCustomerInfoByName")
    public ApiResult getCustomerInfoByName(String name);
}
