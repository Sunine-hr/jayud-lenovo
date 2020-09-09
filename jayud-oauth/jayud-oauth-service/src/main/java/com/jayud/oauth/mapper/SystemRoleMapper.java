package com.jayud.oauth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.model.bo.QueryRoleForm;
import com.jayud.model.po.SystemRole;
import com.jayud.model.vo.SystemRoleView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取用户列表分页
     * @param page
     * @param form
     * @return
     */
    IPage<SystemRoleView> findRoleByPage(Page page, @Param("form") QueryRoleForm form);

}
