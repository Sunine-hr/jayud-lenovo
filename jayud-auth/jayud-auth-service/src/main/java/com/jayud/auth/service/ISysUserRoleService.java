package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.vo.SysUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户-角色关联表 服务类
 *
 * @author jayud
 * @since 2022-02-21
 * @since 2022-02-22
 */
public interface ISysUserRoleService extends IService<SysUserRole> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @date   2022-02-22
     * @param: sysUserRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUserRole>
     **/
    IPage<SysUserRole> selectPage(SysUserRole sysUserRole,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @date   2022-02-22
     * @param: sysUserRole
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysUserRole>
     **/
    List<SysUserRole> selectList(SysUserRole sysUserRole);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-21
     * @date   2022-02-22
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-21
    * @date   2022-02-22
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);


    /**
     * 是否存在员工绑定角色
     * @param rolesIds
     * @return
     */
    boolean exitByRolesIds(List<Long> rolesIds);

    /**
     * 关联员工
     * @param rolesId
     * @param userIds
     */
    void associatedEmployees(Long rolesId, List<Long> userIds);

    /**
     * 分页查询关联员工
     * @param rolesId
     * @param currentPage
     * @param pageSize
     * @param req
     * @return
     */
    IPage<SysUserVO> selectAssociatedEmployeesPage(Long rolesId, Integer currentPage, Integer pageSize, HttpServletRequest req);

    /**
     * 解绑员工
     * @param rolesId
     * @param userIds
     */
    void deleteEmployees(Long rolesId, List<Long> userIds);

    /**
     * 根据条件查询用户绑定角色
     * @param sysUserRole
     * @return
     */
    List<SysUserRole> getByCondition(SysUserRole sysUserRole);

    /**
     * 根据用户id逻辑删除绑定角色
     * @param userId
     */
    void deleteByUserId(Long userId);

    /**
     * 根据角色id查询绑定用户集合id
     * @param roleId
     * @return
     */
    List<Long> getUserIdsByRoleId(Long roleId);
}
