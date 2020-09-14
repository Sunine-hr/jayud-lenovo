package com.jayud.oauth.controller;

import cn.hutool.core.map.MapUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/system/user")
@Api(tags = "集团管理")
public class SystemUserController {

    @Autowired
    ISystemUserService userService;

    @Autowired
    ISystemMenuService menuService;

    @Autowired
    ISystemRoleMenuRelationService roleMenuRelationService;

    @Autowired
    ISystemUserRoleRelationService userRoleRelationService;

    @Autowired
    ISystemRoleService roleService;

    @Autowired
    ISystemDepartmentService departmentService;

    @Autowired
    ILegalEntityService legalEntityService;

    @Autowired
    ISystemWorkService workService;

    @Autowired
    ISystemCompanyService companyService;

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

    @ApiOperation(value = "获取登录后用户的角色菜单相关信息")
    @PostMapping(value = "/findLoginUserInfo")
    public CommonResult<SystemUserLoginInfoVO> findLoginUserInfo() {
        SystemUserLoginInfoVO userLoginInfoVO = userService.findLoginUserInfo();
        return CommonResult.success(userLoginInfoVO);
    }

    @ApiOperation(value = "角色权限管理-新增数据初始化")
    @PostMapping(value = "/findAllMenuNode")
    public CommonResult<List<QueryMenuStructureVO>> findAllMenuNode() {
        List<QueryMenuStructureVO> menuStructureVOS = menuService.findAllMenuNode();
        return CommonResult.success(menuStructureVOS);
    }

    @ApiOperation(value = "角色权限管理-编辑数据初始化 id=角色ID")
    @PostMapping(value = "/editRolePage")
    public CommonResult<EditRoleMenuVO> editRolePage(@RequestBody Map<String,Object> param) {
        EditRoleMenuVO editRoleMenuVO = new EditRoleMenuVO();
        String id = MapUtil.getStr(param,"id");
        param = new HashMap<>();
        param.put("id",Long.valueOf(id));
        SystemRole systemRole = roleService.getRoleByCondition(param);
        editRoleMenuVO.setId(Long.valueOf(id));
        editRoleMenuVO.setName(systemRole.getName());
        editRoleMenuVO.setDescription(systemRole.getDescription());
        editRoleMenuVO.setWebFlag(systemRole.getWebFlag());
        return CommonResult.success(editRoleMenuVO);
    }

