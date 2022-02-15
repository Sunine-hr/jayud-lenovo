package com.jayud.auth.model.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author ciro
 * @date 2022/2/15 16:29
 * @description:
 */
@Data
public class AuthUserDetail implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String phone;

    private List<GrantedAuthority> authorities;



    @Override
    public boolean equals(Object obj) {
        //会话并发生效，使用username判断是否是同一个用户

        if (obj instanceof User){
            //字符串的equals方法是已经重写过的
            return ((User) obj).getUsername().equals(this.username);
        }else {
            return false;
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
