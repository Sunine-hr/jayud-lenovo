package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.po.SystemRoleMenuRelation;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
public interface ISystemRoleMenuRelationService extends IService<SystemRoleMenuRelation> {

    /**
     * 创建角色菜单关联信息
     * @param role
     * @param menuIds
     */
    void createRoleMenuRelation(SystemRole role, List<Long> menuIds);

    /**
     * 移除角色菜单关联信息
     * @param roleIds
     */
    void removeRoleMenuRelation(List<Long> roleIds);

    /**
     * 根据roleId，查询角色菜单关联信息
     * @param roleId
     * @return
     */
    List<SystemRoleMenuRelation> findRoleMenuRelationByRoleId(Long roleId);

}
