package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.AddCusAccountForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

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
    @RequestMapping("/api/findUserByKey")
    ApiResult findUserByKey(@RequestParam(value = "key") String key);

    /**
     * 获取法人主体
     * @return
     */
    @RequestMapping(value = "/api/findLegalEntity",method = RequestMethod.GET)
    ApiResult findLegalEntity();


    /**
     * 获取角色
     * @return
     */
    @RequestMapping("/api/findRole")
    ApiResult findRole();

    /**
     * 获取客户账户负责人
     * @return
     */
    @RequestMapping("/api/findCustAccount")
    ApiResult findCustAccount();

    /**
     * 删除客户账户
     * @return
     */
    @RequestMapping("/api/delCustAccount")
    ApiResult delCustAccount(@RequestParam(value = "id") String id);

    /**
     * 保存客户账户
     * @return
     */
    @RequestMapping(value = "/api/saveOrUpdateCustAccount")
    ApiResult saveOrUpdateCustAccount(@RequestBody AddCusAccountForm form);



}
