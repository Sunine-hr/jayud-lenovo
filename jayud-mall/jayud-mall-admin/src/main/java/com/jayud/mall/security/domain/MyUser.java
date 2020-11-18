package com.jayud.mall.security.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>创建一个`MyUser`对象，用于存放模拟的用户数据</p>
 * <p>（实际中一般从数据库获取，这里为了方便直接模拟）</p>
 */
public class MyUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "断账户是否未过期")
    private boolean accountNonExpired = true;

    @ApiModelProperty(value = "账户是否未锁定")
    private boolean accountNonLocked= true;

    @ApiModelProperty(value = "用户凭证是否没过期")
    private boolean credentialsNonExpired= true;

    @ApiModelProperty(value = "用户是否可用")
    private boolean enabled= true;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
