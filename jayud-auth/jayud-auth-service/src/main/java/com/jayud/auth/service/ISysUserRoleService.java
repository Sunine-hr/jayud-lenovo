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




    boolean exitByRolesIds(List<Long> rolesIds);

    void associatedEmployees(Long rolesId, List<Long> userIds);

    IPage<SysUserVO> selectAssociatedEmployeesPage(Long rolesId, Integer currentPage, Integer pageSize, HttpServletRequest req);

    void deleteEmployees(Long rolesId, List<Long> userIds);
}
