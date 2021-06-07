package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.entity.DataControl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 根据用户名获取用户所属法人主体
     */
    @RequestMapping(value = "/api/getLegalIdBySystemName")
    public ApiResult<List<Long>> getLegalIdBySystemName(@RequestParam("loginName") String loginName);

    /**
     * 根据用户名获取用户所属数据权限
     */
    @RequestMapping(value = "/api/getDataPermission")
    public ApiResult<DataControl> getDataPermission(@RequestParam("loginName") String loginName,
                                                    @RequestParam(value = "UserType") String userType);

}
