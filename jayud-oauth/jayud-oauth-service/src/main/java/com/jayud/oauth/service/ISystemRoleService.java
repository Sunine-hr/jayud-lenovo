package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.SystemRole;
import com.jayud.model.vo.SystemRoleVO;

import java.util.List;

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


}
