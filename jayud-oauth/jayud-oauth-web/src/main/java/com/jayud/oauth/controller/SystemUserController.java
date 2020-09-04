package com.jayud.oauth.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.AddRoleForm;
import com.jayud.model.bo.SystemUserLoginForm;
import com.jayud.model.po.SystemRole;
import com.jayud.model.vo.SystemMenuNode;
import com.jayud.model.vo.SystemUserVO;
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

}

