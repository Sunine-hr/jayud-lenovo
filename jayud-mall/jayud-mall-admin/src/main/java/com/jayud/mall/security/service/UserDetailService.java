package com.jayud.mall.security.service;

import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.security.domain.MyUser;
import com.jayud.mall.service.ISystemRoleService;
import com.jayud.mall.service.ISystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户认证
 */
@Configuration
public class UserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>注入了`PasswordEncoder`对象，该对象用于密码加密，注入前需要手动配置。我们在`BrowserSecurityConfig`中配置</p>
     * <p>`PasswordEncoder`是一个密码加密接口，而`BCryptPasswordEncoder`是Spring Security提供的一个实现方法</p>
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * <p>用户服务</p>
     */
    @Autowired
    ISystemUserService userService;
    /**
     * <p>角色服务</p>
     */
    @Autowired
    ISystemRoleService roleService;

    /**
     * 重写用户登录验证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 模拟一个用户，替代数据库获取逻辑
//        MyUser user = new MyUser();
//        user.setUserName(username);
//        user.setPassword(this.passwordEncoder.encode("123456"));
//        // 输出加密后的密码
//        System.err.println(user.getPassword());
//
//        return new User(username, user.getPassword(), user.isEnabled(),
//                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
//                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        logger.debug("权限框架-加载用户");
        List<GrantedAuthority> auths = new ArrayList<>();

        SystemUserLoginForm loginForm = new SystemUserLoginForm();
        //登录名（手机号／邮箱）不能为空
        loginForm.setLoginname(username);
        SystemUserVO userVO = userService.login(loginForm);

        if (userVO == null) {
            logger.debug("找不到该用户 用户名:{}", username);
            throw new UsernameNotFoundException("找不到该用户！");
        }
        if(userVO.getStatus()==0) {
            logger.debug("用户账号未启用，无法登陆 用户名:{}", username);
            throw new UsernameNotFoundException("用户账号未启用！");
        }
        Long userId = userVO.getId();
        List<SystemRole> systemRoles = roleService.selectRolesByUserId(userId);
        if (systemRoles != null) {
            //设置角色名称
            for (SystemRole role : systemRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                auths.add(authority);
            }
        }
        User user = new User(userVO.getUserName(), userVO.getPassword(),
                true, true, true, true,
                auths);
        return user;
    }
}
