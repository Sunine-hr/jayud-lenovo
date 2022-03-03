package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.dto.AddSysRole;
import com.jayud.auth.model.po.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.vo.SysRoleVO;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色表 服务类
 *
 * @author jayud
 * @since 2022-02-21
 */
public interface ISysRoleService extends IService<SysRole> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: sysRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRole>
     **/
    IPage<SysRoleVO> selectPage(SysRole sysRole,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysRole
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysRole>
     **/
    List<SysRole> selectList(SysRole sysRole);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-21
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);


    /**
     * 根据用户查询角色
     * @param userId
     * @return
     */
    List<SysRole> selectSysRoleByUserId(Long userId);

    /**
     * 是否唯一
     * @param id
     * @param roleName
     * @param roleCode
     * @return
     */
    boolean checkUnique(Long id, String roleName, String roleCode);

    /**
     * 创建/编辑角色
     * @param form
     */
    void addOrUpdate(AddSysRole form);

    /**
     * 根据用户id查询角色id集合
     * @param userId
     * @return
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 设置角色
     * @param userId
     * @param roleIds
     */
    void setRoles(Long userId, List<Long> roleIds);


    /**
     * 设置角色权限
     * @param rolesId
     * @param menuIds
     */
    void setRolePermissions(Long rolesId, List<Long> menuIds);

    /**
     * @description 根据用户名称查询角色
     * @author  ciro
     * @date   2022/2/24 10:48
     * @param: username
     * @return: java.util.List<com.jayud.auth.model.po.SysRole>
     **/
    List<SysRole> selectRoleByUsername(String username);

    /**
     * @description 根据租户查询角色
     * @author  ciro
     * @date   2022/3/3 18:40
     * @param: tenantCode
     * @return: java.util.List<com.jayud.auth.model.po.SysRole>
     **/
    List<SysRole> getRoleByTenantCode(String tenantCode);

}
