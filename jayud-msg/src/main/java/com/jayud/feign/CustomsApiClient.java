package com.jayud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 云报关到金蝶推送日志
 *
 * @author fachang.mao
 * @description
 * @Date: 2020-12-22 11:23
 */
@FeignClient("jayud-customs-api")
public interface CustomsApiClient {

    /**
     * 保存或更新-云报关到金蝶推送日志
     * @param msg
     * @return
     */
    @RequestMapping(path = "/yunbaoguanKingdeePushLog/saveOrOpdateLog", method = RequestMethod.POST)
    Boolean saveOrOpdateLog(@RequestBody String msg);

}
