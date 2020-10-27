package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.po.SystemUserRoleRelation;

import java.util.List;

/**
 * <p>
 * 用户对应角色 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
public interface ISystemUserRoleRelationService extends IService<SystemUserRoleRelation> {

    /**
     * 创建用户角色关联信息
     * @param user
     * @param roleIds
     */
    void createUserRoleRelation(SystemUser user, List<Long> roleIds);

    /**
     * 移除用户角色关联信息
     * @param userIds
     */
    void removeUserRoleRelation(List<Long> userIds);

    /**
     * 根据userId，查询用户角色关联信息
     * @param userId
     * @return
     */
    List<SystemUserRoleRelation> findRoleMenuRelationByRoleId(Long userId);


}
