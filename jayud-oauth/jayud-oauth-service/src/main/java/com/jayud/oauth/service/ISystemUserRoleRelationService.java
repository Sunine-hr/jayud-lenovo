package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUserRoleRelation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
public interface ISystemUserRoleRelationService extends IService<SystemUserRoleRelation> {
    /**
     * 根据用户ID查询可用的角色
     * @param id
     * @return
     */
    List<SystemRole> getEnabledRolesByUserId(Long id);

    /**
     * 批量删除关系
     * @param roleIds
     * @return
     */
    boolean removeRelationByRoleId(List<Long> roleIds);

    /**
     * 创建角色与用户的关联
     * @param roleId
     * @param userId
     */
    void createRelation(Long roleId, Long userId);

    /**
     * 批量删除关系
     * @param userIds
     * @return
     */
    boolean removeRelationByUserId(List<Long> userIds);
}
