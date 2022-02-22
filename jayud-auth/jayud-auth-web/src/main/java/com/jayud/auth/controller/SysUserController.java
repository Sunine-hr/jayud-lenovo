package com.jayud.auth.controller;

import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.vo.SysUserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysUserService;
import com.jayud.auth.model.po.SysUser;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 后台用户表 控制类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Api(tags = "后台用户表")
@RestController
@RequestMapping("/sysUser")
public class SysUserController {


    @Autowired
    public ISysUserService sysUserService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysUser>>
     **/
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<SysUserVO>> selectPage(@RequestBody SysUserForm sysUserForm, HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectPage(sysUserForm, sysUserForm.getCurrentPage(), sysUserForm.getPageSize(), req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysUser>>
     **/
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<SysUserVO>> selectList(@RequestBody SysUser sysUser,
                                                  HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectList(sysUser));
    }

    /**
     * @description 根据ids查询数据  没用到
     **/
    @ApiOperation("根据ids查询数据")
    @PostMapping("/selectIdsList")
    public BaseResult<List<SysUserVO>> selectIdsList(@RequestBody DeleteForm ids) {
        //需要写一个根据id集合查询列表信息
        SysUserForm sysUserForm = new SysUserForm();
        sysUserForm.setRoleIds(ids.getIds());
        List<SysUserVO> sysUserVOS = sysUserService.selectIdsList(sysUserForm);
        return BaseResult.ok(sysUserVOS);
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/saveOrUpdate")
    public BaseResult add(@RequestBody SysUserForm sysUserForm) {
        if (sysUserForm != null) {
            return BaseResult.error("数据不能为空！");
        }
        if (sysUserForm.getId() == null) {
            SysUserVO sysUserName = sysUserService.findSysUserName(sysUserForm);
            if (sysUserName != null) {
                return BaseResult.error("用户名已存在！");
            }
        }
        sysUserService.saveOrUpdateSysUser(sysUserForm);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


//    /**
//     * @description 编辑
//     * @author jayud
//     * @date 2022-02-21
//     * @param: sysUser
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody SysUser sysUser) {
//        sysUserService.updateById(sysUser);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }


//    /**
//     * @description 物理删除
//     * @author jayud
//     * @date 2022-02-21
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id) {
//        sysUserService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @PostMapping("/delSysUser")
    public BaseResult logicDel(@RequestBody DeleteForm ids) {

        if (ids.getIds().size() == 0) {
            return BaseResult.error("id不为空");
        }
        sysUserService.deleteSysUser(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUser>
     **/
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<SysUserVO> queryById(@RequestParam(name = "id", required = true) Long id) {
        SysUserVO sysUserIdOne = null;
        SysUserForm sysUserForm = new SysUserForm();
        sysUserForm.setId(id);
        //关联查询用户信息 关联表 行合并  成列
        sysUserIdOne = sysUserService.findSysUserIdOne(sysUserForm);

        List<Long> list = new ArrayList<>();
        String s = sysUserIdOne.getRoleListIdString();
        String[] a = s.split(",");
        for (int i = 0; i < a.length; i++) {
            list.add(Long.parseLong(a[i]));
        }
        sysUserIdOne.setRoleIds(list);
        return BaseResult.ok(sysUserIdOne);
    }


}
