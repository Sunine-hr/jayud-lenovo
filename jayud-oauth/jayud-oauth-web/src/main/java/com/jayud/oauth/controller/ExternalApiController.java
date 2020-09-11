package com.jayud.oauth.controller;

import com.jayud.common.ApiResult;
import com.jayud.model.bo.AddCusAccountForm;
import com.jayud.model.bo.OprSystemUserForm;
import com.jayud.model.enums.StatusEnum;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.DepartmentVO;
import com.jayud.model.vo.InitComboxVO;
import com.jayud.model.vo.LegalEntityVO;
import com.jayud.model.vo.SystemRoleVO;
import com.jayud.oauth.service.*;
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

    @Autowired
    ISystemUserRoleRelationService userRoleRelationService;

    @ApiOperation(value = "获取当前登录用户")
    @PostMapping(value = "/api/getLoginUser")
    public ApiResult getLoginUser() {
        return new ApiResult(200, userService.getLoginUser().getName());
    }

    @ApiOperation(value = "获取所有部门")
    @RequestMapping(value = "/api/findDepartment")
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
    @PostMapping(value = "/api/findUserByRoleKey")
    public ApiResult findUserByRoleKey(String key) {
        Map<String, Object> param = new HashMap<>();
        param.put("key", key);
        SystemRole role = roleService.getRoleByCondition(param);
        param = new HashMap<>();
        param.put("roleId", role.getId());
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
    @PostMapping("/api/findLegalEntity")
    public ApiResult findLegalEntity() {
        List<LegalEntityVO> legalEntitys = legalEntityService.findLegalEntity(null);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (LegalEntityVO legalEntityVO : legalEntitys) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(legalEntityVO.getId());
            initComboxVO.setName(legalEntityVO.getLegalName());
            initComboxVOS.add(initComboxVO);
        }
        return new ApiResult(200, initComboxVOS);
    }

    @ApiOperation(value = "获取角色")
    @PostMapping("/api/findRole")
    public ApiResult findRole() {
        List<SystemRoleVO> systemRoleVOS = roleService.findRole();
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (SystemRoleVO systemRoleVO : systemRoleVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemRoleVO.getId());
            initComboxVO.setName(systemRoleVO.getName());
            initComboxVOS.add(initComboxVO);
        }
        return new ApiResult(200, initComboxVOS);
    }

    @ApiOperation(value = "获取客户账户负责人")
    @PostMapping("/api/findCustAccount")
    public ApiResult findCustAccount() {
        List<SystemUser> systemUsers = userService.findUserByCondition(null);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (SystemUser systemUser : systemUsers) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemUser.getId());
            initComboxVO.setName(systemUser.getUserName());
            initComboxVOS.add(initComboxVO);
        }
        return new ApiResult(200, initComboxVOS);
    }

    @ApiOperation(value = "删除客户账户")
    @PostMapping("/api/delCustAccount")
    public ApiResult delCustAccount(Long id) {
        OprSystemUserForm form = new OprSystemUserForm();
        form.setCmd("delete");
        form.setId(id);
        userService.oprSystemUser(form);
        return ApiResult.ok();
    }

    @ApiOperation(value = "新增修改客户账户")
    @PostMapping("/api/saveOrUpdateCustAccount")
    public ApiResult saveOrUpdateCustAccount(AddCusAccountForm form) {
        SystemUser systemUser = new SystemUser();
        systemUser.setName(form.getName());
        systemUser.setUserName(form.getUserName());
        systemUser.setEnUserName(form.getEnUserName());
        systemUser.setSuperiorId(form.getDepartmentChargeId());
        systemUser.setCompanyId(form.getCompanyId());
        systemUser.setId(form.getId());
        systemUser.setPassword("E10ADC3949BA59ABBE56E057F20F883E");//默认密码为:123456
        systemUser.setStatus(1);//账户为启用状态
        systemUser.setAuditStatus(StatusEnum.AUDIT_SUCCESS.getCode());
        systemUser.setUserType("2");//客户用户
        userService.saveOrUpdateSystemUser(systemUser);//修改客户账户信息
        //删除旧的账户用户角色关系
        List<Long> userIds = new ArrayList<>();
        userIds.add(systemUser.getId());
        userRoleRelationService.removeRelationByUserId(userIds);
        //处理新增账户角色关系
        userRoleRelationService.createRelation(form.getRoleId(),systemUser.getId());
        return ApiResult.ok();
    }


}









    



