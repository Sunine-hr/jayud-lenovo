package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.po.SystemRoleMenuRelation;
import com.jayud.mall.service.ISystemRoleMenuRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/rolemenu")
@Api(tags = "南京电商后台-角色菜单管理")
public class SystemRoleMenuRelationController {

    @Autowired
    ISystemRoleMenuRelationService roleMenuRelationService;

    @ApiOperation(value = "根据角色Id，查询角色菜单")
    @PostMapping(value = "/findRoleMenuRelationByRoleId")
    public CommonResult<List<SystemRoleMenuRelation>> findUserByPage(@RequestParam(value = "roleId") Long roleId){
        List<SystemRoleMenuRelation> roleMenuRelationList = roleMenuRelationService.findRoleMenuRelationByRoleId(roleId);
        return CommonResult.success(roleMenuRelationList);
    }

}
