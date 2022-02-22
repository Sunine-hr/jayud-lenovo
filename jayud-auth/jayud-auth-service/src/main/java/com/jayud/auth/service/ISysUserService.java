package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.po.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台用户表 服务类
 *
 * @author jayud
 * @since 2022-02-21
 */
public interface ISysUserService extends IService<SysUser> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUser
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUser>
     **/
    IPage<SysUserVO> selectPage(SysUserForm sysUserForm,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUser
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysUser>
     **/
    List<SysUserVO> selectList(SysUser sysUser);


    /**\
     * 新增或者修改
     * @param sysUserForm
     * @return
     */
    boolean saveOrUpdateSysUser(SysUserForm sysUserForm);
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
    BaseResult deleteSysUser(List<Long> ids);




}
