package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.auth.model.po.SysUserRole;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 用户-角色关联表 控制类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Api(tags = "用户-角色关联表")
@RestController
@RequestMapping("/sysUserRole")
public class SysUserRoleController {



    @Autowired
    public ISysUserRoleService sysUserRoleService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysUserRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUserRole>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysUserRole>> selectPage(SysUserRole sysUserRole,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(sysUserRoleService.selectPage(sysUserRole,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-22
    * @param: sysUserRole
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysUserRole>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysUserRole>> selectList(SysUserRole sysUserRole,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysUserRoleService.selectList(sysUserRole));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-22
    * @param: sysUserRole
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysUserRole sysUserRole ){
        sysUserRoleService.save(sysUserRole);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-02-22
     * @param: sysUserRole
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysUserRole sysUserRole ){
        sysUserRoleService.updateById(sysUserRole);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysUserRoleService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysUserRoleService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUserRole>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysUserRole> queryById(@RequestParam(name="id",required=true) int id) {
        SysUserRole sysUserRole = sysUserRoleService.getById(id);
        return BaseResult.ok(sysUserRole);
    }


}
