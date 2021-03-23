package com.jayud.oauth.feign;


import com.jayud.common.ApiResult;
import com.jayud.oauth.model.po.SystemMenu;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * oms模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {

    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<SystemMenu> menusList);
}
