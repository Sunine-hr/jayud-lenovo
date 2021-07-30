package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jayud.scm.mapper.SystemRoleMenuRelationMapper;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemRoleMenuRelation;
import com.jayud.scm.service.ISystemRoleMenuRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Service
public class SystemRoleMenuRelationServiceImpl extends ServiceImpl<SystemRoleMenuRelationMapper, SystemRoleMenuRelation> implements ISystemRoleMenuRelationService {

    /**
     * 根据角色ID查询关联菜单
     * @param roleId
     * @return
     */
    @Override
    public List<SystemRoleMenuRelation> findRelationByRoleId(Long roleId){
        QueryWrapper<SystemRoleMenuRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        return this.list(wrapper);
    }

    /**
     * 创建角色与菜单关联
     * @param role
     * @param menuIds
     */
    @Override
    public void createRelation(SystemRole role, List<Long> menuIds){
        menuIds.forEach(m -> {
            SystemRoleMenuRelation relation = new SystemRoleMenuRelation();
            relation.setRoleId(role.getId());
            relation.setMenuId(m);
            relation.insert();
        });

    }

    @Override
    public boolean removeRelationByRoleId(List<Long> roleIds){
        return baseMapper.removeRelationByRoleId(roleIds);
    }

}
