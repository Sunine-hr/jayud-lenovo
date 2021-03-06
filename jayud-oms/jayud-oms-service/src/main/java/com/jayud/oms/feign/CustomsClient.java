package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.bo.CustomsChangeStatusForm;
import com.jayud.oms.model.bo.InputOrderCustomsForm;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputOrderCustomsVO;
import com.jayud.oms.model.vo.InputSubOrderCustomsVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {

    /**
     * 获取报关单数量
     */
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult<Integer> getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取报关订单详情
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getCustomsDetail")
    ApiResult<InputOrderCustomsVO> getCustomsDetail(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 创建报关单
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/createOrderCustoms")
    ApiResult<Boolean> createOrderCustoms(@RequestBody InputOrderCustomsForm form);

    /**
     * 获取报关订单单号
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/findCustomsOrderNo")
    ApiResult<List<InitChangeStatusVO>> findCustomsOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);


    /**
     * 更改报关单状态
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeCustomsStatus(@RequestBody List<CustomsChangeStatusForm> form);

    /**
     * 根据主订单集合查询所有报关信息
     */
    @RequestMapping(value = "/api/getCustomsOrderByMainOrderNos")
    ApiResult getCustomsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);


    /**
     * 根据主订单查询六联单号附件
     */
    @RequestMapping(value = "/api/getEncodePicByMainOrderNo")
    public ApiResult<List<FileView>> getEncodePicByMainOrderNo(@RequestParam("mainOrderNos") String mainOrderNo);


    @ApiModelProperty(value = "获取所用通过放行审核主订单号")
    @RequestMapping(value = "/api/getAllPassByMainOrderNos")
    public ApiResult<List<String>> getAllPassByMainOrderNos(@RequestParam("mainOrders") List<String> mainOrders);

    /**
     * 根据云报关单号查询订单号
     */
    @RequestMapping(value = "/api/getOrderNoByYunCustomsNo")
    public ApiResult<InputSubOrderCustomsVO> getOrderNoByYunCustomsNo(@RequestParam("yunCustomsNo") String yunCustomsNo);
}
