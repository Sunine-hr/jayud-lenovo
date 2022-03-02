package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.mapper.SysMenuMapper;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUrl;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.service.ISysUrlService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.utils.CurrentUserUtil;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author jayud.dev
 * @since 2022-02-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUrlService sysUrlService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public JSONObject getUserMenuByToken() {
        CurrentUserUtil.getUserToken();
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        Long userId = userDetail.getId();
        SysUser sysUser = sysUserService.getById(userId);
        //租户编码
        String tenantCode = sysUser.getTenantCode();

        List<SysRole> roles = sysRoleService.selectSysRoleByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(roles)){
            roles.forEach(role -> {
                roleIds.add(role.getId());
            });
        }else{
            roleIds.add(0L);
        }

        // 原始的数据一条一条的
        List<SysMenu> menus = this.selectSysMenuByRoleIds(roleIds);
        // 构建好的菜单树，第一层菜单的pid是0
        List<SysMenu> menuTree = buildMenuTree(menus, "0");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roleIds", roleIds);
        jsonObject.put("roles", roles);
        jsonObject.put("menuNodeList", menuTree);
        return jsonObject;
    }

    @Override
    public List<SysMenu> selectSysMenuByRoleIds(List<Long> roleIds) {
        return baseMapper.selectSysMenuByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> allMenuTree(SysMenu sysMenu) {
        List<SysMenu> menus = baseMapper.allMenuTree(sysMenu);
        List<SysMenu> menuTree = buildMenuTree(menus, "0");
        return menuTree;
    }

    @Override
    public List<SysMenu> selectSysMenuByMenuCodes(List<String> menuCodeList) {
        return baseMapper.selectSysMenuByMenuCodes(menuCodeList);
    }

    @Override
    public IPage<SysMenu> selectPage(SysMenu sysMenu, Integer currentPage, Integer pageSize, HttpServletRequest req) {
        //根据菜单id，查询菜单的所有子集ids
        Long menuId = sysMenu.getId();
        if(ObjectUtil.isNotEmpty(menuId)){
            List<SysMenu> menuChildren = sysMenuMapper.selectMenuChildren(menuId);
            List<Long> childrenIds = menuChildren.stream().map(menu -> menu.getId()).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(childrenIds)){
                sysMenu.setChildrenIds(childrenIds);
            }else{
                sysMenu.setChildrenIds(Arrays.asList(0L));
            }
        }
        Page<SysMenu> page=new Page<SysMenu>(currentPage,pageSize);
        IPage<SysMenu> pageList= sysMenuMapper.pageList(page, sysMenu);
        return pageList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysMenu(SysMenu sysMenu) {
        Long id = sysMenu.getId();
        SysMenu menu = this.getById(id);
        if(ObjectUtil.isEmpty(menu)){
            //新增
            String name = sysMenu.getName();
            QueryWrapper<SysMenu> nameQueryWrapper = new QueryWrapper<>();
            nameQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            nameQueryWrapper.lambda().eq(SysMenu::getName, name);
            nameQueryWrapper.lambda().groupBy(SysMenu::getName);
            SysMenu nameOne = this.getOne(nameQueryWrapper);
            if(ObjectUtil.isNotEmpty(nameOne)){
                throw new IllegalArgumentException("name，前端名称，已存在");
            }
            String code = sysMenu.getCode();
            QueryWrapper<SysMenu> codeQueryWrapper = new QueryWrapper<>();
            codeQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            codeQueryWrapper.lambda().eq(SysMenu::getCode, code);
            codeQueryWrapper.lambda().groupBy(SysMenu::getCode);
            SysMenu codeOne = this.getOne(codeQueryWrapper);
            if(ObjectUtil.isNotEmpty(codeOne)){
                throw new IllegalArgumentException("code，前端编码，已存在");
            }
            String router = sysMenu.getRouter();
            QueryWrapper<SysMenu> routerQueryWrapper = new QueryWrapper<>();
            routerQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            routerQueryWrapper.lambda().eq(SysMenu::getRouter, router);
            routerQueryWrapper.lambda().groupBy(SysMenu::getCode);
            SysMenu routerOne = this.getOne(routerQueryWrapper);
            if(ObjectUtil.isNotEmpty(routerOne)){
                throw new IllegalArgumentException("router，前端路由，已存在");
            }

            sysMenu.setCreateBy(CurrentUserUtil.getUsername());
            sysMenu.setCreateTime(new Date());

        }else{
            //修改
            String name = sysMenu.getName();
            QueryWrapper<SysMenu> nameQueryWrapper = new QueryWrapper<>();
            nameQueryWrapper.lambda().ne(SysMenu::getId, id);
            nameQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            nameQueryWrapper.lambda().eq(SysMenu::getName, name);
            nameQueryWrapper.lambda().groupBy(SysMenu::getName);
            SysMenu nameOne = this.getOne(nameQueryWrapper);
            if(ObjectUtil.isNotEmpty(nameOne)){
                throw new IllegalArgumentException("name，前端名称，已存在");
            }
            String code = sysMenu.getCode();
            QueryWrapper<SysMenu> codeQueryWrapper = new QueryWrapper<>();
            codeQueryWrapper.lambda().ne(SysMenu::getId, id);
            codeQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            codeQueryWrapper.lambda().eq(SysMenu::getCode, code);
            codeQueryWrapper.lambda().groupBy(SysMenu::getCode);
            SysMenu codeOne = this.getOne(codeQueryWrapper);
            if(ObjectUtil.isNotEmpty(codeOne)){
                throw new IllegalArgumentException("code，前端编码，已存在");
            }
            String router = sysMenu.getRouter();
            QueryWrapper<SysMenu> routerQueryWrapper = new QueryWrapper<>();
            routerQueryWrapper.lambda().ne(SysMenu::getId, id);
            routerQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            routerQueryWrapper.lambda().eq(SysMenu::getRouter, router);
            routerQueryWrapper.lambda().groupBy(SysMenu::getCode);
            SysMenu routerOne = this.getOne(routerQueryWrapper);
            if(ObjectUtil.isNotEmpty(routerOne)){
                throw new IllegalArgumentException("router，前端路由，已存在");
            }

            sysMenu.setUpdateBy(CurrentUserUtil.getUsername());
            sysMenu.setUpdateTime(new Date());
        }
        this.saveOrUpdate(sysMenu);
    }

    @Override
    public List<SysMenu> selectMenuTreeByTenantCode(String tenantCode) {
        List<SysUrl> urlList = sysUrlService.getSystemByTenantCode(CurrentUserUtil.getUserTenantCode());
        List<SysMenu> menuList = new ArrayList<>();
        if (CollUtil.isNotEmpty(urlList)) {
            List<Integer> typeList = urlList.stream().map(x -> x.getType()).collect(Collectors.toList());
            SysMenu sysMenu = new SysMenu();
            sysMenu.setInSysTypeList(typeList);
            menuList = allMenuTree(sysMenu);
        }
        return menuList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(DeleteForm form) {
        List<Long> ids = form.getIds();
        if(CollUtil.isEmpty(ids)){
            throw new IllegalArgumentException("ids不能为空");
        }
        List<SysMenu> sysMenus = new ArrayList<>();
        for (int i=0; i<ids.size(); i++){
            Long menuId = ids.get(i);
            SysMenu sysMenu = this.getById(menuId);
            QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
            sysMenuQueryWrapper.lambda().eq(SysMenu::getIsDeleted, 0);
            sysMenuQueryWrapper.lambda().eq(SysMenu::getParentId, menuId);
            sysMenuQueryWrapper.lambda().groupBy(SysMenu::getParentId);
            SysMenu one = this.getOne(sysMenuQueryWrapper);
            if(ObjectUtil.isNotEmpty(one)){
                throw new IllegalArgumentException("ids集合中的菜单存在子菜单，不能删除");
            }
            sysMenu.setIsDeleted(true);
            sysMenu.setUpdateBy(CurrentUserUtil.getUsername());
            sysMenu.setUpdateTime(new Date());
            sysMenus.add(sysMenu);
        }
        if(CollUtil.isEmpty(sysMenus)){
            throw new IllegalArgumentException("没有找到对应的菜单");
        }
        this.saveOrUpdateBatch(sysMenus);
    }

    @Override
    public List<LinkedHashMap<String, Object>> exportSysMenu(SysMenu sysMenu) {
        return baseMapper.exportSysMenu(sysMenu);
    }

    @Override
    public JSONObject getUserMenuBtnByToken() {
        CurrentUserUtil.getUserToken();
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        Long userId = userDetail.getId();
        SysUser sysUser = sysUserService.getById(userId);
        //租户编码
        String tenantCode = sysUser.getTenantCode();

        List<SysRole> roles = sysRoleService.selectSysRoleByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(roles)){
            roles.forEach(role -> {
                roleIds.add(role.getId());
            });
        }else{
            roleIds.add(0L);
        }

        // 原始的数据一条一条的
        List<SysMenu> menus = this.selectSysMenuBtnByRoleIds(roleIds);
        // 构建好的菜单树，第一层菜单的pid是0
        List<SysMenu> menuTree = buildMenuTree(menus, "0");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roleIds", roleIds);
        jsonObject.put("roles", roles);
        jsonObject.put("menuNodeList", menuTree);
        return jsonObject;
    }

    @Override
    public List<SysMenu> selectSysMenuBtnByRoleIds(List<Long> roleIds) {
        return baseMapper.selectSysMenuBtnByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> getByIds(List<Integer> actionIds) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysBaseEntity::getId,actionIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 构建菜单树
     *
     * @param menuList
     * @param pid
     * @return
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList, String pid) {
        List<SysMenu> treeList = new ArrayList<>();
        menuList.forEach(menu -> {
            if (StrUtil.equals(pid, menu.getParentId().toString())) {
                menu.setChildren(buildMenuTree(menuList, menu.getId().toString()));
                treeList.add(menu);
            }
        });
        return treeList;
    }

}
