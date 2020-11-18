package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
import com.jayud.mall.service.IServiceGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servicegroup")
@Api(tags = "报价服务组接口")
public class ServiceGroupController {

    @Autowired
    IServiceGroupService serviceGroupService;


    @ApiOperation(value = "查询报价服务组List")
    @PostMapping("/findServiceGroup")
    public CommonResult<List<ServiceGroup>> findServiceGroup(@RequestBody ServiceGroupForm form) {
        List<ServiceGroup> list = serviceGroupService.findServiceGroup(form);
        return CommonResult.success(list);
    }



}
