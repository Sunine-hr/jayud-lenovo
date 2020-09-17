package com.jayud.oauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.po.SystemRole;
import com.jayud.oauth.model.po.SystemRoleMenuRelation;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
public interface ISystemRoleMenuRelationService extends IService<SystemRoleMenuRelation> {

    /**
     * 创建角色与菜单关联
     * @param role
     * @param menuIds
     */
    void createRelation(SystemRole role, List<Long> menuIds);

    /**
     * 根据角色ID查询关联菜单
     * @param roleId
     * @return
     */
    List<SystemRoleMenuRelation> findRelationByRoleId(Long roleId);

    /**
     * 根据角色ID删除菜单关系
     * @param roleIds
     * @return
     */
    boolean removeRelationByRoleId(List<Long> roleIds);

}
