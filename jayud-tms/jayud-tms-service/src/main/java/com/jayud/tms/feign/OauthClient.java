package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.InitComboxVO;
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

    /**
     * 根据部门id获取部门名称
     *
     * @param departmentId
     * @return
     */
    @RequestMapping(value = "/api/getDepartmentNameById")
    ApiResult getDepartmentNameById(@RequestParam("departmentId") Long departmentId);

    /**
     * 获取接单部门
     *
     * @return
     */
    @RequestMapping(value = "/api/findDepartment")
    ApiResult<List<InitComboxVO>> findDepartment();
}
