package com.jayud.receive;

import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author larry
 * @Date 2019/12/2
 * @Time 11:44s
 */
@FeignClient(value = "rtscf-platform-work",path = "/${msgUrl.platform:}")
public interface PlatformMsgClient {

    /**
     * 推送消息至金融平台
     * @param map
     * @return
     */
    @PostMapping("/business/push")
    ApiResult pushMsg(@RequestBody Map map);
}
