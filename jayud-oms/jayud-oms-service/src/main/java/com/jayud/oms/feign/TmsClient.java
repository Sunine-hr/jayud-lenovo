package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.DriverFeedbackStatusForm;
import com.jayud.oms.model.bo.InputOrderTransportForm;
import com.jayud.oms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.oms.model.bo.TmsChangeStatusForm;
import com.jayud.oms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputOrderTransportVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {

    /**
     * 创建中港订单
     */
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult<Boolean> createOrderTransport(@RequestBody InputOrderTransportForm form);


    /**
     * 获取中港订单详情
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult<InputOrderTransportVO> getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取中港订单号
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getTransportOrderNo")
    ApiResult<InitChangeStatusVO> getTransportOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);


    /**
     * 更改报关单状态
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeTransportStatus(@RequestBody List<TmsChangeStatusForm> form);


    /**
     * 分页查询司机的中港订单信息
     */
    @RequestMapping(value = "/api/getDriverOrderTransport")
    ApiResult getDriverOrderTransport(@RequestBody QueryDriverOrderTransportForm form);

    /**
     * 根据中港订单号查询中港订单信息
     */
    @RequestMapping(value = "/api/getTmsOrderByOrderNo")
    public ApiResult getTmsOrderByOrderNo(@RequestParam("orderNo") String orderNo);

    /**
     * PDF派车单
     *
     * @return
     */
    @RequestMapping(value = "/api/dispatchList")
    ApiResult dispatchList(@RequestParam(value = "orderNo") String orderNo);

    /**
     * 根据主键查询司机的中港订单信息
     */
    @RequestMapping(value = "/api/getDriverOrderTransportById")
    ApiResult getDriverOrderTransportById(@RequestParam("orderId") Long orderId);


    /**
     * 获取司机待接单数量（小程序）
     */
    @RequestMapping(value = "/api/getDriverPendingOrderNum")
    ApiResult getDriverOrderTransportDetailById(@RequestParam("driverId") Long driverId
            , @RequestParam("orderNos") List<String> orderNos);

    /**
     * 查询送货地址数量
     */
    @RequestMapping(value = "/api/getDeliveryAddressNum")
    ApiResult getDeliveryAddressNum(@RequestParam("orderNo") String orderNo);

    /**
     * 获取中港订单状态
     */
    @RequestMapping(value = "/api/getOrderTransportStatus")
    ApiResult getOrderTransportStatus(@RequestParam("orderNo") String orderNo);

    /**
     * 司机反馈状态
     */
    @RequestMapping(value = "/api/doDriverFeedbackStatus")
    ApiResult doDriverFeedbackStatus(@RequestBody DriverFeedbackStatusForm form);


    /**
     * 查询提货/收货地址
     */
    @RequestMapping(value = "/api/getDriverOrderTakeAdrByOrderNo")
    public ApiResult<List<DriverOrderTakeAdrVO>> getDriverOrderTakeAdrByOrderNo(@RequestParam("orderNo") List<String> orderNo
            , @RequestParam("oprType") Integer oprType);

    /**
     * 查询派车信息
     */
    @RequestMapping(value = "/api/getOrderSendCarsByOrderNo")
    public ApiResult getOrderSendCarsByOrderNo(@RequestParam("orderNo") String orderNo);

    /**
     * 根据主订单号集合查询中港信息
     */
    @RequestMapping(value = "/api/getTmsOrderByMainOrderNos")
    public ApiResult getTmsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);


    @ApiModelProperty(value = "根据订单号获取送货地址信息(下拉选择)")
    @RequestMapping(value = "/api/initTakeAdrBySubOrderNo")
    public ApiResult initTakeAdrBySubOrderNo(@RequestParam("subOrderNo") String subOrderNo);

    /**
     * 通关前审核前置条件
     */
    @RequestMapping(value = "/api/preconditionsGoCustomsAudit")
    public ApiResult preconditionsGoCustomsAudit();


    /**
     * 根据主订单号集合查询中港详情信息
     */
    @RequestMapping(value = "/api/getTmsOrderInfoByMainOrderNos")
    public ApiResult getTmsOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);

    /**
     * 根据单个主订单获取子订单详情
     */
    @RequestMapping(value = "/api/getInfoByMainOrderNo")
    public ApiResult getInfoByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo);
}
