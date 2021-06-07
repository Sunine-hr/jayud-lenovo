package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.Dict;
import com.jayud.customs.model.vo.OrderStatusVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * customs模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {


    /**
     * 操作主订单
     *
     * @return
     */
    @RequestMapping(value = "/api/oprMainOrder")
    ApiResult oprMainOrder(@RequestBody InputMainOrderForm form);

    /**
     * 记录操作日志
     */
    @RequestMapping(value = "/api/saveOprOrderLog")
    ApiResult saveOprOrderLog(@RequestBody List<OprOrderLogForm> forms);


    /**
     * 获取主订单信息
     */
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult<Long> getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo);

    /**
     * 只记录成功流程状态
     */
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form);

    /**
     * 子订单流程
     *
     * @return
     */
    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult<List<OrderStatusVO>> handleSubProcess(@RequestBody HandleSubProcessForm form);

    /**
     * 记录审核信息
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult saveAuditInfo(@RequestBody AuditInfoForm form);

    /**
     * 删除特定单的操作流程
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/delSpecOprStatus")
    ApiResult delSpecOprStatus(@RequestBody DelOprStatusForm form);

    /**
     * 获取订单号
     *
     * @return
     */
    @RequestMapping(value = "/api/getOrderNo")
    ApiResult getOrderNo(@RequestParam("preOrder") String preOrder, @RequestParam("classCode") String classCode);


    @ApiOperation(value = "主订单驳回标识操作")
    @RequestMapping(value = "/api/doMainOrderRejectionSignOpt")
    public ApiResult<Boolean> doMainOrderRejectionSignOpt(@RequestParam("mainOrderNo") String mainOrderNo,
                                                          @RequestParam("rejectionDesc") String rejectionDesc);


    /**
     * 根据字典类型下拉选项字典
     */
    @RequestMapping(value = "/api/initDictByDictTypeCode")
    public ApiResult<List<InitComboxStrVO>> initDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode);

    /**
     * 获取过滤订单状态数量
     */
    @RequestMapping(value = "/api/getFilterOrderStatus")
    public ApiResult<Integer> getFilterOrderStatus(@RequestParam("mainOrderNos") List<String> mainOrderNos,
                                                   @RequestParam("status") Integer status);

    /**
     * 根据字典名称获取字典代码
     * @param supervisionMode
     * @return
     */
    @RequestMapping(value = "/api/getDictCodeByDictTypeName")
    public ApiResult<String> getDictCodeByDictTypeName(@RequestParam("supervisionMode") String supervisionMode);

    /**
     * 查询待审核费用订单数量
     *
     * @return
     */
    @RequestMapping(value = "/api/auditPendingExpenses")
    ApiResult<Integer> auditPendingExpenses(@RequestParam("subType") String subType,
                                            @RequestParam("legalIds") List<Long> legalIds,
                                            @RequestParam("orderNos")List<String> orderNos);

    /**
     * 推送应收单到报关
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/api/finance/oms/yunbaoguan/receivable/push", method = RequestMethod.POST)
    Boolean saveReceivableBill(@RequestBody String msg);

    /**
     * 获取字典配置
     * @param dictTypeCode
     * @return
     */
    @RequestMapping(path = "/dict/findDict", method = RequestMethod.POST)
    CommonResult<List<Dict>> findDictType(@RequestParam("dictTypeCode") String dictTypeCode);

    /**
     * 获取主订单客户名称
     */
    @RequestMapping(value = "/api/getCustomerNameByOrderNo")
    ApiResult<String> getCustomerNameByOrderNo(@RequestParam(value = "orderNo") String orderNo);
}
