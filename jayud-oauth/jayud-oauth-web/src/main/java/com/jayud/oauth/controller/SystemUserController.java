package com.jayud.oauth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.*;
import com.jayud.model.po.SystemRole;
import com.jayud.model.vo.SystemMenuNode;
import com.jayud.model.vo.SystemUserVO;
import com.jayud.model.vo.UpdateSystemUserVO;
import com.jayud.model.vo.UserLoginToken;
import com.jayud.oauth.service.ISystemMenuService;
import com.jayud.oauth.service.ISystemRoleMenuRelationService;
import com.jayud.oauth.service.ISystemRoleService;
import com.jayud.oauth.service.ISystemUserService;
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

    /**
     * 登录接口
     * @param loginForm
     * @return
     */
    @ApiOperation(value = "登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
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
    @GetMapping(value = "/logout")
    @ResponseBody
    public CommonResult logout() {
        //登出
        userService.logout();
        return CommonResult.success();
    }

    @ApiOperation(value = "角色权限管理-新增数据初始化")
    @RequestMapping(value = "/findAllMenuNode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SystemMenuNode>> findAllMenuNode() {
        List<SystemMenuNode> systemMenuNodes = menuService.findAllMenuNode();
        return CommonResult.success(systemMenuNodes);
    }

    @ApiOperation(value = "角色权限管理-新增确认")
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
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
    @PostMapping("/list")
    @ApiOperation(value = "账户管理和人员审核（总经办")
    public CommonResult<CommonPageResult<SystemUserVO>> list(@RequestBody QuerySystemUserForm form) {

        IPage<SystemUserVO> pageList = userService.getPageList(form);

        CommonPageResult<SystemUserVO> pageVO = new CommonPageResult(pageList);


        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "账户管理-新增修改数据初始化")
    @RequestMapping(value = "/getAccountSystemUser", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> getAccountSystemUser(Long id) {
        UpdateSystemUserVO systemUserVO = userService.getSystemUser(id);
        return CommonResult.success(systemUserVO);
    }

    @ApiOperation(value = "账户管理-删除/修改/新增")
    @RequestMapping(value = "/oprAccountSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> oprAccountSystemUser(@RequestBody OprSystemUserForm form) {
        userService.oprSystemUser(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "人员审核(总经办)-审核")
    @RequestMapping(value = "/auditAccountSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> auditAccountSystemUser(@RequestBody AuditSystemUserForm form) {
        userService.auditSystemUser(form);
        return CommonResult.success();
    }

    /**
     * 组织架构模块
     */
    @ApiOperation(value = "组织架构界面-初始化")
    @RequestMapping(value = "/findOrgStructure", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> findOrgStructure(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-新增部门/编辑")
    @RequestMapping(value = "/saveOrUpdateDepartment", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> saveOrUpdateDepartment(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除部门")
    @RequestMapping(value = "/delDepartment", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> delDepartment(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-删除员工")
    @RequestMapping(value = "/delSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> delSystemUser(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "组织架构界面-新增员工/编辑")
    @RequestMapping(value = "/saveOrUpdatedSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> saveOrUpdatedSystemUser(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    /**
     * 法人主体模块
     */
    @ApiOperation(value = "法人主体-界面")
    @RequestMapping(value = "/findLegalEntityByPage", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> findLegalEntityByPage(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-新增/修改")
    @RequestMapping(value = "/saveOrUpdateLegalEntity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> saveOrUpdateLegalEntity(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-删除")
    @RequestMapping(value = "/deleteLegalEntity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> deleteLegalEntity(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }

    @ApiOperation(value = "法人主体-审核")
    @RequestMapping(value = "/auditLegalEntity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UpdateSystemUserVO> auditLegalEntity(@RequestBody AuditSystemUserForm form) {
        return CommonResult.success();
    }


    


}

