package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户-角色关联表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-22
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: page
     * @param: sysUserRole
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUserRole>
     **/
    IPage<SysUserRole> pageList(@Param("page") Page<SysUserRole> page, @Param("sysUserRole") SysUserRole sysUserRole);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysUserRole
     * @return: java.util.List<com.jayud.auth.model.po.SysUserRole>
     **/
    List<SysUserRole> list(@Param("sysUserRole") SysUserRole sysUserRole);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);



    int updateSysUserRoleMultiRow( @Param("sysUserRole") SysUserRole sysUserRole);

    /**
     * 判断用户有没有管理员权限
     * @param username
     * @param admin
     * @return
     */
    int getCountByUserNameAndRoleName(@Param("username") String username, @Param("admin")String admin,@Param("tenantCode") String tenantCode);

    /**
     * 判断该用户是否有按钮权限
     * @param username
     * @param userTenantCode
     * @return
     */
    int getCountByUserName(@Param("username")String username,@Param("userTenantCode") String userTenantCode,@Param("menuCode") String menuCode);

    /**
     * 判断用户是否有该级别的审核权限
     * @param username
     * @param userTenantCode
     * @param menuCode
     * @param newStep
     * @return
     */
    int getCountByUserNameAndStep(@Param("username")String username, @Param("userTenantCode")String userTenantCode, @Param("menuCode")String menuCode, @Param("newStep")Integer newStep);

    /**
     * 根据用户id获取角色信息
     * @param id
     * @return
     */
    List<SysRole> getEnabledRolesByUserId(@Param("id")Long id);
}
