package com.jayud.mall.security.service;

import com.jayud.mall.model.bo.CustomerLoginForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.security.entity.SecurityUser;
import com.jayud.mall.security.entity.UserDTO;
import com.jayud.mall.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义用户认证
 */
@Configuration
@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>注入了`PasswordEncoder`对象，该对象用于密码加密，注入前需要手动配置。我们在`BrowserSecurityConfig`中配置</p>
     * <p>`PasswordEncoder`是一个密码加密接口，而`BCryptPasswordEncoder`是Spring Security提供的一个实现方法</p>
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
//    /**
//     * <p>用户服务</p>
//     */
//    @Autowired
//    ISystemUserService userService;
//    /**
//     * <p>角色服务</p>
//     */
//    @Autowired
//    ISystemRoleService roleService;

    /**
     * <p>客户登录</p>
     */
    @Autowired
    ICustomerService customerService;

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
//        logger.debug("权限框架-加载用户");
//        HttpServletRequest request = ContextHolderUtils.getRequest();
//        String password = request.getParameter(passwordParameter);
//        logger.info("password = {}", password);
//
//        List<GrantedAuthority> auths = new ArrayList<>();
//
//        CustomerLoginForm loginForm = new CustomerLoginForm();
//        loginForm.setLoginname(username);
//        CustomerVO customerVO = customerService.customerLogin(loginForm);
//        //客户认证
//        if (customerVO == null) {
//            logger.debug("找不到该用户(手机号):{}", username);
//            throw new UsernameNotFoundException("找不到该用户！");
//        }
//        //启用状态，默认为1，1是0否
//        if(customerVO.getStatus()==0) {
//            logger.debug("用户账号未启用，无法登陆(手机号):{}", username);
//            throw new DisabledException("用户账号被禁用！");
//        }
//        // security bcryptPasswordEncoder自定义密码验证
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        if (!bcryptPasswordEncoder.matches(password,customerVO.getPasswd())){
//            throw new BadCredentialsException("密码错误，请重新输入");
//        }
//
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("注册客户");//角色写死
//        auths.add(authority);
//
//        //存放用户信息-授权用户
//        CustomerUser customerUser = ConvertUtil.convert(customerVO, CustomerUser.class);
//        getHttpSession().setAttribute(BaseAuthVO.WEB_CUSTOMER_USER_LOGIN_SESSION_KEY, customerUser);
//        User user = new User(customerUser.getUserName(), customerUser.getPasswd(),
//                true, true, true, true,
//                auths);
//
//        return user;


        //根据用户名查询数据
        CustomerLoginForm loginForm = new CustomerLoginForm();
        loginForm.setLoginname(username);
        CustomerVO customerVO = customerService.customerLogin(loginForm);
        //判断
        if(customerVO == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserDTO curUser = new UserDTO();
        curUser.setUsername(customerVO.getPhone());
        curUser.setPassword(customerVO.getPasswd());
//        BeanUtils.copyProperties(customerVO,curUser);

        //根据用户查询用户权限列表
//        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        List<String> permissionValueList = Arrays.asList("注册客户");
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;

    }
}
