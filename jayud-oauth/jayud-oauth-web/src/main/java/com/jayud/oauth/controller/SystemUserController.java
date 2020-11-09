package com.jayud.oauth.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.MD5;
import com.jayud.oauth.model.bo.*;
import com.jayud.oauth.model.enums.SystemUserStatusEnum;
import com.jayud.oauth.model.enums.UserTypeEnum;
import com.jayud.oauth.model.po.*;
import com.jayud.oauth.model.vo.*;
import com.jayud.oauth.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *
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
        if(userVO.getIsError()){
            return CommonResult.error(ResultEnum.LOGIN_FAIL);
        }
        return CommonResult.success(userVO);
    }

    /**
     * 登出接口
     *
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
    public CommonResult<EditRoleMenuVO> editRolePage(@RequestBody Map<String, Object> param) {
        EditRoleMenuVO editRoleMenuVO = new EditRoleMenuVO();
        String id = MapUtil.getStr(param, "id");
        param = new HashMap<>();
        param.put("id", Long.valueOf(id));
        SystemRole systemRole = roleService.getRoleByCondition(param);
        editRoleMenuVO.setId(Long.valueOf(id));
        editRoleMenuVO.setName(systemRole.getName());
        editRoleMenuVO.setDescription(systemRole.getDescription());
        editRoleMenuVO.setWebFlag(systemRole.getWebFlag());
        //获取角色所拥有的所有菜单
        List<SystemRoleMenuRelation> roleMenuRelations = roleMenuRelationService.findRelationByRoleId(Long.valueOf(id));
        List<Long> menuIds = new ArrayList<>();
        for (SystemRoleMenuRelation roleMenuRel : roleMenuRelations) {
            menuIds.add(roleMenuRel.getMenuId());
        }
        editRoleMenuVO.setMenuIds(menuIds);
        return CommonResult.success(editRoleMenuVO);
    }

    @ApiOperation(value = "角色权限管理-新增确认")
    @PostMapping(value = "/addRole")
    public CommonResult addRole(@RequestBody AddRoleForm addRoleForm) {
        SystemRole systemRole = ConvertUtil.convert(addRoleForm, SystemRole.class);
        if (addRoleForm.getMenuIds() == null) {
            return CommonResult.error(400, "参数不合法");
        }
        if (addRoleForm.getId() != null) {
            //编辑角色权限
            roleService.saveOrUpdate(systemRole);
            //清除旧的角色菜单关系
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(addRoleForm.getId());
            roleMenuRelationService.removeRelationByRoleId(roleIds);
        } else {//新增
            roleService.saveRole(systemRole);
        }
        systemRole.setId(systemRole.getId());
        roleMenuRelationService.createRelation(systemRole, addRoleForm.getMenuIds());
        return CommonResult.success();
    }

    @ApiOperation(value = "角色权限管理-分页查询")
    @PostMapping(value = "/findRoleByPage")
    public CommonResult<CommonPageResult<SystemRoleView>> findRoleByPage(@RequestBody QueryRoleForm form) {
        IPage<SystemRoleView> pageList = roleService.findRoleByPage(form);
        CommonPageResult<SystemRoleView> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "角色权限管理-删除")
    @PostMapping(value = "/delRole")
    public CommonResult delRole(@RequestBody DeleteForm form) {
        //删除角色前校验该角色是否有授权人员
        boolean result = userRoleRelationService.isExistUserRelation(form.getIds());
        if (!result) {
            return CommonResult.error(400, "该角色有授权人员，不允许删除");
        }
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

        //选择父级部门时要显示下面所有得部门员工信息
        if ("byDepartmentId".equals(form.getCmd())) {
            List<Long> departmentIds = new ArrayList<>();
            Long departmentId = form.getDepartmentId();
            departmentIds.add(departmentId);
            List<DepartmentVO> departmentVOS = departmentService.findDepartment(null);
            List<DepartmentVO> check = new ArrayList<>();
            handleDepartIds(departmentVOS, departmentId, check);
            for (DepartmentVO departmentVO : check) {
                departmentIds.add(departmentVO.getId());
            }
            form.setDepartmentIds(departmentIds);
        }
        IPage<SystemUserVO> pageList = userService.getPageList(form);

        CommonPageResult<SystemUserVO> pageVO = new CommonPageResult(pageList);


        return CommonResult.success(pageVO);
    }

    /**
     * 递归获取该部门下的所有子部门
     *
     * @param alls
     * @param departmentId
     * @param check
     */
    private static void handleDepartIds(List<DepartmentVO> alls, long departmentId, List<DepartmentVO> check) {
        for (DepartmentVO all : alls) {
            if (all.getFId() == departmentId) {
                check.add(all);
                handleDepartIds(alls, all.getId(), check);
            }
        }
    }


    @ApiOperation(value = "账户管理-修改数据初始化,id=用户ID")
    @PostMapping(value = "/getAccountSystemUser")
    public CommonResult<UpdateSystemUserVO> getAccountSystemUser(@RequestBody Map<String, Object> param) {
        String id = MapUtil.getStr(param, "id");
        UpdateSystemUserVO systemUserVO = userService.getSystemUser(Long.valueOf(id));
        return CommonResult.success(systemUserVO);
    }

    @ApiOperation(value = "账户管理-删除/修改/新增")
    @PostMapping(value = "/oprAccountSystemUser")
    public CommonResult oprAccountSystemUser(@RequestBody OprSystemUserForm form) {
        return userService.oprSystemUser(form);
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
    public CommonResult<List<DepartmentChargeVO>> findOrgStructureCharge(@RequestBody Map<String, Object> param) {
        String departmentId = MapUtil.getStr(param, "id");
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
    public CommonResult delDepartment(@RequestBody Map<String, Object> param) {
        Long departmentId = Long.valueOf(MapUtil.getStr(param, CommonConstant.DEPARTMENT_ID));
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add(departmentId);
        List<DepartmentVO> departmentVOS = departmentService.findDepartment(null);
        List<DepartmentVO> check = new ArrayList<>();
        handleDepartIds(departmentVOS, departmentId, check);
        for (DepartmentVO departmentVO : check) {
            departmentIds.add(departmentVO.getId());
        }
        departmentService.removeByIds(departmentIds);
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
        SystemUser systemUser = ConvertUtil.convert(form, SystemUser.class);
        String loginUser = getLoginName();
        //如果新增编辑传的是我是负责人,则把历史负责人改为员工
        if ("1".equals(form.getIsDepartmentCharge())) {
            userService.updateIsCharge(form.getDepartmentId());
        }
        //校验该员工是否存在
        Map<String, Object> param = new HashMap<>();
        List<SystemUser> users = new ArrayList<>();
        if (form.getId() != null) {
            param.put("id", form.getId());
            users = userService.findUserByCondition(param);//旧用户姓名
            if (!users.get(0).getUserName().equals(form.getUserName())) {//修改了用户姓名进行重复校验
                param = new HashMap<>();
                param.put("user_name", form.getUserName());
                users = userService.findUserByCondition(param);
                if (users != null && users.size() > 0) {
                    return CommonResult.error(400, "该员工已经存在");
                }
            }
            systemUser.setUpdatedUser(loginUser);
        } else {
            param = new HashMap<>();
            param.put("user_name", form.getUserName());
            users = userService.findUserByCondition(param);
            if (users != null && users.size() > 0) {
                return CommonResult.error(400, "该员工已经存在");
            }
            systemUser.setUserType(UserTypeEnum.EMPLOYEE_TYPE.getCode());
            systemUser.setStatus(SystemUserStatusEnum.OFF.getCode());
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
        Map<String, String> param = new HashMap<>();
        param.put("legal_name", form.getLegalName());
        if (form.getId() != null) {
            LegalEntity oldLegalEntity = legalEntityService.getById(form.getId());
            if (oldLegalEntity != null && !oldLegalEntity.getLegalName().equals(form.getLegalName())) {
                List<LegalEntityVO> legalEntityVOS = legalEntityService.findLegalEntity(param);
                if (legalEntityVOS != null && legalEntityVOS.size() > 0) {
                    return CommonResult.error(400, "该法人主体已存在，不能重复录入");
                }
            }
            legalEntity.setId(form.getId());
            legalEntity.setUpdatedUser(getLoginName());
        } else {
            List<LegalEntityVO> legalEntityVOS = legalEntityService.findLegalEntity(param);
            if (legalEntityVOS != null && legalEntityVOS.size() > 0) {
                return CommonResult.error(400, "该法人主体已存在，不能重复录入");
            }
            legalEntity.setCreatedUser(getLoginName());
        }
        legalEntity.setAuditStatus(1L);
        legalEntity.setLegalName(form.getLegalName());
        legalEntity.setLegalEnName(form.getLegalEnName());
        legalEntity.setPhone(form.getPhone());
        legalEntity.setFax(form.getFax());
        legalEntity.setAddress(form.getAddress());
        legalEntity.setBank(form.getBank());
        legalEntity.setAccountOpen(form.getAccountOpen());
        legalEntity.setTaxIdentificationNum(form.getTaxIdentificationNum());
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
        legalEntity.setAuditComment(form.getAuditComment());
        legalEntityService.saveOrUpdate(legalEntity);
        return CommonResult.success();
    }


    /**
     * 所有下拉框的初始化
     */
    @ApiOperation(value = "查询岗位,需求上暂时没有体现岗位和部门的关系，id=部门ID,不传查所有的")
    @PostMapping(value = "/initWork")
    public CommonResult<List<InitComboxVO>> findWorkByDepartmentId(@RequestBody Map<String, Object> param) {
        String departmentId = MapUtil.getStr(param, "id");
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
        Map<String, Object> param = new HashMap<>();
        param.put("status", "0");
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

    /**
     * LDR
     *
     * @return
     */
    @ApiOperation(value = "获取采购用户--下拉选框--姓名")
    @PostMapping(value = "/initPurchaseUser")
    public CommonResult<List<InitComboxVO>> initPurchaseUser() {
        Map<String, Object> param = new HashMap<>();
        param.put("status", "1");
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
        Map<String, Object> param = new HashMap<>();
        //param.put("is_department_charge","1");
        param.put("user_type", "1");
        List<SystemUser> systemUsers = userService.findUserByCondition(param);
        for (SystemUser systemUser : systemUsers) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(systemUser.getId());
            initComboxVO.setName(systemUser.getUserName());
            initComboxs.add(initComboxVO);
        }
        return CommonResult.success(initComboxs);
    }

    @ApiOperation(value = "获取启用系统用户")
    @RequestMapping("/getEnableUser")
    public CommonResult<List<SystemUserVO>> getEnableUser() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("status", SystemUserStatusEnum.ON.getCode());
        List<SystemUser> users = this.userService.findUserByCondition(map);
        List<SystemUserVO> vo = new ArrayList<>();
        users.stream().forEach(tmp -> vo.add(ConvertUtil.convert(tmp, SystemUserVO.class)));
        return CommonResult.success(vo);
    }

    @ApiOperation(value = "账户管理-选择姓名联动出部门和岗位,id=姓名隐藏值")
    @PostMapping(value = "/initUserWorkInfo")
    public CommonResult<Map<String, Object>> initUserWorkInfo(@RequestBody Map<String, Object> param) {
        Long userId = Long.valueOf(MapUtil.getStr(param, CommonConstant.ID));
        SystemUser systemUser = userService.getById(userId);
        Map<String, Object> result = new HashMap<>();
        if (systemUser == null || systemUser.getDepartmentId() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        result.put(CommonConstant.WORK, systemUser.getWorkName());
        result.put(CommonConstant.DEPARTMENT_ID, systemUser.getDepartmentId());
        return CommonResult.success(result);
    }

    /**
     * 获取created_user和updated_user
     *
     * @return
     */
    private String getLoginName() {
        return userService.getLoginUser().getName();
    }


    @ApiOperation(value = "重置密码")
    @PostMapping(value = "/resetPassword")
    public CommonResult resetPassword(@Valid @RequestBody ResetUserPasswordForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return CommonResult.error(400, "两个密码不相同");
        }
        SystemUser user = new SystemUser().setId(form.getId()).setPassword(MD5.encode(form.getPassword()));
        this.userService.saveOrUpdateSystemUser(user);
        return CommonResult.success();
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(@Valid @RequestBody UpdateUserPasswordForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return CommonResult.error(400, "两个密码不相同");
        }
        String oldPassWord = MD5.encode(form.getOldPassword());
        if (!this.userService.getById(form.getId()).getPassword().equals(oldPassWord)) {
            return CommonResult.error(400,"密码错误");
        }

        SystemUser user = new SystemUser().setId(form.getId()).setPassword(MD5.encode(form.getPassword()));
        this.userService.saveOrUpdateSystemUser(user);
        return CommonResult.success();
    }

}

