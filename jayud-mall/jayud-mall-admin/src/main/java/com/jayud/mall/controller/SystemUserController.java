package com.jayud.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.bo.SystemUserParaForm;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/systemuser")
@Api(tags = "后台-用户管理")
@ApiSort(value = 3)
public class SystemUserController {

    /**
     * baseService，获取admin项目，登录用户
     */
    @Autowired
    BaseService baseService;

    @Autowired
    ISystemUserService userService;


    @ApiOperation(value = "测试,登录的当前用户")
    @ApiOperationSupport(order = 1)
    @PostMapping(value = "/hi")
    public String hi(){
        AuthUser user = baseService.getUser();
        log.info("当前登录用户user:"+user);
        return "hi, mall user: "+ JSONObject.toJSONString(user);
    }


    @ApiOperation(value = "登录接口")
    @ApiOperationSupport(order = 2)
    @PostMapping(value = "/login")
    public CommonResult<SystemUserVO> login(@Valid @RequestBody SystemUserLoginForm loginForm) {
        SystemUserVO userVO = userService.login(loginForm);
        if(userVO != null){
            return CommonResult.success(userVO);
        }else{
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

    }

    @ApiOperation(value = "查询用户list")
    @ApiOperationSupport(order = 3)
    @PostMapping(value = "/getUserList")
    public CommonResult<List<SystemUserVO>> getUserList(){
        List<SystemUserVO> userList = userService.getUserList();
        return CommonResult.success(userList);
    }

    @ApiOperation(value = "新增用户")
    @ApiOperationSupport(order = 4)
    @PostMapping(value = "/insertUser")
    public CommonResult<SystemUserVO> insertUser(@Valid @RequestBody SaveSystemUserForm form){
        AuthUser authUser = baseService.getUser();
        SystemUserVO user = ConvertUtil.convert(authUser, SystemUserVO.class);
        return userService.insertUser(form);
    }

    @ApiOperation(value = "修改用户")
    @ApiOperationSupport(order = 5)
    @PostMapping(value = "/updateUser")
    public CommonResult updateUser(@Valid @RequestBody SaveSystemUserForm form){
        userService.updateUser(form);
        return CommonResult.success("修改用户成功！");
    }

    @ApiOperation(value = "删除用户")
    @ApiOperationSupport(order = 6)
    @PostMapping(value = "/deleteUser")
    public CommonResult deleteUser(@Valid @RequestBody SystemUserParaForm form){
        Long id = form.getId();
        userService.deleteUser(id);
        return CommonResult.success("删除用户成功！");
    }

    @ApiOperation(value = "根据id获取用户")
    @ApiOperationSupport(order = 7)
    @PostMapping(value = "/getUserById")
    public CommonResult<SystemUserVO> getUser(@Valid @RequestBody SystemUserParaForm form){
        Long id = form.getId();
        SystemUserVO user = userService.getUser(id);
        return CommonResult.success(user);
    }

    @ApiOperation(value = "禁用用户")
    @ApiOperationSupport(order = 8)
    @PostMapping(value = "/disableUser")
    public CommonResult disableUser(@Valid @RequestBody SystemUserParaForm form){
        Long id = form.getId();
        userService.disableUser(id);
        return CommonResult.success("用户禁用成功！");
    }

    @ApiOperation(value = "启用用户")
    @ApiOperationSupport(order = 9)
    @PostMapping(value = "/enableUser")
    public CommonResult enableUser(@Valid @RequestBody SystemUserParaForm form){
        Long id = form.getId();
        userService.enableUser(id);
        return CommonResult.success("用户启用成功！");
    }

    @ApiOperation(value = "重置用户密码")
    @ApiOperationSupport(order = 10)
    @PostMapping(value = "/resetPassword")
    public CommonResult resetPassword(@Valid @RequestBody SystemUserParaForm form) {
        Long id = form.getId();
        userService.resetPassword(id);
        return CommonResult.success("重置用户密码成功！");
    }

    @ApiOperation(value = "用户分页查询")
    @ApiOperationSupport(order = 11)
    @PostMapping(value = "/findUserByPage")
    public CommonResult<CommonPageResult<SystemUserVO>> findUserByPage(@RequestBody QueryUserForm form){
        IPage<SystemUserVO> pageList = userService.findUserByPage(form);
        CommonPageResult<SystemUserVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}