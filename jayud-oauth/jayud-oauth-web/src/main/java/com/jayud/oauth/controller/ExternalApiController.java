package com.jayud.oauth.controller;

import com.jayud.common.ApiResult;
import com.jayud.oauth.service.ILegalEntityService;
import com.jayud.oauth.service.ISystemDepartmentService;
import com.jayud.oauth.service.ISystemRoleService;
import com.jayud.oauth.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/user")
@Api(tags = "oauth对外接口")
public class ExternalApiController {

    @Autowired
    ISystemUserService userService;

    @Autowired
    ISystemDepartmentService departmentService;

    @Autowired
    ILegalEntityService legalEntityService;

    @Autowired
    ISystemRoleService roleService;

    @ApiOperation(value = "获取当前登录用户")
    @PostMapping(value = "/getLoginUser")
    public ApiResult getLoginUser() {
        return new ApiResult(200, userService.getLoginUser().getName());
    }

    @ApiOperation(value = "获取所有接单部门")
    @PostMapping(value = "/findDepartment")
    public ApiResult findJieDanDepartment() {
        return new ApiResult(200, departmentService.findDepartment(null));
    }

    @ApiOperation(value = "根据角色获取用户")
    @PostMapping(value = "/findUserByRoleId")
    public ApiResult findUserByRoleId(Long roleId) {
        return new ApiResult(200, userService.findUserByRoleId(roleId));
    }

    @ApiOperation(value = "获取法人主体")
    @PostMapping("/findLegalEntity")
    public ApiResult findLegalEntity() {
        return new ApiResult(200, legalEntityService.findLegalEntity(null));
    }

    @ApiOperation(value = "获取角色")
    @PostMapping("/findRole")
    public ApiResult findRole() {
        return new ApiResult(200, roleService.findRole());
    }

    @ApiOperation(value = "获取客户账户负责人")
    @PostMapping("findCustAccount")
    public ApiResult findCustAccount() {
        return new ApiResult(200, userService.findUserByCondition(null));
    }










    


}

