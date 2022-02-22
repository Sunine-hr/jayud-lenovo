package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.dto.AddSysRole;
import com.jayud.auth.model.po.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    IPage<SysRole> selectPage(SysRole sysRole,
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

    boolean checkUnique(Long id, String roleName, String roleCode);

    void addOrUpdate(AddSysRole form);

    List<SysRole> getRoleByUserId(Long userId);
}
