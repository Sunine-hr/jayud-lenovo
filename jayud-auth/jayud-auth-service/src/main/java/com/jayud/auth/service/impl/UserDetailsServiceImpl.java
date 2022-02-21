package com.jayud.auth.service.impl;

import com.jayud.auth.model.dto.AuthUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ciro
 * @date 2022/2/21 14:29
 * @description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthUserDetail authUserDetail = new AuthUserDetail();
        authUserDetail.setId(1L);
        authUserDetail.setUsername("admin");
        authUserDetail.setPassword("$2a$10$0Gh0i/roNUhOHnEXTpn3WexRsc8RfhxAaumD.KdzUtmxesVwHH8q6");
        return authUserDetail;
    }
}
