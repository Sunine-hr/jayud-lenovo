package com.jayud.auth.service.impl;

import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.dto.AuthUserDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ciro
 * @date 2022/2/21 14:29
 * @description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUserName(null,username);
        AuthUserDetail authUserDetail = new AuthUserDetail();
        BeanUtils.copyProperties(sysUser,authUserDetail);
        authUserDetail.setUsername(sysUser.getName());
        authUserDetail.setRealName(sysUser.getUserName());
        List<SysRole> roleList = sysRoleService.selectRoleByUsername(username);
        List<String> roleCodes = roleList.stream().map(x->x.getRoleCode()).collect(Collectors.toList());
        authUserDetail.setAuthorities(AuthorityUtils.createAuthorityList(roleCodes.toArray(new String[]{})));
        return authUserDetail;
    }
}
