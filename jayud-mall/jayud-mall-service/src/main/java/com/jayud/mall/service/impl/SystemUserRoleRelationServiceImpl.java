package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.SystemUserRoleRelationMapper;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.jayud.mall.service.ISystemUserRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户对应角色 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class SystemUserRoleRelationServiceImpl extends ServiceImpl<SystemUserRoleRelationMapper, SystemUserRoleRelation> implements ISystemUserRoleRelationService {

    @Autowired
    SystemUserRoleRelationMapper userRoleRelationMapper;

    @Override
    public void createUserRoleRelation(SystemUser user, List<Long> roleIds) {
        roleIds.forEach(m -> {
            SystemUserRoleRelation relation = new SystemUserRoleRelation();
            relation.setRoleId(user.getId().intValue());
            relation.setRoleId(m.intValue());
            relation.insert();
        });
    }

    @Override
    public void removeUserRoleRelation(List<Long> userIds) {
        userRoleRelationMapper.removeUserRoleRelation(userIds);
    }

    @Override
    public List<SystemUserRoleRelation> findUserRoleRelationByUserId(Long userId) {
        QueryWrapper<SystemUserRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }

}
