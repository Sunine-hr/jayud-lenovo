package com.jayud.oauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.oauth.model.bo.AddCusAccountForm;
import com.jayud.oauth.model.bo.OprSystemUserForm;
import com.jayud.oauth.model.bo.QueryAccountForm;
import com.jayud.oauth.model.enums.StatusEnum;
import com.jayud.oauth.model.enums.SystemUserStatusEnum;
import com.jayud.oauth.model.po.*;
import com.jayud.oauth.model.vo.*;
import com.jayud.oauth.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ISystemCompanyService systemCompanyService;

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
    @RequestMapping(value = "/api/findUserByKey")
    public ApiResult findUserByKey(@RequestParam(value = "key") String key) {
        Map<String, Object> param = new HashMap<>();
        param.put("key", key);
        SystemRole role = roleService.getRoleByCondition(param);
        param = new HashMap<>();
        if (role != null) {
            param.put("roleId", role.getId());
        }
        param.put("user_type", "1");
        param.put("status", "1");
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
    @RequestMapping(value = "/api/findLegalEntity")
    public ApiResult findLegalEntity() {
        Map<String, String> param = new HashMap<>();
        param.put(SqlConstant.AUDIT_STATUS, CommonConstant.VALUE_2);
        List<LegalEntityVO> legalEntitys = legalEntityService.findLegalEntity(param);
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
    @RequestMapping("/api/findRole")
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
    @RequestMapping("/api/findCustAccount")
    public ApiResult findCustAccount() {
        Map<String, Object> param = new HashMap<>();
        param.put("status", "1");
        List<SystemUser> systemUsers = userService.findUserByCondition(param);
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
    @RequestMapping(value = "/api/delCustAccount")
    public ApiResult delCustAccount(@RequestParam(value = "id") String id) {
        OprSystemUserForm form = new OprSystemUserForm();
        form.setCmd("delete");
        form.setId(Long.parseLong(id));
        userService.oprSystemUser(form);
        return ApiResult.ok();
    }

    @ApiOperation(value = "新增修改客户账户")
    @RequestMapping("/api/saveOrUpdateCustAccount")
    public ApiResult saveOrUpdateCustAccount(@RequestBody AddCusAccountForm form) {
        //校验登录名唯一性
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();

        if (form.getId() != null) {
            queryWrapper.lambda().select(SystemUser::getId).eq(SystemUser::getUserName, form.getUserName());
            SystemUser tmp = this.userService.getOne(queryWrapper);
            if (tmp != null && !tmp.getId().equals(form.getId())) {
                return ApiResult.error(ResultEnum.NAME_EXIST.getCode(), ResultEnum.NAME_EXIST.getMessage());
            }
        } else {
            queryWrapper.lambda().eq(SystemUser::getUserName, form.getUserName()).or().eq(SystemUser::getName, form.getName());
            if (this.userService.count(queryWrapper) > 0) {
                return ApiResult.error(ResultEnum.LOGIN_NAME_EXIST.getCode(), ResultEnum.LOGIN_NAME_EXIST.getMessage());
            }
        }


        SystemUser systemUser = new SystemUser();
        systemUser.setName(form.getName());
        systemUser.setUserName(form.getUserName());
        systemUser.setEnUserName(form.getEnUserName());
        systemUser.setSuperiorId(form.getDepartmentChargeId());
        systemUser.setCompanyId(form.getCompanyId());
        systemUser.setId(form.getId());
        systemUser.setPassword("E10ADC3949BA59ABBE56E057F20F883E");//默认密码为:123456
        systemUser.setStatus(SystemUserStatusEnum.ON.getCode());//账户为启用状态
        systemUser.setAuditStatus(StatusEnum.AUDIT_SUCCESS.getCode());
        systemUser.setUserType(form.getUserType());//用户类型
        userService.saveOrUpdateSystemUser(systemUser);//修改客户账户信息
        //删除旧的账户用户角色关系
        List<Long> userIds = new ArrayList<>();
        userIds.add(systemUser.getId());
        userRoleRelationService.removeRelationByUserId(userIds);
        //处理新增账户角色关系
        userRoleRelationService.createRelation(form.getRoleId(), systemUser.getId());
        return ApiResult.ok();
    }

    @ApiOperation(value = "根据主键集合获取系统用户信息")
    @RequestMapping("/api/getUsersByIds")
    public ApiResult<List<SystemUserVO>> getUsersByIds(@RequestParam("ids") List<Long> ids) {
        List<SystemUser> users = this.userService.getByIds(ids);
        List<SystemUserVO> vo = new ArrayList<>();
        users.stream().forEach(tmp -> {
            SystemUserVO systemUserVO = ConvertUtil.convert(tmp, SystemUserVO.class);
            systemUserVO.setPassword(null);
            vo.add(systemUserVO);
        });
        return new ApiResult(200, vo);
    }


    @ApiOperation(value = "获取公司信息")
    @RequestMapping("/api/getCompany")
    public ApiResult<List<InitComboxVO>> getCompany() {
        List<Company> list = this.systemCompanyService.list();
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (Company company : list) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(company.getId());
            initComboxVO.setName(company.getCompanyName());
            initComboxVOS.add(initComboxVO);
        }

        return new ApiResult(200, initComboxVOS);
    }


    @ApiOperation(value = "分页查询各个模块中账户管理(客户/供应商)")
    @RequestMapping("/api/findEachModuleAccountByPage")
    public ApiResult findEachModuleAccountByPage(@RequestBody QueryAccountForm form) {
        IPage<SystemUserVO> iPage = this.userService.findEachModuleAccountByPage(form);
        return ApiResult.ok(new CommonPageResult(iPage));
    }

    @ApiModelProperty(value = "启用/禁用用户")
    @RequestMapping("/api/enableOrDisableSupplierAccount")
    public ApiResult enableOrDisableSupplierAccount(@RequestParam(value = "id") Long id) {
        SystemUser user = this.userService.getById(id);
        SystemUser systemUser = new SystemUser();
        if (SystemUserStatusEnum.ON.getCode().equals(user.getStatus())) {
            systemUser.setId(id).setStatus(SystemUserStatusEnum.OFF.getCode());
        } else {
            systemUser.setId(id).setStatus(SystemUserStatusEnum.ON.getCode());
        }
        systemUser.setUpdatedTime(DateUtils.getNowTime()).setUpdatedUser(UserOperator.getToken());
        if (this.userService.updateById(systemUser)) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.SAVE_ERROR.getCode(), ResultEnum.SAVE_ERROR.getMessage());
        }

    }


    @ApiOperation("根据id获取各个模块中账户信息(客户/供应商)")
    @RequestMapping("/api/getEachModuleAccountById")
    public ApiResult getEachModuleAccountById(@RequestParam("id") Long id) {
        SystemUser systemUser = this.userService.getById(id);
        List<SystemRoleVO> roleList = this.roleService.getRoleList(systemUser.getId());
        SystemUserVO userVO = ConvertUtil.convert(systemUser, SystemUserVO.class);
        userVO.setPassword(null);
        //TODO 暂时角色是一对一
        if (!CollectionUtils.isEmpty(roleList)) {
            userVO.setRoleId(roleList.get(0).getId());
        }
        return ApiResult.ok(userVO);
    }

    @ApiOperation("根据登录名查询用户信息")
    @RequestMapping(value = "/api/getSystemUserByName")
    public ApiResult getSystemUserByName(@RequestParam("name") String name) {
        SystemUser systemUser = this.userService.selectByName(name);
        return ApiResult.ok(systemUser);
    }


    @ApiOperation("根据法人主体id查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityByLegalId")
    public ApiResult getLegalEntityByLegalId(@RequestParam("legalId") Long legalId) {
        LegalEntity legalEntity = this.legalEntityService.getById(legalId);
        return ApiResult.ok(legalEntity);
    }


    @ApiOperation("根据法人主体id集合查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityByLegalIds")
    public ApiResult<LegalEntity> getLegalEntityByLegalIds(@RequestParam("legalId") List<Long> legalIds) {
        List<LegalEntity> legalEntity = this.legalEntityService.getBaseMapper().selectBatchIds(legalIds);
        return ApiResult.ok(legalEntity);
    }


    @ApiOperation("根据部门名称查询部门id")
    @RequestMapping(value = "/api/getDeptIdByDeptName")
    public ApiResult getDeptIdByDeptName(@RequestParam("deptName") String deptName){
        Department byDeptName = departmentService.getByDeptName(deptName);
        if(byDeptName==null){
            return ApiResult.error();
        }
        return ApiResult.ok(byDeptName.getIdCode());
    }


    @ApiOperation("根据业务员名称查询业务员id")
    @RequestMapping(value = "/api/getSystemUserBySystemName")
    public ApiResult getSystemUserBySystemName(@RequestParam("name") String name){
        SystemUser systemUserBySystemName = userService.getSystemUserBySystemName(name);
        if(systemUserBySystemName==null){
            return ApiResult.error();
        }
        return ApiResult.ok(systemUserBySystemName.getId());
    }


    @ApiOperation("根据法人主体姓名查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityByLegalName")
    public ApiResult getLegalEntityByLegalName(@RequestParam("name") String name){
        LegalEntity legalEntityByLegalName = legalEntityService.getLegalEntityByLegalName(name);
        if(legalEntityByLegalName==null){
            return ApiResult.error();
        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "根据用户名获取用户所属法人主体")
    @PostMapping(value = "/getLegalIdBySystemName")
    public ApiResult getLegalIdBySystemName(@RequestParam("loginName") String loginName) {
        SystemUser systemUser = userService.getLoginUser(loginName);
        return null;
    }
}









    



