package com.jayud.auth.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.jayud.auth.model.dto.AddSysRole;
import com.jayud.auth.model.po.SysDict;
import com.jayud.auth.model.vo.SysRoleVO;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.ISysMenuService;
import com.jayud.auth.service.ISysRoleMenuService;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.model.po.SysRole;

import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 角色表 控制类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Api(tags = "角色表")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-21
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysRole>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysRoleVO>> selectPage(SysRole sysRole,
                                                   @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        sysRole.setIsDeleted(false);
        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            sysRole.setTenantCode(null);
        } else {
            sysRole.setTenantCode(CurrentUserUtil.getUserTenantCode());
        }
        return BaseResult.ok(sysRoleService.selectPage(sysRole, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-21
     * @param: sysRole
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysRole>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysRole>> selectList(SysRole sysRole,
                                                HttpServletRequest req) {

        sysRole.setIsDeleted(false);
        sysRole.setTenantCode(CurrentUserUtil.getUserTenantCode());
        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            sysRole.setTenantCode(null);
        } else {
            sysRole.setTenantCode(CurrentUserUtil.getUserTenantCode());
        }
        return BaseResult.ok(sysRoleService.selectList(sysRole));
    }

    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-21
     * @param: sysRole
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增/修改")
    @PostMapping("/addOrUpdate")
    public BaseResult addOrUpdate(@Valid @RequestBody AddSysRole form) {
//        form.checkAddOrUpdate();
        this.sysRoleService.checkUnique(form.getId(), form.getRoleName(), form.getRoleCode());
        sysRoleService.addOrUpdate(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        SysRole sysRole = this.sysRoleService.getById(id);
        if (sysRole.getRoleCode().equals(CommonConstant.SUPER_TENANT)) {
            throw new JayudBizException(SysTips.DELETE_ADMIN_ERROR);
        }
        if (this.sysUserRoleService.exitByRolesIds(Arrays.asList(id))) {
            return BaseResult.error(SysTips.ERROR_MSG_ONE);
        }

        sysRoleService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 批量逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("批量逻辑删除")
    @PostMapping("/batchLogicDel")
    public BaseResult batchLogicDel(@RequestBody List<SysRole> sysRoles) {

        for (SysRole sysRole : sysRoles) {
            if (sysRole.getRoleCode().equals(CommonConstant.SUPER_TENANT)) {
                throw new JayudBizException(SysTips.DELETE_ADMIN_ERROR);
            }
        }

        List<Long> rolesIds = sysRoles.stream().map(e -> e.getId()).collect(Collectors.toList());

        if (this.sysUserRoleService.exitByRolesIds(rolesIds)) {
            return BaseResult.error(SysTips.ERROR_MSG_ONE);
        }
        List<SysRole> tmps = new ArrayList<>();
        for (Long rolesId : rolesIds) {
            SysRole sysRole = new SysRole();
            sysRole.setIsDeleted(true).setId(rolesId);
            tmps.add(sysRole);
        }
        sysRoleService.updateBatchById(tmps);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    @ApiOperation("关联员工")
    @PostMapping("/associatedEmployees")
    public BaseResult associatedEmployees(@RequestBody Map<String, Object> map) {
        Long rolesId = MapUtil.getLong(map, "rolesId");
        List<Long> userIds = MapUtil.get(map, "userIds", new TypeReference<List<Long>>() {
        });
        if (rolesId == null || CollectionUtil.isEmpty(userIds)) {
            return BaseResult.error(SysTips.ERROR_MSG);
        }
        this.sysUserRoleService.associatedEmployees(rolesId, userIds);
        return BaseResult.ok();

    }

    @ApiOperation("根据角色id获取用户id集合")
    @GetMapping("/getUserIdsByRoleId")
    public BaseResult<List<Long>> getUserIdsByRoleId(@RequestParam("roleId") Long roleId) {
        List<Long> userIds = this.sysUserRoleService.getUserIdsByRoleId(roleId);
        List<Long> ids = new ArrayList<>();
        userIds.forEach(e ->
                ids.add(Long.valueOf("111111" + e))
        );
        return BaseResult.ok(ids);

    }

    @ApiOperation("分页查询关联员工")
    @GetMapping("/selectAssociatedEmployeesPage")
    public BaseResult<IPage<SysUserVO>> selectAssociatedEmployeesPage(@RequestParam("rolesId") Long rolesId,
                                                                      @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      HttpServletRequest req) {
        IPage<SysUserVO> page = this.sysUserRoleService.selectAssociatedEmployeesPage(rolesId, currentPage, pageSize, req);
        return BaseResult.ok(page);

    }


    /**
     * @description 删除员工
     **/
    @ApiOperation("删除员工")
    @GetMapping("/deleteEmployee")
    public BaseResult deleteEmployee(@RequestParam("rolesId") Long rolesId, @RequestParam("userId") Long userId) {
        sysUserRoleService.deleteEmployees(rolesId, Arrays.asList(userId));
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 批量删除员工
     **/
    @ApiOperation("批量删除员工")
    @PostMapping("/deleteEmployees")
    public BaseResult deleteEmployees(@RequestBody Map<String, Object> map) {
        Long rolesId = MapUtil.getLong(map, "rolesId");
        List<Long> userIds = MapUtil.get(map, "userIds", new TypeReference<List<Long>>() {
        });
        if (rolesId == null || CollectionUtil.isEmpty(userIds)) {
            return BaseResult.error(SysTips.BATCH_DEL_SUCCESS);
        }
        sysUserRoleService.deleteEmployees(rolesId, userIds);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 根据用户id获取角色集合id
     **/
    @ApiOperation("根据用户id获取角色集合id")
    @GetMapping("/getRoleIdsByUserId")
    public BaseResult<List<Long>> getRoleIdsByUserId(@RequestParam("userId") Long userId) {
        List<Long> roleIds = sysRoleService.getRoleIdsByUserId(userId);
        return BaseResult.ok(roleIds);
    }

    /**
     * @description 设置角色
     **/
    @ApiOperation("设置角色")
    @PostMapping("/setRoles")
    public BaseResult setRoles(@RequestBody Map<String, Object> map) {
        Long userId = MapUtil.getLong(map, "userId");
        List<Long> roleIds = MapUtil.get(map, "roleIds", new TypeReference<List<Long>>() {
        });
        if (userId == null || CollectionUtil.isEmpty(roleIds)) {
            return BaseResult.error(SysTips.ERROR_MSG);
        }
        sysRoleService.setRoles(userId, roleIds);
        return BaseResult.ok();
    }

    /**
     * @description 根据角色id获取菜单权限id
     **/
    @ApiOperation("根据角色id获取菜单权限id")
    @GetMapping("/getMenuIdsByRoleId")
    public BaseResult<List<Long>> getMenuIdsByRoleId(@RequestParam("roleId") Long roleId) {
        List<Long> menuIds = sysRoleMenuService.getMenuIdsByRoleId(roleId);
        return BaseResult.ok(menuIds);
    }

    /**
     * @description 设置角色权限
     **/
    @ApiOperation("设置角色权限")
    @PostMapping("/setRolePermissions")
    public BaseResult setRolePermissions(@RequestBody Map<String, Object> map) {
        Long rolesId = MapUtil.getLong(map, "rolesId");
        List<Long> menuIds = MapUtil.get(map, "menuIds", new TypeReference<List<Long>>() {
        });
        Set<Long> parentMenuIds = MapUtil.get(map, "parentMenuIds", new TypeReference<Set<Long>>() {
        });
        if (rolesId == null || CollectionUtil.isEmpty(menuIds)) {
            return BaseResult.error(SysTips.ERROR_MSG);
        }
        parentMenuIds.addAll(menuIds);
        List<Long> tmps = new ArrayList<>();
        tmps.addAll(parentMenuIds);
        tmps.addAll(menuIds);
        sysRoleService.setRolePermissions(rolesId, tmps);
        return BaseResult.ok();
    }

    /**
     * @description 编辑
     * @author jayud
     * @date 2022-02-21
     * @param: sysRole
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody SysRole sysRole ){
//        sysRoleService.updateById(sysRole);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id) {
//        sysRoleService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysRole>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysRole> queryById(@RequestParam(name = "id", required = true) int id) {
        SysRole sysRole = sysRoleService.getById(id);
        return BaseResult.ok(sysRole);
    }

    @ApiOperation("判断用户是否有角色")
    @ApiImplicitParam(name = "roleCode", value = "角色编码", dataType = "String", required = true)
    @GetMapping(value = "/isHasRole")
    public BaseResult isHasRole(@RequestParam(name = "roleCode", required = true) String roleCode) {
        if (CurrentUserUtil.hasRole(roleCode)) {
            return BaseResult.ok();
        }
        return BaseResult.error();
    }

    @ApiOperation("根据租户查询角色")
    @PostMapping(value = "/getRoleByTenantCode")
    public BaseResult<List<SysRole>> getRoleByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        return BaseResult.ok(sysRoleService.getRoleByTenantCode(tenantCode));
    }
}
