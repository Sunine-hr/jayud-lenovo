package com.jayud.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.system.entity.SysUser;
import com.jayud.system.mapper.SysUserMapper;
import com.jayud.system.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author ciro
 * @since 2022-02-15
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
