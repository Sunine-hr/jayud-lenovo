package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.model.bo.AddCusAccountForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "jayud-oauth-web",path = "/jayud-oauth")
public interface OauthClient {

    /**
     * 获取登录用户信息
     */
    @PostMapping("/api/getLoginUser")
    ApiResult getLoginUser();

    /**
     * 获取接单部门
     * @return
     */
    @RequestMapping(value = "/api/findDepartment")
    ApiResult findDepartment();

    /**
     * 获取接单业务员和接单客服
     * @param key
     * @return
     */
    @PostMapping("/api/findUserByRoleId")
    ApiResult findUserByKey(String key);

    /**
     * 获取法人主体
     * @return
     */
    @PostMapping("/api/findLegalEntity")
    ApiResult findLegalEntity();


    /**
     * 获取角色
     * @return
     */
    @PostMapping("/api/findRole")
    ApiResult findRole();

    /**
     * 获取客户账户负责人
     * @return
     */
    @PostMapping("/api/findCustAccount")
    ApiResult findCustAccount();

    /**
     * 删除客户账户
     * @return
     */
    @PostMapping("/api/delCustAccount")
    ApiResult delCustAccount(Long id);

    /**
     * 保存客户账户
     * @return
     */
    @PostMapping("/api/saveOrUpdateCustAccount")
    ApiResult saveOrUpdateCustAccount(AddCusAccountForm form);



}
