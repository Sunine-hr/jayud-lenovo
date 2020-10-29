package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.jayud.mall.service.ISystemUserRoleRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/userrole")
@Api(tags = "南京电商后台-用户角色管理")
public class SystemUserRoleRelationController {

    @Autowired
    ISystemUserRoleRelationService userRoleRelationService;

    @ApiOperation(value = "根据用户Id，查询用户角色信息")
    @PostMapping(value = "/findUserRoleRelationByUserId")
    public CommonResult<List<SystemUserRoleRelation>> findUserRoleRelationByUserId(@RequestParam(value = "userId") Long userId){
        List<SystemUserRoleRelation> roleMenuRelationList = userRoleRelationService.findUserRoleRelationByUserId(userId);
        return CommonResult.success(roleMenuRelationList);
    }

}
