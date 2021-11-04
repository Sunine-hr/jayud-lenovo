package com.jayud.Inlandtransport.feign;


import com.jayud.Inlandtransport.model.bo.OprStatusForm;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderAddressVO;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.common.entity.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * oms模块的接口
 */
@FeignClient(value = "jayud-oms-web", configuration = FeignRequestInterceptor.class)
public interface OmsClient {


    /**
     * 根据客户名称获取订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByCustomerName")
    ApiResult getByCustomerName(@RequestParam("customerName") String customerName);


    /**
     * 根据主订单集合查询主订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByOrderNos")
    ApiResult getMainOrderByOrderNos(@RequestParam("orderNos") List<String> orderNos);

    /**
     * 只记录成功操作流程状态
     */
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form);

    /**
     * 记录审核信息
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult saveAuditInfo(@RequestBody AuditInfoForm form);

    /**
     * 初始化审核通过车辆供应商
     *
     * @return
     */
    @RequestMapping(value = "/api/initSupplierInfo")
    CommonResult<List<InitComboxVO>> initSupplierInfo();

    /**
     * 根据客户id查询客户信息
     */
    @RequestMapping(value = "/api/getCustomerInfoById")
    ApiResult getCustomerInfoById(@RequestParam("id") Long id);


    /**
     * 删除特定单的操作流程
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/delSpecOprStatus")
    ApiResult delSpecOprStatus(@RequestBody DelOprStatusForm form);


    /**
     * 查询物流轨迹节点
     */
    @RequestMapping(value = "/api/getLogisticsTrackNode")
    ApiResult getLogisticsTrackNode(@RequestBody String condition);

    /**
     * 根据主订单号修改主订单
     */
    @RequestMapping(value = "/api/updateByMainOrderNo")
    public ApiResult updateByMainOrderNo(@RequestBody String value);


    /**
     * 根据orderId和类型删除物流轨迹跟踪表
     */
    @RequestMapping(value = "/api/deleteLogisticsTrackByType")
    ApiResult deleteLogisticsTrackByType(@RequestParam("orderId") Long orderId, @RequestParam("type") Integer type);


    /**
     * 根据业务id集合查询订单地址
     */
    @RequestMapping(value = "/api/getOrderAddressByBusIds")
    public ApiResult<List<OrderAddressVO>> getOrderAddressByBusIds(@RequestParam("orderId") List<Long> orderId,
                                                                   @RequestParam("businessType") Integer businessType);

    /**
     * 根据订单id集合查询商品信息
     */
    @RequestMapping(value = "/api/getGoodsByBusIds")
    public ApiResult<List<GoodsVO>> getGoodsByBusIds(@RequestParam("orderId") List<Long> orderId,
                                                     @RequestParam("businessType") Integer businessType);

    /**
     * 获取主订单ID
     */
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo);

//    /**
//     * 获取所有的车型尺寸
//     *
//     * @return
//     */
//    @RequestMapping(value = "/api/findVehicleSize")
//    ApiResult<List<VehicleSizeInfoVO>> findVehicleSize();

    /**
     * 根据供应商id集合查询供应商信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getSupplierInfoByIds")
    ApiResult getSupplierInfoByIds(@RequestParam("supplierIds") List<Long> supplierIds);


    /**
     * 根据字典类型查询字典
     */
    @RequestMapping(value = "/api/getDictByDictTypeCode")
    public ApiResult getDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode);

    /**
     * 根据字典类型下拉选项字典
     */
    @RequestMapping(value = "/api/initDictByDictTypeCode")
    public ApiResult<List<InitComboxStrVO>> initDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode);

    /**
     * 订单地址(保存提货/送货地址)
     *
     * @return
     */
    @RequestMapping(value = "/api/addDeliveryAddress")
    public ApiResult addDeliveryAddress(@RequestBody List<OrderDeliveryAddress> deliveryAddressList);

    /**
     * 获取订单节点
     *
     * @return
     */
    @RequestMapping(value = "/api/getOrderProcessNode")
    public ApiResult<String> getOrderProcessNode(@RequestParam("mainOrderNo") String mainOrderNo,
                                                 @RequestParam("orderNo") String orderNo,
                                                 @RequestParam("currentNodeStatus") String currentNodeStatus);

    /**
     * 获取提货/送货地址信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getDeliveryAddress")
    public ApiResult<List<OrderDeliveryAddress>> getDeliveryAddress(@RequestParam("orderId") List<Long> orderId,
                                                                    @RequestParam("businessType") Integer businessType);

    /**
     * 初始化车辆下拉框
     */
    @RequestMapping(value = "api/initVehicle")
    ApiResult<List<InitComboxVO>> initVehicle(@RequestParam("type") Integer type);

    /**
     * 陆车牌下拉框联动车辆供应商，大陆车牌，香港车牌，司机电话
     */
    @RequestMapping(value = "api/initVehicleInfo")
    public ApiResult initVehicleInfo(@RequestParam("vehicleId") Long vehicleId);

    /**
     * 根据车辆id查询车辆信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getVehicleInfoByIds")
    public ApiResult getVehicleInfoByIds(@RequestParam("orderIds") List<Long> orderIds);


    @RequestMapping(value = "/api/getOrderNo")
    ApiResult getOrderNo(@RequestParam("preOrder") String preOrder, @RequestParam("classCode") String classCode);

    /**
     * 查询联系人信息
     */
    @RequestMapping(value = "/getContactInfoByPhone")
    public CommonResult<List<Map<String, Object>>> getContactInfoByPhone(@RequestParam("businessType") Integer businessType);

    @ApiOperation(value = "主订单驳回标识操作")
    @RequestMapping(value = "/api/doMainOrderRejectionSignOpt")
    public ApiResult<Boolean> doMainOrderRejectionSignOpt(@RequestParam("mainOrderNo") String mainOrderNo,
                                                          @RequestParam("rejectionDesc") String rejectionDesc);

    /**
     * 查询待审核费用订单数量
     *
     * @return
     */
    @RequestMapping(value = "/api/auditPendingExpenses")
    public ApiResult<Integer> auditPendingExpenses(@RequestParam("subType") String subType,
                                                   @RequestParam("legalIds") List<Long> legalIds,
                                                   @RequestParam("orderNos") List<String> orderNos);

    /**
     * 查询待审核费用订单数量
     *
     * @return
     */
    @RequestMapping(value = "/api/auditPendingExpensesNum")
    public ApiResult<Integer> auditPendingExpensesNum(@RequestParam("subType") String subType,
                                                      @RequestParam("dataControl") DataControl dataControl,
                                                      @RequestParam("orderNos") List<String> orderNos);

    @ApiOperation("根据类型和业务类型查询订单id")
    @RequestMapping(value = "/api/getOrderAddressOrderIdByTimeInterval")
    public ApiResult<Set<Long>> getOrderAddressOrderIdByTimeInterval(@RequestParam("timeInterval") List<String> timeInterval,
                                                                     @RequestParam("type") Integer type,
                                                                     @RequestParam("businessType") Integer businessType);

    /**
     * 应收/应付费用状态
     *
     * @return
     */
    @RequestMapping(value = "/api/getCostStatus")
    public ApiResult<Map<String, Object>> getCostStatus(@RequestParam("mainOrderNos") List<String> mainOrderNos,
                                                        @RequestParam("orderNos") List<String> orderNos);

}
