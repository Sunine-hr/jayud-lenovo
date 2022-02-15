package com.jayud.system.controller;

import lombok.extern.slf4j.Slf4j;
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
public class UserController {

    @GetMapping("/getMsg")
    public String getMsg(){
        log.info("now:{}",new Date());
        return "is_success";
    }

}
