package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.HarbourInfoForm;
import com.jayud.mall.model.po.HarbourInfo;
import com.jayud.mall.service.IHarbourInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/harbourinfo")
@Api(tags = "后台-机场港口信息接口")
public class HarbourInfoController {

    @Autowired
    IHarbourInfoService harbourInfoService;

    @ApiOperation(value = "查询机场港口信息List")
    @PostMapping("/findHarbourInfo")
    public CommonResult<List<HarbourInfo>> findHarbourInfo(@RequestBody HarbourInfoForm form) {
        List<HarbourInfo> list = harbourInfoService.findHarbourInfo(form);
        return CommonResult.success(list);
    }

}
