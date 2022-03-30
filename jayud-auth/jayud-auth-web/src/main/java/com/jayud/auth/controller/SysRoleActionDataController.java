package com.jayud.auth.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddSysRoleActionDataForm;
import com.jayud.auth.model.bo.PermissionForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.enums.CorrespondEnum;
import com.jayud.auth.model.po.*;
import com.jayud.auth.model.vo.SysRoleActionDataVO;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.*;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.CurrentUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色数据权限 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/systemRoleActionData")
@Api(tags = "权限数据管理")
public class SysRoleActionDataController {

    @Autowired
    private ISysRoleActionDataService systemRoleActionDataService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Autowired
    private ISysMenuService sysMenuService;

    @ApiOperation(value = "根据条件分页查询角色数据权限")
    @PostMapping(value = "/findByPage")
    public BaseResult findByPage(@RequestBody QueryForm form) {
        if (form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null) {
            return BaseResult.error(444, "该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        IPage<SysRoleActionDataVO> page = this.systemRoleActionDataService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return BaseResult.ok(new CommonPageResult(page));
        } else {
            CommonPageResult<SysRoleActionDataVO> pageVO = new CommonPageResult(page);
            return BaseResult.ok(pageVO);
        }
    }

    @ApiOperation(value = "添加角色数据权限")
    @PostMapping(value = "/addSystemRoleActionData")
    public BaseResult addSystemRoleActionData(@RequestBody AddSysRoleActionDataForm form) {

        if (form == null) {
            return BaseResult.ok();
        }

        boolean result = systemRoleActionDataService.addSystemRoleActionData(form);
        if (result) {
            return BaseResult.ok();
        }
        return BaseResult.error(444, "添加角色审核权限失败");
    }

    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysRoleActionData>
     **/
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<SysRoleActionData> queryById(@RequestParam(name = "roleId", required = true) Long roleId) {
        QueryWrapper<SysRoleActionData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleActionData::getRoleId,roleId);
        SysRoleActionData sysRoleActionData = systemRoleActionDataService.getOne(queryWrapper);
        return BaseResult.ok(sysRoleActionData);
    }


    @ApiOperation(value = "判断是否有按钮权限")
    @PostMapping(value = "/isPermission")
    public BaseResult isPermission(@RequestBody PermissionForm form) {

        //获取登录用户
        SysUserVO systemUser = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        //获取按钮信息
        SysMenu sysMenu = sysMenuService.getSysMenuByMenuCode(form.getCode());
        if(null == sysMenu){
            BaseResult.error(444,"按钮code不存在");
        }

        //是否为管理员
        int count = sysUserRoleService.getCountByUserNameAndRoleName(systemUser.getName(),"super_admin",systemUser.getTenantCode());
        if(count<=0){
            //获取登录用户所属角色
            List<SysRole> enabledRolesByUserId = sysUserRoleService.getEnabledRolesByUserId(systemUser.getId());
            List<Long> longs = new ArrayList<>();
            for (SysRole systemRole : enabledRolesByUserId) {
                longs.add(systemRole.getId());

            }
            List<SysRoleMenu> systemRoleActions = sysRoleMenuService.getSystemRoleMenuByRoleIdsAndActionCode(longs,sysMenu.getId());
            if(CollectionUtil.isEmpty(systemRoleActions)){
                return BaseResult.error(444,"该用户没有该按钮权限");
            }
        }
        return BaseResult.ok();
    }
}

