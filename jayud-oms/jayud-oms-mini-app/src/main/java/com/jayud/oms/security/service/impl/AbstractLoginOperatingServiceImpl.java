package com.jayud.oms.security.service.impl;


import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.common.RedisUtils;
import com.jayud.common.exception.JayudBizException;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.security.entity.Certificate;
import com.jayud.oms.security.entity.LoginUser;
import com.jayud.oms.security.factory.LoginFactory;
import com.jayud.oms.security.properties.JayudSecurityProperties;
import com.jayud.oms.security.service.LoginOperatingService;
import com.jayud.oms.security.strategy.LoginStrategy;
import com.jayud.oms.security.util.HttpUtil;
import com.jayud.oms.security.util.SecurityUtil;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.WechatAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * 登录操作抽象类
 */
@Slf4j
public abstract class AbstractLoginOperatingServiceImpl implements LoginOperatingService {

    /**
     * 权限管理器
     */
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtils redisUtils;
    //权限配置
    @Autowired
    private JayudSecurityProperties securityProperties;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Autowired
    private WechatAppService wechatAppService;
    @Value("${wechatApplet.appid}")
    private String wechatAppletAppId;
    @Value("${wechatApplet.secret}")
    private String wechatAppletSecret;

    @Override
    public void loadAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    /**
     * 登录操作
     */
    @Override
    public Authentication login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //接受等用户登录输入参数
        LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
        //校验用户输入参数
        loginUser.check();
        //自定义登录验证
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getAccount(), loginUser, new ArrayList<>()));
    }

    /**
     * 登录校验
     *
     * @return
     */
    @Override
    public Authentication checkLogin(Authentication authentication) {
        // 用户名
        String account = authentication.getName();
        // 用户对象
        LoginUser loginUser = (LoginUser) authentication.getCredentials();
        //自定义校验
        this.customCheck(account);
        //获取凭证
        Certificate certificate = this.getCertificate(account);

        LoginStrategy loginStrategy = LoginFactory.getInstance().create(loginUser.selectLoginType());
        //登录操作
        boolean past = loginStrategy.doLogin(loginUser, certificate);

        //登录校验是否成功
        if (past) {
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            //绑定小程序oppId
            DriverInfo driverInfo = this.driverInfoService.getById(certificate.getId());
            if (driverInfo.getAppletId() == null) {
                JSONObject jsonObject = this.wechatAppService.getOpenId(wechatAppletAppId, wechatAppletSecret, loginUser.getCode());
                if (jsonObject.getStr("errmsg") == null) {
//                    throw new JayudBizException("登录失败 错误提示:" + jsonObject.getStr("errmsg"));
                    this.driverInfoService.updateById(new DriverInfo().setId(driverInfo.getId()).setAppletId(jsonObject.getStr("openid")));
                }

            }
            // 生成令牌
            return new UsernamePasswordAuthenticationToken(certificate.getId(), null,
                    authorities);
        } else {
            throw new BadCredentialsException("账号或密码错误");
        }
    }

    /**
     * 成功登录
     *
     * @param req
     * @param res
     * @param chain
     * @param auth
     */
    @Override
    public void successLogin(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        String id = auth.getPrincipal().toString();

        String token = SecurityUtil.generateToken(id);

        res.addHeader(securityProperties.getHeader(), securityProperties.getHeaderPrefix() + " " + token);
        // 登录成功，存储token
        redisUtils.set(securityProperties.getHeaderPrefix().trim() + "_MINI_TOKEN_" + id, token, (securityProperties.getExpired()));
        //自定义回调参数
        Map<String, Object> response = callback(id);
        //存储token
        response.put("token", token);
        //自定操作
        customOperation(id);
        //成功返回
        HttpUtil.writeData(req, res, response);
    }

    /**
     * 回调参数
     *
     * @param id
     * @return
     */
    public abstract Map<String, Object> callback(String id);

    /**
     * 自定操作
     *
     * @param id
     */
    public abstract void customOperation(String id);

    /**
     * 自定校验
     */
    public abstract void customCheck(String account) throws SecurityException;

    /**
     * 获取用户凭证
     */
    public abstract Certificate getCertificate(String account);


}
