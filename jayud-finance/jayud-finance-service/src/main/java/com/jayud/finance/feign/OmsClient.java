package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import com.jayud.finance.bo.AuditInfoForm;
import com.jayud.finance.bo.OprCostBillForm;
import com.jayud.finance.bo.OrderCostForm;
import com.jayud.finance.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * finance模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

    /**
     * 应付/应付暂存
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
                                       @RequestParam("cmd") String cmd, @RequestBody Map<String, Object> param);

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

    /**
     * 批量修改费用状态
     *
     * @param costIds 费用主键
     * @param isBill
     * @param status  费用状态
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/batchUpdateCostStatus")
    public ApiResult batchUpdateCostStatus(@RequestParam("costIds") List<Long> costIds,
                                           @RequestParam("isBill") String isBill,
                                           @RequestParam("status") Integer status,
                                           @RequestParam("type") Integer type);

    /**
     * 根据费用主键集合批量查询费用币种信息
     *
     * @param costIds 费用主键
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getCostCurrencyInfo")
    public ApiResult getCostCurrencyInfo(@RequestParam("costIds") List<Long> costIds,
                                         @RequestParam("type") Integer type);

    /**
     * 根据费用主键集合批量查询费用信息
     *
     * @param costIds 费用主键
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getCostInfo")
    public ApiResult getCostInfo(@RequestParam("costIds") List<Long> costIds,
                                 @RequestParam("type") Integer type);

    /**
     * 根据主订单集合查询主订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByOrderNos")
    ApiResult getMainOrderByOrderNos(@RequestParam("orderNos") List<String> orderNos);

    /**
     * 根据订单id集合查询商品信息
     */
    @RequestMapping(value = "/api/getGoodsByBusIds")
    public ApiResult<List<InputGoodsVO>> getGoodsByBusIds(@RequestParam("orderId") List<Long> orderId,
                                                          @RequestParam("businessType") Integer businessType);

    @ApiOperation("根据业务id集合查询订单地址")
    @RequestMapping(value = "/api/getOrderAddressByBusIds")
    public ApiResult<List<InputOrderAddressVO>> getOrderAddressByBusIds(@RequestParam("orderId") List<Long> orderId,
                                                                        @RequestParam("businessType") Integer businessType);

    /**
     * 根据审核表唯一标识查询审核描述(对账单)
     *
     * @param extUniqueFlags
     * @return
     */
    @RequestMapping(value = "/api/getByExtUniqueFlag")
    public ApiResult<Map<String, Object>> getByExtUniqueFlag(@RequestBody List<String> extUniqueFlags);


    /**
     * 根据订单号统计费用金额
     *
     * @param type 类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/statisticalCostByOrderNos")
    public ApiResult<Map<String, Map<String, BigDecimal>>> statisticalCostByOrderNos(@RequestParam("orderNos") List<String> orderNos,
                                                                                     @RequestParam("isMain") Boolean isMain,
                                                                                     @RequestParam("type") Integer type);

    /**
     * 根据订单号查询未出账单费用
     *
     * @param type 类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getNoBillCost")
    public ApiResult getNoBillCost(@RequestParam("orderNos") List<String> orderNos,
                                   @RequestParam("isMain") Boolean isMain,
                                   @RequestParam("type") Integer type);
}
