package com.jayud.oauth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.po.SystemRole;
import com.jayud.oauth.model.vo.SystemRoleVO;
import com.jayud.oauth.model.bo.QueryRoleForm;
import com.jayud.oauth.model.vo.SystemRoleView;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
public interface ISystemRoleService extends IService<SystemRole> {


    /**
     * 获取用户角色
     * @param userId
     * @return
     */
    List<SystemRoleVO> getRoleList(Long userId);

    /**
     * 角色新增,需返回新增记录的主键
     * @param role
     */
    Long saveRole(SystemRole role);


    /**
     * 获取所有角色
     * @return
     */
    List<SystemRoleVO> findRole();

    /**
     * 用户列表查询
     * @param form
     * @return
     */
    IPage<SystemRoleView> findRoleByPage(QueryRoleForm form);

    /**
     * 根据条件查询角色
     * @param param
     * @return
     */
    SystemRole getRoleByCondition(Map<String,Object> param);

}
