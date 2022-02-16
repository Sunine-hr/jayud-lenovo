package com.jayud.auth.listener;

import cn.hutool.http.useragent.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @author ciro
 * @date 2022/2/16 14:32
 * @description:    登录成功日志
 */
@Slf4j
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent>{

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        if (event.getAuthentication().getDetails().toString().contains("username")) {
            log.error("login inside username");
        }
    }
}

