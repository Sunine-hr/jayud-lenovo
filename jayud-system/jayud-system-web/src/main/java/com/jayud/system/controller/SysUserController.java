package com.jayud.system.controller;

import com.jayud.system.entity.SysUser;
import com.jayud.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ciro
 * @date 2022/2/15 11:16
 * @description:
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/getMsg")
    public String getMsg(){
        SysUser sysUser = userService.getById(1);
        log.info("now:{}",new Date());
        return "is_success";
    }

}
