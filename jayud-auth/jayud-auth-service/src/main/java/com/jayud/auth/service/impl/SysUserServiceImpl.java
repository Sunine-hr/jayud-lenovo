package com.jayud.auth.service.impl;

import com.jayud.auth.model.dto.AuthUserDetail;
import com.jayud.auth.model.entity.SysUser;
import com.jayud.auth.mapper.SysUserMapper;
import com.jayud.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = this.baseMapper.selectById(1);
        if (sysUser == null){
            throw new UsernameNotFoundException(s);
        }
        AuthUserDetail authUserDetail = new AuthUserDetail();
        BeanUtils.copyProperties(sysUser,authUserDetail);
        return authUserDetail;
//        return new User(sysUser.getName(), sysUser.getPassword(), AuthorityUtils.createAuthorityList(new String[]{}));
    }

    @Override
    public SysUser getUser() {
        return this.baseMapper.selectById(1);
    }
}
