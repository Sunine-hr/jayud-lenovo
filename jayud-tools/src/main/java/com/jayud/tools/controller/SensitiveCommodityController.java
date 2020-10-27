package com.jayud.tools.controller;

import com.jayud.common.CommonResult;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.service.ISensitiveCommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensitivecommodity")
@Api(tags = "佳裕达小工具-敏感品名管理")
public class SensitiveCommodityController {

    @Autowired
    ISensitiveCommodityService sensitiveCommodityService;

    @ApiOperation(value = "查询敏感品名list")
    @PostMapping(value = "/getSensitiveCommodityList")
    public CommonResult getSensitiveCommodityList(){
        List<SensitiveCommodity> userList = sensitiveCommodityService.getSensitiveCommodityList();
        return CommonResult.success(userList);
    }

}
