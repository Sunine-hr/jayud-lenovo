package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.model.bo.OprOrderLogForm;
import com.jayud.model.bo.InputOrderForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * customs模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web",path = "/")
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

}
