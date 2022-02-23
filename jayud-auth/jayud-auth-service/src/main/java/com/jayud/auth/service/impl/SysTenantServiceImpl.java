package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysTenantForm;
import com.jayud.auth.model.po.*;
import com.jayud.auth.service.*;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.enums.SystemTypeEnum;
import com.jayud.common.utils.PasswordUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.mapper.SysTenantMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 多租户信息表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private ISysTenantToSystemService sysTenantToSystemService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public IPage<SysTenant> selectPage(SysTenant sysTenant,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysTenant> page=new Page<SysTenant>(currentPage,pageSize);
        IPage<SysTenant> pageList= sysTenantMapper.pageList(page, sysTenant);
        return pageList;
    }

    @Override
    public List<SysTenant> selectList(SysTenant sysTenant){
        return sysTenantMapper.list(sysTenant);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysTenantMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
//        sysTenantMapper.deleteById(id);
        sysTenantMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveTenant(SysTenantForm sysTenantForm) {
        boolean isAdd = false;
        if (sysTenantForm.getId() == null){
            isAdd = true;
        }
        if (!checkSameCode(isAdd,sysTenantForm)){
            if (isAdd){
                this.save(sysTenantForm);
                //初始化租户数据
                initCreateTenant(sysTenantForm);
            }else {
                this.updateById(sysTenantForm);
            }
            sysTenantToSystemService.saveTenantSystemRelation(sysTenantForm);
            if (isAdd){
                return BaseResult.ok(SysTips.ADD_SUCCESS);
            }else {
                return BaseResult.ok(SysTips.EDIT_SUCCESS);
            }
        }
        return BaseResult.ok(SysTips.TENANT_CODE_SAME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initCreateTenant(SysTenantForm sysTenantForm) {
        addTenantToSystem(sysTenantForm);
        SysRole sysRole = addTenantToRole(sysTenantForm.getTenantCode());
        addTenantRoleToMenu(sysTenantForm.getTenantCode(),sysRole.getId());
        SysUser sysUser = addTenantUser(sysTenantForm.getTenantCode());
        addTenantUserToRole(sysUser.getId(),sysRole.getId());
    }


    /**
     * @description 判断是否有相同编码
     * @author  ciro
     * @date   2022/2/22 10:50
     * @param: isAdd
     * @param: sysTenantForm
     * @return: boolean
     **/
    private boolean checkSameCode(boolean isAdd,SysTenantForm sysTenantForm){
        SysTenant checks = new SysTenant();
        checks.setTenantCode(sysTenantForm.getTenantCode());
        List<SysTenant> tenantList = selectList(checks);
        if (isAdd){
            if (CollUtil.isNotEmpty(tenantList)){
                return true;
            }else {
                return false;
            }
        }else {
            if (CollUtil.isEmpty(tenantList)){
                return false;
            }else {
                if (tenantList.get(0).getId().equals(sysTenantForm.getId())){
                    return false;
                }
            }
        }
        return true;
    }

    //新增租户初始数据--start
    /**
     * @description 分配租户-系统
     * @author  ciro
     * @date   2022/2/23 14:11
     * @param: tenantId
     * @return: void
     **/
    private void addTenantToSystem(SysTenantForm sysTenantForm){
        List<Integer> typeList = sysTenantForm.getSysUrlList().stream().map(x->x.getType()).collect(Collectors.toList());
        if (!typeList.contains(SystemTypeEnum.AUTH.getType())) {
            SysTenantToSystem tenantToSystem = new SysTenantToSystem();
            tenantToSystem.setTenantId(sysTenantForm.getId());
            tenantToSystem.setSystemId(1L);
            sysTenantToSystemService.save(tenantToSystem);
        }
    }


    /**
     * @description 新增租户-角色
     * @author  ciro
     * @date   2022/2/23 14:20
     * @param: tenantCode
     * @return: com.jayud.auth.model.po.SysRole
     **/
    private SysRole addTenantToRole(String tenantCode){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("超级管理员");
        sysRole.setRoleCode("super_admin");
        sysRole.setStatus(1);
        sysRole.setTenantCode(tenantCode);
        sysRole.setRemark("超级管理员");
        sysRoleService.save(sysRole);
        return sysRole;
    }

    /**
     * @description 新增租户-角色-菜单关联
     * @author  ciro
     * @date   2022/2/23 14:20
     * @param: tenantCode
     * @param: roleId
     * @return: void
     **/
    private void addTenantRoleToMenu(String tenantCode,Long roleId){
        List<String> menuCodeList = new ArrayList<>();
        menuCodeList.add("auth");
        menuCodeList.add("p_system");
        menuCodeList.add("p_organization");
        menuCodeList.add("p_employees");
        menuCodeList.add("p_role");
        List<SysMenu> sysMenuList = sysMenuService.selectSysMenuByMenuCodes(menuCodeList);
        if (CollUtil.isNotEmpty(sysMenuList)){
            List<SysRoleMenu> roleMenuList = new ArrayList<>();
            sysMenuList.forEach(sysMenu -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(sysMenu.getId());
                roleMenuList.add(sysRoleMenu);
            });
            sysRoleMenuService.saveBatch(roleMenuList);
        }
    }

    /**
     * @description 新增租户-创建管理员账号
     * @author  ciro
     * @date   2022/2/23 15:49
     * @param: tenantCode
     * @return: com.jayud.auth.model.po.SysUser
     **/
    private SysUser addTenantUser(String tenantCode){
        SysUser sysUser = new SysUser();
        sysUser.setName(tenantCode+"_admin");
        sysUser.setPassword(PasswordUtil.encodeOauthPassword("666888"));
        sysUser.setUserName("超级管理员");
        sysUser.setUserType("1");
        sysUser.setTenantCode(tenantCode);
        sysUserService.save(sysUser);
        return sysUser;
    }

    /**
     * @description 新增租户-保存用户和权限关系
     * @author  ciro
     * @date   2022/2/23 15:53
     * @param: userId
     * @param: roleIdList
     * @return: void
     **/
    private void addTenantUserToRole(Long userId,Long roleId){
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(roleId);
        sysUserRoleService.save(sysUserRole);
    }


    //新增租户初始数据--end
}
