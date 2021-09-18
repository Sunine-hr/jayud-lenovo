package com.jayud.xxjob.task.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * tms模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

    /**
     * 消息推送
     *
     * @return
     */
    @RequestMapping(value = "/api/channelMsgPush")
    public ApiResult channelMsgPush();
}
