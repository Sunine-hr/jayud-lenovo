package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.dto.SysUserDTO;
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
     * @author jayud
     * @date 2022-02-21
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
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysUser>
     **/
    List<SysUserVO> selectList(SysUser sysUser);

    //根据id集合查询用户
    List<SysUserVO> selectIdsList(SysUserForm sysUserForm);

    /**
     * \
     * 新增或者修改
     *
     * @param sysUserForm
     * @return
     */
    boolean saveOrUpdateSysUser(SysUserForm sysUserForm);

    //校验用户名
    SysUserVO findSysUserName(SysUserForm sysUserForm);

    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult deleteSysUser(List<Long> ids);

    //编辑 根据id查询信息
    SysUserVO findSysUserIdOne(SysUserForm sysUserForm);
    /**
     * @description 根据租户
     * @author  ciro
     * @date   2022/2/22 14:18
     * @param: tenantCode
     * @param: name
     * @return: com.jayud.auth.model.po.SysUser
     **/
    SysUser getUserByUserName(String tenantCode,String name);


    /**
     * @description 判断用户状态
     * @author  ciro
     * @date   2022/2/22 15:01
     * @param: tenantCode
     * @param: name
     * @param: password
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult checkUserStatus(String tenantCode,String name,String password);



    BaseResult findUpdateUserPassword(SysUserForm sysUserForm);

    /**
     * 根据用户名查找用户
     * @param token
     * @return
     */
    SysUserVO getSystemUserByName(String token);

    /**
     * @description 根据角色编码查询用户
     * @author  ciro
     * @date   2022/3/3 9:52
     * @param: roleCode
     * @return: java.util.List<com.jayud.auth.model.dto.SysUserDto>
     **/
    List<SysUserDTO> selectUserByRoleCode(String roleCode);
}
