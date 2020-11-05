package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "A001-南京电商后台-用户管理")
@RestController
@RequestMapping("/system/user")
public class SystemUserController {

    @Autowired
    ISystemUserService userService;


    @ApiOperation(value = "测试", position = 1)
    @PostMapping(value = "/hi")
    public String hi(){
        return "hi mall admin";
    }


    @ApiOperation(value = "登录接口")
    @PostMapping(value = "/login")
    public CommonResult<SystemUserVO> login(@Valid @RequestBody SystemUserLoginForm loginForm) {
        SystemUserVO userVO = userService.login(loginForm);
        if(userVO != null){
            return CommonResult.success(userVO);
        }else{
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

    }

    @ApiOperation(value = "查询用户list", position = 2)
    @PostMapping(value = "/getUserList")
    public CommonResult<List<SystemUserVO>> getUserList(){
        List<SystemUserVO> userList = userService.getUserList();
        return CommonResult.success(userList);
    }

    @ApiOperation(value = "新增用户", position = 3)
    @PostMapping(value = "/insertUser")
    public CommonResult insertUser(@Valid @RequestBody SaveUserForm userForm){
        userService.insertUser(userForm);
        return CommonResult.success("新增用户成功！");
    }

    @ApiOperation(value = "修改用户")
    @PostMapping(value = "/updateUser")
    public CommonResult updateUser(@Valid @RequestBody SaveUserForm userForm){
        userService.updateUser(userForm);
        return CommonResult.success("修改用户成功！");
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "/deleteUser")
    public CommonResult deleteUser(@RequestParam(value = "id") Long id){
        userService.deleteUser(id);
        return CommonResult.success("删除用户成功！");
    }

    @ApiOperation(value = "根据id获取用户")
    @PostMapping(value = "/getUserById")
    public CommonResult<SystemUserVO> getUser(@RequestParam(value = "id") Long id){
        SystemUserVO user = userService.getUser(id);
        return CommonResult.success(user);
    }

    @ApiOperation(value = "禁用用户")
    @PostMapping(value = "/disableUser")
    public CommonResult disableUser(@RequestParam(value = "id") Long id){
        userService.disableUser(id);
        return CommonResult.success("用户禁用成功！");
    }

    @ApiOperation(value = "启用用户")
    @PostMapping(value = "/enableUser")
    public CommonResult enableUser(@RequestParam(value = "id") Long id){
        userService.enableUser(id);
        return CommonResult.success("用户启用成功！");
    }

    @ApiOperation(value = "重置用户密码")
    @PostMapping(value = "/resetPassword")
    public CommonResult resetPassword(@RequestParam(value = "id") Long id) {
        userService.resetPassword(id);
        return CommonResult.success("重置用户密码成功！");
    }

    @ApiOperation(value = "用户分页查询")
    @PostMapping(value = "/findUserByPage")
    public CommonResult<CommonPageResult<SystemUserVO>> findUserByPage(@RequestBody QueryUserForm form){
        IPage<SystemUserVO> pageList = userService.findUserByPage(form);
        CommonPageResult<SystemUserVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }



}

