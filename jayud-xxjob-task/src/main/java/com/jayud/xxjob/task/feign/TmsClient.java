package com.jayud.xxjob.task.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * oms模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {

    /**
     * 批量更新中港车实时定位GPS
     *
     * @return
     */
    @RequestMapping(value = "/api/batchSyncGPSTMSHistoryPositioning")
    public ApiResult batchSyncGPSTMSHistoryPositioning();

    /**
     * 批量更新中港车实时定位GPS
     *
     * @return
     */
    @RequestMapping(value = "/api/batchSyncTMSGPSPositioning")
    public ApiResult batchSyncTMSGPSPositioning();

}
