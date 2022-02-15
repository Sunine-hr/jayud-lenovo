package com.jayud.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.entity.SysUser;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author ciro
 * @since 2022-02-15
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getUser();


}
