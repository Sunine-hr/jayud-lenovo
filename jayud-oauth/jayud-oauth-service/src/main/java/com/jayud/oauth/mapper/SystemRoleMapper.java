package com.jayud.oauth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.model.po.SystemRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-03
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    /**
     * 根据用户ID获取角色
     * @param userId
     * @return
     */
    List<SystemRole> getRoleList(Long userId);

    /**
     * 角色新增,需返回新增记录的主键
     * @param systemRole
     * @return
     */
    Long saveRole(SystemRole systemRole);

}
