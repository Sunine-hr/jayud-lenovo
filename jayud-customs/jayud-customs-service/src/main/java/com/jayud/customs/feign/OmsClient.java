package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.customs.model.bo.InputMainOrderForm;
import com.jayud.customs.model.bo.OprOrderLogForm;
import com.jayud.customs.model.bo.OprStatusForm;
import com.jayud.customs.model.vo.InputMainOrderVO;
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
    ApiResult oprMainOrder(@RequestBody InputMainOrderForm form);

    /**
     * 记录操作日志
     */
    @RequestMapping(value = "/api/saveOprOrderLog")
    ApiResult saveOprOrderLog(@RequestBody List<OprOrderLogForm> forms);


    /**
     * 获取主订单信息
     */
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult<InputMainOrderVO> getMainOrderById(@RequestParam(value = "id") Long id);

    /**
     * 获取主订单信息
     */
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult<Long> getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo);

    /**
     * 记录流程状态
     */
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form);

}
