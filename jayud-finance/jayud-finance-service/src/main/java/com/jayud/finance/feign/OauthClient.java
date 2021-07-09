package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
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
    public ApiResult getLegalIdBySystemName(@RequestParam("loginName") String loginName);

    /**
     * 根据用户名获取用户所属法人主体
     */
    @RequestMapping(value = "/api/getLegalNameBySystemName")
    public ApiResult getLegalNameBySystemName(@RequestParam("loginName") String loginName);

    @ApiOperation("根据法人主体id集合查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityByLegalIds")
    public ApiResult getLegalEntityByLegalIds(@RequestParam("legalId") List<Long> legalIds);

    @ApiOperation("根据法人主体姓名查询法人主体信息")
    @RequestMapping(value = "/api/getAllLegalEntityByLegalName")
    public ApiResult getAllLegalEntityByLegalName(@RequestParam("name") String name);

    /**
     * 根据部门id获取部门名称
     *
     * @param departmentIds
     * @return
     */
    @RequestMapping(value = "/api/getDepartmentByDepartment")
    ApiResult getDepartmentByDepartment(@RequestParam("departmentIds") List<Long> departmentIds);

    /**
     * 获取接单部门
     *
     * @return
     */
    @RequestMapping(value = "/api/findDepartment")
    ApiResult<List<InitComboxVO>> findDepartment();
}
