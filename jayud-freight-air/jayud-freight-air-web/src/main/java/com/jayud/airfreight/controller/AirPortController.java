package com.jayud.airfreight.controller;


import com.jayud.airfreight.service.IAirPortService;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 飞机港口地址表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/airPort")
@Api(tags = "飞机港口")
public class AirPortController {

    @Autowired
    private IAirPortService airPortService;

//    @ApiOperation(value = "飞机港口下拉选项")
//    @PostMapping(value = "/initAirPort")
//    public CommonResult<List<InitComboxStrVO>> initAirPort(){
//
//    }
}

