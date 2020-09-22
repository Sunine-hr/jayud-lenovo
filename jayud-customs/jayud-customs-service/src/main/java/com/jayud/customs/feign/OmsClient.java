package com.jayud.customs.feign;


import com.jayud.common.ApiResult;

import com.jayud.customs.model.bo.OprOrderLogForm;
import com.jayud.customs.model.bo.InputOrderForm;
import com.jayud.customs.model.bo.OprStatusForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * customs模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {


    /**
     * 操作主订单
     * @return
     */
    @RequestMapping(value = "/api/oprMainOrder")
    ApiResult oprMainOrder(@RequestBody InputOrderForm form);

    /**
     * 记录操作日志
     */
    @RequestMapping(value = "/api/saveOprOrderLog")
    ApiResult saveOprOrderLog(@RequestBody List<OprOrderLogForm> forms);


    /**
     * 获取主订单信息
     */
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult getMainOrderById(@RequestParam(value = "idValue") Long idValue);

    /**
     * 获取根路径
     */
    @RequestMapping(value = "/api/getBaseUrl")
    ApiResult getBaseUrl();

    /**
     * 记录流程状态
     */
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form);

}
