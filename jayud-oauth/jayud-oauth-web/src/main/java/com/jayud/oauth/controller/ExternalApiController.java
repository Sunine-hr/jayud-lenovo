package com.jayud.oauth.controller;

import com.jayud.common.ApiResult;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.DepartmentVO;
import com.jayud.model.vo.InitComboxVO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/oauth/api")
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

    @ApiOperation(value = "获取所有部门")
    @PostMapping(value = "/findDepartment")
    public ApiResult findDepartment() {
        List<DepartmentVO> departmentVOS = departmentService.findDepartment(null);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (DepartmentVO department : departmentVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(department.getId());
            initComboxVO.setName(department.getName());
            initComboxVOS.add(initComboxVO);
        }
        return new ApiResult(200, initComboxVOS);
    }

    @ApiOperation(value = "根据角色KEY获取用户")
    @PostMapping(value = "/findUserByRoleKey")
    public ApiResult findUserByRoleKey(String key) {
        Map<String,Object> param = new HashMap<>();
        param.put("key",key);
        SystemRole role = roleService.getRoleByCondition(param);
        param = new HashMap<>();
        param.put("roleId",role.getId());
        List<SystemUser> systemUsers = userService.findUserByCondition(param);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (SystemUser systemUser : systemUsers) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemUser.getId());
            initComboxVO.setName(systemUser.getName());
            initComboxVOS.add(initComboxVO);
        }
        return new ApiResult(200, initComboxVOS);
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

