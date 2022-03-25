package com.jayud.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.service.ISysMenuService;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ciro
 * @date 2022/2/17 13:32
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/sysMenu")
@Api(tags = "菜单信息")
public class SysMenuController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 获取当前用户的菜单
     * @return
     */
    @ApiOperation(value = "获取当前用户的菜单")
    @PostMapping(value = "/getUserMenuByToken")
    public BaseResult getUserMenuByToken(){
        //JSONObject jsonObject = JSONObject.parseObject(menu);
        JSONObject jsonObject = sysMenuService.getUserMenuByToken();
        return BaseResult.ok(jsonObject);
    }

    /**
     * 获取所有的菜单树
     * @return
     */
    @ApiOperation(value = "获取所有的菜单")
    @PostMapping(value = "allMenuTree")
    public BaseResult<List<SysMenu>> allMenuTree(@RequestBody SysMenu sysMenu){
        List<SysMenu> tree = sysMenuService.allMenuTree(sysMenu);
        return BaseResult.ok(tree);
    }

    /**
     * 获取所有是审核的菜单树
     * @return
     */
    @ApiOperation(value = "获取所有是审核的菜单树")
    @PostMapping(value = "allCheckMenuTree")
    public BaseResult<List<SysMenu>> allCheckMenuTree(){
        List<SysMenu> tree = sysMenuService.allCheckMenuTree();
        return BaseResult.ok(tree);
    }

    /**
     * 分页查询数据
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<SysMenu>> selectPage(@RequestBody SysMenu sysMenu,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysMenuService.selectPage(sysMenu,sysMenu.getCurrentPage(),sysMenu.getPageSize(),req));
    }

    /**
     * 新增or编辑
     */
    @ApiOperation(value = "新增OR编辑")
    @PostMapping(value = "saveSysMenu")
    public BaseResult saveSysMenu(@RequestBody SysMenu sysMenu){
        sysMenuService.saveSysMenu(sysMenu);
        return BaseResult.ok();
    }

    /**
     * 根据租户查询菜单树
     * @return
     */
    @ApiOperation(value = "根据租户查询菜单树")
    @GetMapping(value = "selectMenuTreeByTenantCode")
    public BaseResult<List<SysMenu>> selectMenuTreeByTenantCode(){
        return BaseResult.ok(sysMenuService.selectMenuTreeByTenantCode(CurrentUserUtil.getUserTenantCode()));
    }

    /**
     * 批量删除菜单
     */
    @ApiOperation(value = "批量删除菜单(逻辑删除)")
    @PostMapping(value = "batchDelete")
    public BaseResult batchDelete(@RequestBody DeleteForm form){
        sysMenuService.batchDelete(form);
        return BaseResult.ok();
    }

    /**
     * 导出菜单
     */
    @ApiOperation(value = "导出菜单")
    @PostMapping(path = "/exportSysMenu")
    public void exportSysMenu(HttpServletResponse response, @RequestBody SysMenu sysMenu){
        try {
            List<String> headList = Arrays.asList(
                    "菜单名称",
                    "菜单编码",
                    "菜单图标",
                    "菜单路由",
                    "菜单排序",
                    "菜单状态"
            );
            List<LinkedHashMap<String, Object>> dataList = sysMenuService.exportSysMenu(sysMenu);
            ExcelUtils.exportExcel(headList, dataList, "菜单信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 获取当前用户的菜单及其按钮
     * @return
     */
    @ApiOperation(value = "获取当前用户的菜单及其按钮")
    @PostMapping(value = "/getUserMenuBtnByToken")
    public BaseResult getUserMenuBtnByToken(){
        //JSONObject jsonObject = JSONObject.parseObject(menu);
        JSONObject jsonObject = sysMenuService.getUserMenuBtnByToken();
        return BaseResult.ok(jsonObject);
    }



}
