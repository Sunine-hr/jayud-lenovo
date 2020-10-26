package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.SystemRoleMenuRelationMapper;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.po.SystemRoleMenuRelation;
import com.jayud.mall.service.ISystemRoleMenuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Service
public class SystemRoleMenuRelationServiceImpl extends ServiceImpl<SystemRoleMenuRelationMapper, SystemRoleMenuRelation> implements ISystemRoleMenuRelationService {

    @Autowired
    SystemRoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public void createRoleMenuRelation(SystemRole role, List<Long> menuIds) {
        menuIds.forEach(m -> {
            SystemRoleMenuRelation relation = new SystemRoleMenuRelation();
            relation.setRoleId(Long.valueOf(role.getId()));
            relation.setMenuId(m);
            relation.insert();
        });

    }

    @Override
    public void removeRoleMenuRelation(List<Long> roleIds) {
        roleMenuRelationMapper.removeRoleMenuRelation(roleIds);
    }

    @Override
    public List<SystemRoleMenuRelation> findRoleMenuRelationByRoleId(Long roleId) {
        QueryWrapper<SystemRoleMenuRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        return this.list(wrapper);
    }


}
