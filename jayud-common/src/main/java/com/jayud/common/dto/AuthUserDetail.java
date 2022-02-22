package com.jayud.common.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author ciro
 * @date 2022/2/15 16:29
 * @description:
 */
@Data
public class AuthUserDetail implements UserDetails {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 登录名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 是否部门负责人
     */
    private Boolean isDepartCharge;
    /**
     *所属部门id
     */
    private Long departId;
    /**
     * 负责部门ids
     */
    private String departIds;
    /**
     * 岗位id
     */
    private Long postId;
    /**
     * 员工状态：0-离职，1-在职
     */
    private Integer jobStatus;
    /**
     * 冻结状态：0-冻结，1-正常
     */
    private Integer status;
    /**
     * 上司id
     */
    private Long supervisorId;
    /**
     * 兼职部门ids
     */
    private String partTimeDepId;

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
