package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.model.bo.AddCusAccountForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 获取登录用户信息
     */
    @PostMapping("/system/user/getLoginUser")
    ApiResult getLoginUser();

    /**
     * 获取接单部门
     * @return
     */
    @PostMapping("/oauth/api/findDepartment")
    ApiResult findDepartment();

    /**
     * 获取接单业务员和接单客服
     * @param roleId
     * @return
     */
    @PostMapping("system/user/findUserByRoleId")
    ApiResult findUserByRoleId(Long roleId);

    /**
     * 获取法人主体
     * @return
     */
    @PostMapping("system/user/findLegalEntity")
    ApiResult findLegalEntity();


    /**
     * 获取角色
     * @return
     */
    @PostMapping("system/user/findRole")
    ApiResult findRole();

    /**
     * 获取客户账户负责人
     * @return
     */
    @PostMapping("system/user/findCustAccount")
    ApiResult findCustAccount();

    /**
     * 删除客户账户
     * @return
     */
    @PostMapping("system/user/delCustAccount")
    ApiResult delCustAccount(Long id);

    /**
     * 保存客户账户
     * @return
     */
    @PostMapping("system/user/saveOrUpdateCustAccount")
    ApiResult saveOrUpdateCustAccount(AddCusAccountForm form);



}
