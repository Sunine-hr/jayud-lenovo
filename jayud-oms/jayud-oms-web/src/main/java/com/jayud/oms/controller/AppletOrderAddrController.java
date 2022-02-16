package com.jayud.oms.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小程序订单地址记录 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/appletOrderAddr")
public class AppletOrderAddrController {

    @GetMapping("/getMsg")
    public String getMsg(){
        return "is_success";
    }
}

