package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.InputOrderTransportForm;
import com.jayud.oms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.oms.model.bo.TmsChangeStatusForm;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputOrderTransportVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult<InputOrderTransportVO> getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取中港订单号
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getTransportOrderNo")
    ApiResult<InitChangeStatusVO> getTransportOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);


    /**
     * 更改报关单状态
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
     * PDF派车单
     * @return
     */
    @RequestMapping(value = "/api/dispatchList")
    ApiResult dispatchList(@RequestParam(value = "orderNo") String orderNo);

    /**
     * 根据主键查询司机的中港订单信息
     */
    @RequestMapping(value = "/api/getDriverOrderTransportById")
    ApiResult getDriverOrderTransportById(@RequestParam("orderId") Long orderId);

}
