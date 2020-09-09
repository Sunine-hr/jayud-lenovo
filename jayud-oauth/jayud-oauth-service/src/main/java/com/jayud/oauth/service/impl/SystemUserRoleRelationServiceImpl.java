package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUserRoleRelation;
import com.jayud.oauth.mapper.SystemUserRoleRelationMapper;
import com.jayud.oauth.service.ISystemUserRoleRelationService;
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


}
