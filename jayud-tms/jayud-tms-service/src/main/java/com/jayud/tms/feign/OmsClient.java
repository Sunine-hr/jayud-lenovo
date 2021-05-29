package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.tms.model.bo.AuditInfoForm;
import com.jayud.tms.model.bo.HandleSubProcessForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.vo.InitComboxVO;
import com.jayud.tms.model.vo.OrderStatusVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * tms模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

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
    ApiResult<List<InitComboxVO>> initSupplierInfo();

    /**
     * 初始化中转仓库
     *
     * @return
     */
    @RequestMapping(value = "/api/initWarehouseInfo")
    ApiResult<List<InitComboxVO>> initWarehouseInfo();

    /**
     * 删除前面操作成功的记录
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/api/delOprStatus")
    ApiResult delOprStatus(@RequestParam("orderId") Long orderId);

    /**
     * 根据orderId和类型删除物流轨迹跟踪表
     */
    @RequestMapping(value = "/api/deleteLogisticsTrackByType")
    ApiResult deleteLogisticsTrackByType(@RequestParam("orderId") Long orderId, @RequestParam("type") Integer type);

    /**
     * 删除特定单的操作流程
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/delSpecOprStatus")
    ApiResult delSpecOprStatus(@RequestBody DelOprStatusForm form);

    /**
     * 子订单流程
     *
     * @return
     */
    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult<List<OrderStatusVO>> handleSubProcess(@RequestBody HandleSubProcessForm form);


    /**
     * 初始化司机下拉框
     *
     * @return
     */
    @RequestMapping(value = "/api/initDriver")
    ApiResult<List<InitComboxVO>> initDriver();


    /**
     * 初始化车辆下拉框
     */
    @RequestMapping(value = "api/initVehicle")
    ApiResult<List<InitComboxVO>> initVehicle(@RequestParam("type") Integer type);

//    /**
//     * 司机下拉框联动车辆供应商，大陆车牌，香港车牌，司机电话
//     *
//     * @return
//     */
//    @RequestMapping(value = "/api/initDriverInfo")
//    ApiResult<DriverInfoLinkVO> initDriverInfo(@RequestParam("driverId") Long driverId);


    @ApiOperation(value = "初始化车辆下拉框")
    /**
     * 陆车牌下拉框联动车辆供应商，大陆车牌，香港车牌，司机电话
     */
    @RequestMapping(value = "api/initVehicleInfo")
    public ApiResult initVehicleInfo(@RequestParam("vehicleId") Long vehicleId);


    /**
     * 根据车辆主键查询车辆信息
     */
    @RequestMapping(value = "/api/getVehicleInfoById")
    ApiResult getVehicleInfoById(@RequestParam("vehicleId") Long vehicleId);

    /**
     * 根据主订单号查询法人主体信息
     */
    @RequestMapping(value = "/api/getLegalEntityInfoByOrderNo")
    ApiResult getLegalEntityInfoByOrderNo(@RequestParam("mainOrderNo") String mainOrderNo);

    /**
     * 是否是虚拟仓
     *
     * @param warehouseInfoId
     * @return
     */
    @RequestMapping(value = "/api/isVirtualWarehouse")
    ApiResult<Boolean> isVirtualWarehouse(@RequestParam("warehouseInfoId") Long warehouseInfoId);

    /**
     * 通关前审核/中港通关前复核页面详情
     *
     * @return
     */
    @RequestMapping(value = "/api/initGoCustomsAudit")
    public ApiResult initGoCustomsAudit(@RequestParam("mainOrderNo") String mainOrderNo);

    /**
     * 根据车辆id获取车辆和供应商信息
     */
    @RequestMapping(value = "api/getVehicleAndSupplierInfo")
    public ApiResult getVehicleAndSupplierInfo(@RequestParam("vehicleId") Long vehicleId);


    @ApiOperation(value = "根据司机id查询司机信息")
    @RequestMapping(value = "/api/getDriverById")
    public ApiResult getDriverById(@RequestParam("driverId") Long driverId);

    /**
     * 根据客户code集合查询客户信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getCustomerByUnitCode")
    ApiResult getCustomerByUnitCode(@RequestBody List<String> unitCodes);

    @ApiOperation(value = "主订单驳回标识操作")
    @RequestMapping(value = "/api/doMainOrderRejectionSignOpt")
    public ApiResult<Boolean> doMainOrderRejectionSignOpt(@RequestParam("mainOrderNo") String mainOrderNo,
                                                          @RequestParam("rejectionDesc") String rejectionDesc);

    /**
     * 根据主订单集合查询主订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByOrderNos")
    ApiResult getMainOrderByOrderNos(@RequestParam("orderNos") List<String> orderNos);

    /**
     * 根据车辆id查询车辆信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getVehicleInfoByIds")
    public ApiResult getVehicleInfoByIds(@RequestParam("orderIds") List<Long> vehicleIds);


    /**
     * 根据司机ids查询司机信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getDriverInfoByIds")
    public ApiResult getDriverInfoByIds(@RequestParam("driverIds") List<Long> driverIds);

    /**
     * 根据中转仓库ids查询中转仓库信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getWarehouseInfoByIds")
    public ApiResult getWarehouseInfoByIds(@RequestParam("warehouseIds") List<Long> warehouseIds);

    /**
     * 根据子订单id查询所有子订单物流轨迹
     */
    @RequestMapping(value = "/api/getLogisticsTrackBySubId")
    public ApiResult getLogisticsTrackByType(@RequestParam("subOrderIds") List<Long> subOrderIds,
                                             @RequestParam("type") Integer type);


    /**
     * 是否录用费用
     *
     * @return
     */
    @RequestMapping(value = "/api/isCost")
    public ApiResult<Map<String, Object>> isCost(@RequestBody List<String> orderNos,
                                                 @RequestParam("subType") String subType);

    /**
     * 应收/应付费用状态
     *
     * @return
     */
    @RequestMapping(value = "/api/getCostStatus")
    public ApiResult<Map<String, Object>> getCostStatus(@RequestParam("mainOrderNos") List<String> mainOrderNos,
                                                        @RequestParam("orderNos") List<String> orderNos);


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
     * 根据id集合获取中转仓库
     */
    @RequestMapping(value = "api/getWarehouseMapByIds")
    public ApiResult<Map<Long, Map<String, Object>>> getWarehouseMapByIds(@RequestParam("warehouseIds") List<Long> warehouseIds);


    /**
     * 根据编码获取港口名称
     */
    @RequestMapping(value = "/api/getPortNameByCode")
    public ApiResult<String> getPortNameByCode(@RequestBody String code);
}
