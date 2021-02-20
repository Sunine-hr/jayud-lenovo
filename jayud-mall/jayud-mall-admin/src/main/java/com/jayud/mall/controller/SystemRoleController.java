package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.model.bo.SaveSystemParaForm;
import com.jayud.mall.model.vo.SystemRoleVO;
import com.jayud.mall.service.ISystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/systemrole")
@Api(tags = "A004-admin-角色管理")
@ApiSort(value = 4)
public class SystemRoleController {

    @Autowired
    ISystemRoleService roleService;

    @ApiOperation(value = "保存角色(新增/修改)")
    @PostMapping(value = "/saveRole")
    @ApiOperationSupport(order = 1)
    public CommonResult saveRole(@Valid @RequestBody SaveRoleForm from){
        roleService.saveRole(from);
        return CommonResult.success("角色保存成功！");
    }

    @ApiOperation(value = "删除角色")
    @PostMapping(value = "/deleteRole")
    @ApiOperationSupport(order = 2)
    public CommonResult deleteRole(@Valid @RequestBody SaveSystemParaForm form){
        Long id = form.getId();
        roleService.deleteRole(id);
        return CommonResult.success("删除角色成功！");
    }

    @ApiOperation(value = "根据id获取角色信息")
    @PostMapping(value = "/getRoleById")
    @ApiOperationSupport(order = 3)
    public CommonResult<SystemRoleVO> getRoleById(@Valid @RequestBody SaveSystemParaForm form){
        Long id = form.getId();
        SystemRoleVO role = roleService.getRole(id);
        return CommonResult.success(role);
    }

    @ApiOperation(value = "角色分页查询")
    @PostMapping(value = "/findRoleByPage")
    @ApiOperationSupport(order = 4)
    public CommonResult<CommonPageResult<SystemRoleVO>> findRoleByPage(@Valid @RequestBody QueryRoleForm form){
        IPage<SystemRoleVO> pageList = roleService.findRoleByPage(form);
        CommonPageResult<SystemRoleVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询角色list")
    @PostMapping("/findRole")
    @ApiOperationSupport(order = 5)
    public CommonResult<List<SystemRoleVO>> findRole(@Valid @RequestBody QueryRoleForm form) {
        List<SystemRoleVO> list = roleService.findRole(form);
        return CommonResult.success(list);
    }


}
