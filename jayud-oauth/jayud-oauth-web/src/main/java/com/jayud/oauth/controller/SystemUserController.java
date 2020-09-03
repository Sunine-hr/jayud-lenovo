package com.jayud.oauth.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.model.bo.SystemUserLoginForm;
import com.jayud.model.vo.SystemUserVO;
import com.jayud.model.vo.UserLoginToken;
import com.jayud.oauth.service.ISystemMenuService;
import com.jayud.oauth.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/system/user")
@Api(tags = "用户管理")
public class SystemUserController {

    @Autowired
    ISystemUserService service;

    @Autowired
    ISystemMenuService menuService;


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

        //该系统暂没有图验证 TODO

        //登录逻辑
        SystemUserVO userVO = service.login(token);

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
        service.logout();
        return CommonResult.success();
    }

    /**
     *
     * @return 登录用户信息
     */
    @ApiOperation(value = "登录用户信息查询")
    @GetMapping(value = "/info")
    @ResponseBody
    public CommonResult<SystemUserVO> info() {
        //登录用户信息
        SystemUserVO loginUser = service.getLoginUser();
        //查询权限菜单集合
        if(CollectionUtils.isNotEmpty(loginUser.getRoleIds())){
            loginUser.setMenuNodeList(menuService.roleTreeList(loginUser.getRoleIds()));
        }
        return CommonResult.success(loginUser);
    }

}

