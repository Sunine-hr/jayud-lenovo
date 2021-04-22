package com.jayud.storage.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * sea模块消费oms模块的接口
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

//    /**
//     * 只记录成功操作流程状态
//     */
//    @RequestMapping(value = "/api/saveOprStatus")
//    ApiResult saveOprStatus(@RequestBody TrailerProcessOptForm form);
//
//    /**
//     * 记录审核信息
//     *
//     * @param form
//     * @return
//     */
//    @RequestMapping(value = "/api/saveAuditInfo")
//    ApiResult saveAuditInfo(@RequestBody AuditInfoForm form);

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

//    /**
//     * 批量保存/修改商品信息
//     */
//    @RequestMapping(value = "/api/saveOrUpdateGoodsBatch")
//    public ApiResult saveOrUpdateGoodsBatch(@RequestBody List<AddGoodsForm> goodsForms);
//
//    /**
//     * 批量保存/修改订单地址信息
//     */
//    @RequestMapping(value = "/api/saveOrUpdateOrderAddressBatch")
//    public ApiResult saveOrUpdateOrderAddressBatch(@RequestBody List<AddOrderAddressForm> forms);
//
//    /**
//     * 根据业务id集合查询订单地址
//     */
//    @RequestMapping(value = "/api/getOrderAddressByBusIds")
//    public ApiResult<List<OrderAddressVO>> getOrderAddressByBusIds(@RequestParam("orderId") List<Long> orderId,
//                                                                   @RequestParam("businessType") Integer businessType);
//
//    /**
//     * 根据订单id集合查询商品信息
//     */
//    @RequestMapping(value = "/api/getGoodsByBusIds")
//    public ApiResult<List<GoodsVO>> getGoodsByBusIds(@RequestParam("orderId") List<Long> orderId,
//                                                     @RequestParam("businessType") Integer businessType);


    /**
     * 根据供应商id集合查询供应商信息
     * @return
     */
    @RequestMapping(value = "/api/getSupplierInfoByIds")
    ApiResult getSupplierInfoByIds(@RequestParam("supplierIds") List<Long> supplierIds);

    /**
     * 获取柜车大小类型
     */
    @RequestMapping(value = "/api/getVehicleSizeInfo")
    ApiResult getVehicleSizeInfo();

    /**
     * 获取附件集合
     */
    @RequestMapping(value = "/api/getTrailerAttachments")
    ApiResult getTrailerAttachments(@RequestParam("orderId") Long orderId);

    /**
     * 根据客户code集合查询客户信息
     * @return
     */
    @RequestMapping(value = "/api/getCustomerByUnitCode")
    ApiResult getCustomerByUnitCode(@RequestBody List<String> unitCodes);

    /**
     * 获取订单号
     * @return
     */
    @RequestMapping(value = "/api/getOrderNo")
    ApiResult getOrderNo(@RequestParam("preOrder") String preOrder , @RequestParam("classCode") String classCode);

    /**
     * 获取订单号
     * @return
     */
    @RequestMapping(value = "/api/getWarehouseNumber")
    ApiResult getWarehouseNumber(@RequestParam("preOrder") String preOrder);

//    /**
//     * 批量新增/修改订单流程
//     *
//     * @return
//     */
//    @RequestMapping(value = "/api/batchAddOrUpdateProcess")
//    public ApiResult batchAddOrUpdateProcess(@RequestBody List<OrderFlowSheet> orderFlowSheets);

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
     * 根据字典类型下拉选项字典
     */
    @RequestMapping(value = "/api/initDictByDictTypeCode")
    public ApiResult<List<InitComboxStrVO>> initDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode);

    @ApiOperation(value = "初始化车辆下拉框")
    @RequestMapping(value = "api/initVehicle")
    public ApiResult initVehicle(@RequestParam("type") Integer type);

//    @ApiOperation(value = "初始化车辆下拉框 (确认派车)")
//    @RequestMapping(value = "api/initVehicleInfo")
//    public ApiResult<VehicleInfoLinkVO> initVehicleInfo(@RequestParam("vehicleId") Long vehicleId);
//
//    @ApiOperation(value = "单个存储商品信息")
//    @RequestMapping(value = "api/saveOrUpdateGood")
//    ApiResult saveOrUpdateGood(@RequestBody AddGoodsForm goodsForm);

    @ApiOperation(value = "单个商品信息获取")
    @RequestMapping(value = "api/getGoodById")
    ApiResult getGoodById(@RequestParam("id") Long id);

    @ApiOperation(value = "主订单驳回标识操作")
    @RequestMapping(value = "/api/doMainOrderRejectionSignOpt")
    public ApiResult<Boolean> doMainOrderRejectionSignOpt(@RequestParam("mainOrderNo") String mainOrderNo,
                                                          @RequestParam("rejectionDesc") String rejectionDesc);

    @ApiOperation(value = "查询联系人信息")
    @RequestMapping(value = "/getContactInfoByPhone")
    public CommonResult<List<Map<String, Object>>> getContactInfoByPhone(@RequestParam("businessType") Integer businessType);
}
