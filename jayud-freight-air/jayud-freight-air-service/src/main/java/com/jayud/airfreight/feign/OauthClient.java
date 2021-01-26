package com.jayud.airfreight.feign;


import com.jayud.common.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 根据登录名查询用户信息
     */
    @RequestMapping(value = "/api/getSystemUserByName")
    ApiResult getSystemUserByName(@RequestParam("name") String name);

    @ApiOperation("根据法人主体id集合查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityByLegalIds")
    public ApiResult getLegalEntityByLegalIds(@RequestParam("legalId") List<Long> legalIds);

    /**
     * 根据用户名获取用户所属法人主体
     */
    @RequestMapping(value = "/api/getLegalIdBySystemName")
    public ApiResult getLegalIdBySystemName(@RequestParam("loginName") String loginName) ;
}
