package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.model.vo.SystemRoleVO;
import com.jayud.mall.service.ISystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/role")
@Api(tags = "南京电商后台-角色管理")
public class SystemRoleController {

    @Autowired
    ISystemRoleService roleService;

    @ApiOperation(value = "保存角色(新增/修改)")
    @PostMapping(value = "/saveRole")
    public CommonResult saveRole(@Valid @RequestBody SaveRoleForm from){
        roleService.saveRole(from);
        return CommonResult.success("角色保存成功！");
    }

    @ApiOperation(value = "删除角色")
    @PostMapping(value = "/deleteRole")
    public CommonResult deleteRole(@RequestParam(value = "id") Long id){
        roleService.deleteRole(id);
        return CommonResult.success("删除角色成功！");
    }

    @ApiOperation(value = "根据id获取角色信息")
    @PostMapping(value = "/getRoleById")
    public CommonResult<SystemRoleVO> getRoleById(@RequestParam(value = "id") Long id){
        SystemRoleVO role = roleService.getRole(id);
        return CommonResult.success(role);
    }

    @ApiOperation(value = "角色分页查询")
    @PostMapping(value = "/findRoleByPage")
    public CommonResult<CommonPageResult<SystemRoleVO>> findRoleByPage(@RequestBody QueryRoleForm form){
        IPage<SystemRoleVO> pageList = roleService.findRoleByPage(form);
        CommonPageResult<SystemRoleVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