    @ApiOperation(value = "角色权限管理-新增确认")
    @PostMapping(value = "/addRole")
    public CommonResult addRole(@RequestBody AddRoleForm addRoleForm){
        SystemRole systemRole = ConvertUtil.convert(addRoleForm, SystemRole.class);
        if(addRoleForm.getId() != null){
            //编辑角色权限
            roleService.saveOrUpdate(systemRole);
            //清除旧的角色菜单关系
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(addRoleForm.getId());
            roleMenuRelationService.removeRelationByRoleId(roleIds);
        }else {//新增
            roleService.saveRole(systemRole);
        }
        systemRole.setId(systemRole.getId());
        roleMenuRelationService.createRelation(systemRole, addRoleForm.getMenuIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "角色权限管理-分页查询")
    @PostMapping(value = "/findRoleByPage")
    public CommonResult<CommonPageResult<SystemRoleView>> findRoleByPage(@RequestBody QueryRoleForm form){
        IPage<SystemRoleView> pageList = roleService.findRoleByPage(form);
        CommonPageResult<SystemRoleView> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "角色权限管理-删除")
    @PostMapping(value = "/delRole")
    public CommonResult delRole(@RequestBody DeleteForm form){
        roleService.removeByIds(form.getIds());//删除角色
        roleMenuRelationService.removeRelationByRoleId(form.getIds());//删除角色和菜单的关系
        userRoleRelationService.removeRelationByRoleId(form.getIds());//删除角色和用户的关系
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

    @ApiOperation(value = "账户管理-修改数据初始化,id=用户ID")
    @PostMapping(value = "/getAccountSystemUser")
    public CommonResult<UpdateSystemUserVO> getAccountSystemUser(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        UpdateSystemUserVO systemUserVO = userService.getSystemUser(Long.valueOf(id));
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
    @ApiOperation(value = "组织架构界面-初始化部门结构")
    @PostMapping(value = "/findOrgStructure")
    public CommonResult<List<QueryOrgStructureVO>> findOrgStructure() {
        List<QueryOrgStructureVO> orgStructures = userService.findOrgStructure();
        return CommonResult.success(orgStructures);
    }

    @ApiOperation(value = "组织架构界面-初始化负责人信息,id=部门ID")
    @PostMapping(value = "/findOrgStructureCharge")
    public CommonResult<List<DepartmentChargeVO>> findOrgStructureCharge(@RequestBody Map<String,Object> param) {
        String departmentId = MapUtil.getStr(param,"id");
        List<DepartmentChargeVO> departmentChargeVOS = userService.findOrgStructureCharge(Long.valueOf(departmentId));
        return CommonResult.success(departmentChargeVOS);
    }

    @ApiOperation(value = "组织架构界面-新增部门/编辑,departmentId编辑时传")
    @PostMapping(value = "/saveOrUpdateDepartment")
    public CommonResult saveOrUpdateDepartment(@RequestBody AddDepartmentForm form) {
        departmentService.saveOrUpdateDepartment(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除部门,departmentId传的是部门ID")
    @PostMapping(value = "/delDepartment")
    public CommonResult delDepartment(@RequestBody Map<String,Object> param) {
        departmentService.removeById(MapUtil.getStr(param,"departmentId"));
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除员工")
    @PostMapping(value = "/delSystemUser")
    public CommonResult delSystemUser(@RequestBody DeleteForm form) {
        userService.removeByIds(form.getIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-新增员工/编辑")
    @PostMapping(value = "/saveOrUpdatedSystemUser")
    public CommonResult saveOrUpdatedSystemUser(@RequestBody AddSystemUserForm form) {
        SystemUser systemUser = ConvertUtil.convert(form,SystemUser.class);
        String loginUser = getLoginName();
        //如果新增编辑传的是我是负责人,则把历史负责人改为员工
        if("1".equals(form.getIsDepartmentCharge())){
            userService.updateIsCharge(form.getDepartmentId());
        }
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
    public CommonResult deleteLegalEntity(@RequestBody DeleteForm form) {
        legalEntityService.removeByIds(form.getIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-审核,审核界面信息就只有4个,可从列表里面取")
    @PostMapping(value = "/auditLegalEntity")
    public CommonResult auditLegalEntity(@RequestBody AuditLegalEntityForm form) {
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setId(form.getId());
        legalEntity.setAuditStatus(Long.parseLong(form.getAuditStatus()));
        legalEntity.setUpdatedUser(getLoginName());
        legalEntityService.saveOrUpdate(legalEntity);
        return CommonResult.success();
    }


    /**
     * 所有下拉框的初始化
     */
    @ApiOperation(value = "查询岗位,需求上暂时没有体现岗位和部门的关系，id=部门ID,不传查所有的")
    @PostMapping(value = "/initWork")
    public CommonResult<List<InitComboxVO>> findWorkByDepartmentId(@RequestBody Map<String,Object> param) {
        String departmentId = MapUtil.getStr(param,"id");
        List<WorkVO> workVOS = workService.findWork(Long.valueOf(departmentId));
        List<InitComboxVO> initComboxs = new ArrayList<>();
        for (WorkVO workVO : workVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(workVO.getId());
            initComboxVO.setName(workVO.getWorkName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-姓名")
    @PostMapping(value = "/initUserAccount")
    public CommonResult<List<InitComboxVO>> initUserAccount() {
        Map<String,Object> param = new HashMap<>();
        param.put("status","0");
        List<InitComboxVO> initComboxs = new ArrayList<>();
        List<SystemUser> systemUsers = userService.findUserByCondition(param);
        for (SystemUser systemUser : systemUsers) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemUser.getId());
            initComboxVO.setName(systemUser.getUserName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-部门")
    @PostMapping(value = "/initUserDepartment")
    public CommonResult<List<InitComboxVO>> initUserDepartment() {
        List<InitComboxVO> initComboxs = new ArrayList<>();
        List<DepartmentVO> departmentVOS = departmentService.findDepartment(null);
        for (DepartmentVO departmentVO : departmentVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(departmentVO.getId());
            initComboxVO.setName(departmentVO.getName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-岗位")
    @PostMapping(value = "/initUserWork")
    public CommonResult<List<InitComboxVO>> initUserWork() {
        List<InitComboxVO> initComboxs = new ArrayList<>();
        List<WorkVO> workVOS = workService.findWork(null);
        for (WorkVO workVO : workVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(workVO.getId());
            initComboxVO.setName(workVO.getWorkName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-角色")
    @PostMapping(value = "/initAccountRole")
    public CommonResult<List<InitComboxVO>> initAccountRole() {
        List<InitComboxVO> initComboxs = new ArrayList<>();
        List<SystemRoleVO> systemRoleVOS = roleService.findRole();
        for (SystemRoleVO systemRoleVO : systemRoleVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemRoleVO.getId());
            initComboxVO.setName(systemRoleVO.getName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-所属公司")
    @PostMapping(value = "/initUserAccountCompany")
    public CommonResult<List<InitComboxVO>> initUserAccountCompany() {
        List<InitComboxVO> initComboxs = new ArrayList<>();
        List<CompanyVO> companyVOS = companyService.findCompany();
        for (CompanyVO companyVO : companyVOS) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(companyVO.getId());
            initComboxVO.setName(companyVO.getCompanyName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "账户管理-新增数据初始化-所属上级")
    @PostMapping(value = "/initUserAccountSuperiors")
    public CommonResult<List<InitComboxVO>> initUserAccountSuperiors() {
        List<InitComboxVO> initComboxs = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        //param.put("is_department_charge","1");
        List<SystemUser> systemUsers = userService.findUserByCondition(param);
        for (SystemUser systemUser : systemUsers) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemUser.getId());
            initComboxVO.setName(systemUser.getUserName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    /**
     * 获取created_user和updated_user
     * @return
     */
    private String getLoginName(){
        return userService.getLoginUser().getName();
    }

    


}

