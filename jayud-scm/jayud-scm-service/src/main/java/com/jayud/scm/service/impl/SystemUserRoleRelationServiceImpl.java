package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.mapper.SystemUserRoleRelationMapper;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemUserRoleRelation;
import com.jayud.scm.service.ISystemUserRoleRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Service
public class SystemUserRoleRelationServiceImpl extends ServiceImpl<SystemUserRoleRelationMapper, SystemUserRoleRelation> implements ISystemUserRoleRelationService {

    @Override
    public List<SystemRole> getEnabledRolesByUserId(Long id) {
        return baseMapper.getEnabledRolesByUserId(id);
    }

    @Override
    public boolean removeRelationByRoleId(List<Long> roleIds) {
        return baseMapper.removeRelationByRoleId(roleIds);
    }

    /**
     * 创建角色与用户的关联
     * @param roleId
     * @param userId
     */
    @Override
    public void createRelation(Long roleId, Long userId){
        SystemUserRoleRelation roleRelation = new SystemUserRoleRelation();
        roleRelation.setRoleId(roleId);
        roleRelation.setUserId(userId);
        saveOrUpdate(roleRelation);

    }

    @Override
    public boolean removeRelationByUserId(List<Long> userIds) {
        return baseMapper.removeRelationByUserId(userIds);
    }

    @Override
    public boolean isExistUserRelation(List<Long> roleIds) {
        List<SystemUserRoleRelation> userRoleRelations = baseMapper.isExistUserRelation(roleIds);
        if(userRoleRelations != null && userRoleRelations.size() > 0){
            return false;
        }
        return true;
    }


}
