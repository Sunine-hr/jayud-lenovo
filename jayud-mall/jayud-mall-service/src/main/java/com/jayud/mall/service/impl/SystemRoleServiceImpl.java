package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.SystemRoleMapper;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.po.SystemRoleMenuRelation;
import com.jayud.mall.model.vo.SystemRoleVO;
import com.jayud.mall.service.ISystemRoleMenuRelationService;
import com.jayud.mall.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

    @Autowired
    SystemRoleMapper roleMapper;

    @Autowired
    ISystemRoleMenuRelationService roleMenuRelationService;

    @Autowired
    BaseService baseService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SaveRoleForm saveRoleForm) {
        SystemRole systemRole = ConvertUtil.convert(saveRoleForm, SystemRole.class);

        AuthUser user = baseService.getUser();//当前登录人
        if(systemRole.getId() == null){
            systemRole.setCreateBy(user.getId().toString());
            systemRole.setCreateTime(LocalDateTime.now());
        }
        systemRole.setUpdateBy(user.getId().toString());
        systemRole.setUpdateTime(LocalDateTime.now());
        //1.保存角色
        this.saveOrUpdate(systemRole);

        Integer roleId = systemRole.getId();
        QueryWrapper<SystemRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        //2.删除角色关联菜单
        roleMenuRelationService.remove(queryWrapper);

        List<Long> menuIds = saveRoleForm.getMenuIds();
        List<SystemRoleMenuRelation> systemRoleMenuRelations = new ArrayList<>();
        menuIds.forEach(menuId -> {
            SystemRoleMenuRelation systemRoleMenuRelation = new SystemRoleMenuRelation();
            systemRoleMenuRelation.setRoleId(Long.valueOf(roleId));
            systemRoleMenuRelation.setMenuId(menuId);
            systemRoleMenuRelations.add(systemRoleMenuRelation);
        });
        //3.保存角色关联菜单
        roleMenuRelationService.saveOrUpdateBatch(systemRoleMenuRelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        //删除角色
        this.removeById(id);
        //删除角色菜单
        QueryWrapper<SystemRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", id);
        roleMenuRelationService.remove(queryWrapper);
    }

    @Override
    public SystemRoleVO getRole(Long id) {
        SystemRole systemRole = this.getById(id);
        SystemRoleVO roleVO = ConvertUtil.convert(systemRole, SystemRoleVO.class);

        QueryWrapper<SystemRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("menu_id");
        queryWrapper.eq("role_id", systemRole.getId());
        List<SystemRoleMenuRelation> systemRoleMenuRelations = roleMenuRelationService.list(queryWrapper);
        List<Long> menuIds = new ArrayList<>();
        systemRoleMenuRelations.forEach(systemRoleMenuRelation -> {
            Long menuId = systemRoleMenuRelation.getMenuId();
            menuIds.add(menuId);
        });
        roleVO.setMenuIds(menuIds);
        return roleVO;
    }

    @Override
    public IPage<SystemRoleVO> findRoleByPage(QueryRoleForm form) {
        //定义分页参数
        Page<SystemRoleVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SystemRoleVO> pageInfo = baseMapper.findRoleByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<SystemRole> selectRolesByUserId(Long userId) {
        List<SystemRole> list = roleMapper.selectRolesByUserId(userId);
        return list;
    }

    @Override
    public List<SystemRoleVO> findRole(QueryRoleForm form) {
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper<>();
        String roleName = form.getRoleName();
        if(roleName != null && roleName != ""){
            queryWrapper.eq("role_name", roleName);
        }
        List<SystemRole> list = this.list(queryWrapper);
        List<SystemRoleVO> systemRoleVOS = ConvertUtil.convertList(list, SystemRoleVO.class);
        return systemRoleVOS;
    }
}
