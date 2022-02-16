package com.jayud.auth.controller;

import com.jayud.auth.model.dto.AuthUserDetail;
import com.jayud.auth.model.entity.SysUser;
import com.jayud.auth.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author ciro
 * @since 2022-02-15
 */
@Slf4j
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/getMsg")
    public String isSuccess(){
        SysUser sysUser = userService.getUser();
        log.info("sysUser:{}",sysUser);
        return "is_success";
    }

    @GetMapping(value = "/current")
    public Principal getSysUser(Principal principal) {
        Object userDetail = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail == null){
            return null;
        }
        return principal;
    }


}

