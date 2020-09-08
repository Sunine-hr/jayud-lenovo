package com.jayud.oauth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.*;
import com.jayud.model.po.LegalEntity;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.*;
import com.jayud.oauth.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/system/user")
@Api(tags = "用户管理")
public class SystemUserController {

    @Autowired
    ISystemUserService userService;

    @Autowired
    ISystemMenuService menuService;

    @Autowired
    ISystemRoleMenuRelationService roleMenuRelationService;

    @Autowired
    ISystemRoleService roleService;

    @Autowired
    ISystemDepartmentService departmentService;

    @Autowired
    ILegalEntityService legalEntityService;

    @Autowired
    ISystemWorkService workService;

    /**
     * 登录接口
     * @param loginForm
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping(value = "/login")
    public CommonResult<SystemUserVO> login(@Valid @RequestBody SystemUserLoginForm loginForm) {
        //构造登录数据
        UserLoginToken token = new UserLoginToken();
        //登录名
        token.setUsername(loginForm.getUsername());
        //密码
        token.setPassword(loginForm.getPassword().toCharArray());
        //保持登录
        token.setRememberMe(loginForm.getKeepLogin());

        //该系统暂没有图验证

        //登录逻辑
        SystemUserVO userVO = userService.login(token);

        return CommonResult.success(userVO);
    }

    /**
     * 登出接口
     * @return
     */
    @ApiOperation(value = "登出接口")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        //登出
        userService.logout();
        return CommonResult.success();
    }

    @ApiOperation(value = "角色权限管理-新增数据初始化")
    @PostMapping(value = "/findAllMenuNode")
    public CommonResult<List<SystemMenuNode>> findAllMenuNode() {
        List<SystemMenuNode> systemMenuNodes = menuService.findAllMenuNode();
        return CommonResult.success(systemMenuNodes);
    }

    @ApiOperation(value = "角色权限管理-新增确认")
    @PostMapping(value = "/addRole")
    public CommonResult addRole(@RequestBody AddRoleForm addRoleForm){
        SystemRole systemRole = ConvertUtil.convert(addRoleForm, SystemRole.class);
        List<Long> menuIds = addRoleForm.getMenuIds();
        roleService.saveRole(systemRole);
        systemRole.setId(systemRole.getId());
        roleMenuRelationService.createRelation(systemRole,menuIds);
        return CommonResult.success();
    }

    /**
     * 账户管理模块
     */
    @ApiOperation(value = "账户管理和人员审核（总经办和组织架构界面-点击部门初始员工信息")
    @PostMapping("/list")
    public CommonResult<CommonPageResult<SystemUserVO>> list(@RequestBody QuerySystemUserForm form) {

        IPage<SystemUserVO> pageList = userService.getPageList(form);

        CommonPageResult<SystemUserVO> pageVO = new CommonPageResult(pageList);


        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "账户管理-新增修改数据初始化")
    @PostMapping(value = "/getAccountSystemUser")
    public CommonResult<UpdateSystemUserVO> getAccountSystemUser(Long id) {
        UpdateSystemUserVO systemUserVO = userService.getSystemUser(id);
        return CommonResult.success(systemUserVO);
    }

    @ApiOperation(value = "账户管理-删除/修改/新增")
    @PostMapping(value = "/oprAccountSystemUser")
    public CommonResult oprAccountSystemUser(@RequestBody OprSystemUserForm form) {
        userService.oprSystemUser(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "人员审核(总经办)-审核")
    @PostMapping(value = "/auditAccountSystemUser")
    public CommonResult auditAccountSystemUser(@RequestBody AuditSystemUserForm form) {
        userService.auditSystemUser(form);
        return CommonResult.success();
    }

    /**
     * 组织架构模块
     */
    @ApiOperation(value = "组织架构界面-初始化部门，fId为0时获取的是一级部门,点击一级部门传一级部门的ID,获取的是子部门")
    @PostMapping(value = "/findOrgStructure")
    public CommonResult<List<QueryOrgStructureVO>> findOrgStructure(Long fId) {
        List<QueryOrgStructureVO> orgStructures = userService.findOrgStructure(fId);
        return CommonResult.success(orgStructures);
    }

    @ApiOperation(value = "组织架构界面-初始化负责人信息,departmentId传的是部门ID")
    @PostMapping(value = "/findOrgStructureCharge")
    public CommonResult<List<DepartmentChargeVO>> findOrgStructureCharge(Long departmentId) {
        List<DepartmentChargeVO> departmentChargeVOS = userService.findOrgStructureCharge(departmentId);
        return CommonResult.success(departmentChargeVOS);
    }

    @ApiOperation(value = "组织架构界面-新增部门/编辑,departmentId编辑时传")
    @PostMapping(value = "/saveOrUpdateDepartment")
    public CommonResult saveOrUpdateDepartment(Long departmentId,@RequestBody AddDepartmentForm form) {
        departmentService.saveOrUpdateDepartment(departmentId,form);
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除部门,departmentId传的是部门ID")
    @PostMapping(value = "/delDepartment")
    public CommonResult delDepartment(Long departmentId) {
        departmentService.removeById(departmentId);
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除员工")
    @PostMapping(value = "/delSystemUser")
    public CommonResult delSystemUser(@RequestBody DelSystemUserForm form) {
        userService.removeByIds(form.getUserIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-新增员工/编辑")
    @PostMapping(value = "/saveOrUpdatedSystemUser")
    public CommonResult saveOrUpdatedSystemUser(@RequestBody AddSystemUserForm form) {
        SystemUser systemUser = ConvertUtil.convert(form,SystemUser.class);
        String loginUser = getLoginName();
        if(form.getId() != null){
            systemUser.setUpdatedUser(loginUser);
        }else {
            systemUser.setStatus(0);
            systemUser.setCreatedUser(loginUser);
        }
        userService.saveOrUpdateSystemUser(systemUser);
        return CommonResult.success();
    }

    /**
     * 法人主体模块
     */
    @ApiOperation(value = "法人主体-界面")
    @PostMapping(value = "/findLegalEntityByPage")
    public CommonResult<CommonPageResult<LegalEntityVO>> findLegalEntityByPage(@RequestBody QueryLegalEntityForm form) {
        IPage<LegalEntityVO> pageList = legalEntityService.findLegalEntityByPage(form);
        CommonPageResult<LegalEntityVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "法人主体-新增/修改")
    @PostMapping(value = "/saveOrUpdateLegalEntity")
    public CommonResult saveOrUpdateLegalEntity(@RequestBody AddLegalEntityForm form) {
        LegalEntity legalEntity = new LegalEntity();
        if(form.getId() != null){
            legalEntity.setId(form.getId());
            legalEntity.setUpdatedUser(getLoginName());
        }else {
            legalEntity.setAuditStatus(1L);
            legalEntity.setCreatedUser(getLoginName());
        }
        legalEntity.setLegalName(form.getLegalName());
        legalEntity.setLegalCode(form.getLegalCode());
        legalEntity.setRigisAddress(form.getRigisAddress());
        legalEntity.setSaleDepartId(form.getSaleDepartId());
        legalEntityService.saveOrUpdate(legalEntity);
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-删除")
    @PostMapping(value = "/deleteLegalEntity")
    public CommonResult deleteLegalEntity(@RequestBody DelLegalEntityForm form) {
        legalEntityService.removeByIds(form.getIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-审核")
    @PostMapping(value = "/auditLegalEntity")
    public CommonResult auditLegalEntity(@RequestBody AuditLegalEntityForm form) {
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setId(form.getId());
        legalEntity.setAuditStatus(form.getAuditStatus());
        legalEntity.setUpdatedUser(getLoginName());
        legalEntityService.saveOrUpdate(legalEntity);
        return CommonResult.success();
    }

    @ApiOperation(value = "查询部门,id为部门ID,不传代表查所有的")
    @PostMapping(value = "/findDepartmentById")
    public CommonResult<List<DepartmentVO>> findDepartmentById(Long id) {
        List<DepartmentVO> departmentVOS = departmentService.findDepartment(id);
        return CommonResult.success(departmentVOS);
    }

    @ApiOperation(value = "查询岗位,需求上暂时没有体现岗位和部门的关系，不传查所有的")
    @PostMapping(value = "/findWorkByDepartmentId")
    public CommonResult<List<WorkVO>> findWorkByDepartmentId(Long departmentId) {
        List<WorkVO> workVOS = workService.findWork(departmentId);
        return CommonResult.success(workVOS);
    }

    /**
     * 获取created_user和updated_user
     * @return
     */
    private String getLoginName(){
        return userService.getLoginUser().getName();
    }

    


}

