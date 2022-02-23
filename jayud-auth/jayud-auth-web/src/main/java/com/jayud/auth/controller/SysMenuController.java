package com.jayud.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.service.ISysMenuService;
import com.jayud.common.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ciro
 * @date 2022/2/17 13:32
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 获取当前用户的菜单
     * @return
     */
    @ApiModelProperty(value = "获取当前用户的菜单")
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
    @ApiModelProperty(value = "获取所有的菜单")
    @PostMapping(value = "allMenuTree")
    public BaseResult<List<SysMenu>> allMenuTree(){
        List<SysMenu> tree = sysMenuService.allMenuTree();
        return BaseResult.ok(tree);
    }

    /**
     * 分页查询
     */




}
