package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/system/user")
@Api(tags = "南京电商后台用户管理")
public class SystemUserController {

    @Autowired
    ISystemUserService userService;


//    @ApiOperation(value = "测试")
//    @PostMapping(value = "hi")
//    public String hi(){
//
//        return "hi mall admin";
//    }


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



}

