package com.jayud.mall.security.service;

import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.domain.BaseAuthVO;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.security.utils.ContextHolderUtils;
import com.jayud.mall.service.ISystemRoleService;
import com.jayud.mall.service.ISystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
     * <p>前端密码</p>
     */
    private String passwordParameter = "password";

    /**
     * 获取HttpSession
     * @return
     */
    public HttpSession getHttpSession() {
        ServletRequestAttributes sa = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sa != null && sa.getRequest() != null ? sa.getRequest().getSession() : null;
    }

    /**
     * 重写用户登录验证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("权限框架-加载用户");
        HttpServletRequest request = ContextHolderUtils.getRequest();
        String password = request.getParameter(passwordParameter);
        logger.error("password = {}", password);

        List<GrantedAuthority> auths = new ArrayList<>();

        SystemUserLoginForm loginForm = new SystemUserLoginForm();
        //登录名（手机号／邮箱）不能为空
        loginForm.setLoginname(username);
        SystemUserVO userVO = userService.login(loginForm);
        //用户认证
        if (userVO == null) {
            logger.debug("找不到该用户 （手机号／邮箱）:{}", username);
            throw new UsernameNotFoundException("找不到该用户！");
        }
        //账号状态验证
        //帐号启用状态：0->Off 启用；1->On 停用
        if(userVO.getStatus()==1) {
            logger.debug("用户账号未启用，无法登陆 （手机号／邮箱）:{}", username);
            throw new UsernameNotFoundException("用户账号未启用！");
        }
        // security bcryptPasswordEncoder自定义密码验证
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bcryptPasswordEncoder.matches(password,userVO.getPassword())){
            throw new BadCredentialsException("密码错误，请重新输入");
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
        //存放用户信息-授权用户
        AuthUser authUser = ConvertUtil.convert(userVO, AuthUser.class);
        getHttpSession().setAttribute(BaseAuthVO.ADMIN_USER_LOGIN_SESSION_KEY, authUser);
        User user = new User(userVO.getUserName(), userVO.getPassword(),
                true, true, true, true,
                auths);
        return user;
    }
}
