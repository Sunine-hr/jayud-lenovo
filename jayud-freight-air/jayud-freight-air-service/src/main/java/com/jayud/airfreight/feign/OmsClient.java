package com.jayud.airfreight.feign;


import com.jayud.airfreight.model.bo.AirProcessOptForm;
import com.jayud.airfreight.model.bo.AuditInfoForm;
import com.jayud.airfreight.model.bo.InputOrderForm;
import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.entity.InitComboxVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * tms模块消费oms模块的接口
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
    ApiResult saveOprStatus(@RequestBody AirProcessOptForm form);

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
     * 根据客户id查询客户信息
     */
    @RequestMapping(value = "/api/getCustomerInfoById")
    ApiResult getCustomerInfoById(@RequestParam("id") Long id);


    /**
     * 暂存订单
     */
    @RequestMapping(value = "/api/holdOrder")
    ApiResult holdOrder(@RequestBody InputOrderForm form);

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
     * 根据编码获取港口名称
     */
    @RequestMapping(value = "/api/getPortCodeByName")
    public ApiResult getPortCodeByName(@RequestBody String name);

    /**
     * 根据orderId和类型删除物流轨迹跟踪表
     */
    @RequestMapping(value = "/api/deleteLogisticsTrackByType")
    ApiResult deleteLogisticsTrackByType(@RequestParam("orderId") Long orderId, @RequestParam("type") Integer type);
}
