package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 获取登录用户信息
     */
    @GetMapping("/system/user/getLoginUser")
    ApiResult getLoginUser();

    /**
     * 获取接单部门
     * @return
     */
    @GetMapping("system/user/findJieDanDepartment")
    ApiResult findDepartment();

    /**
     * 获取接单业务员和接单客服
     * @param roleId
     * @return
     */
    @GetMapping("system/user/findUserByRoleId")
    ApiResult findUserByRoleId(Long roleId);

    /**
     * 获取法人主体
     * @return
     */
    @GetMapping("system/user/findLegalEntity")
    ApiResult findLegalEntity();




}
