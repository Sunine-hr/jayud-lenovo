package com.jayud.oms.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = JayudSecurityProperties.JAYUD_SECURITY_PREFIX)
@Component
public class JayudSecurityProperties {

    public static final String JAYUD_SECURITY_PREFIX = "jayud.security";
    //允许访问的接口
    private String[] permitAll = new String[]{};
    //token过期时间(秒)，默认一天
    private int expired = 60 * 60 * 24;
    //token的key
    private String header = "token";
    //token值前缀
    private String headerPrefix = "Jayud";
    //设置默认用户信息
    private String defaultUserId = "";
    //设置开关
    private boolean authSwitch;


    public String getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public String[] getPermitAll() {
        return permitAll;
    }

    public void setPermitAll(String[] permitAll) {
//        for (int i = 0; i < permitAll.length; i++) {
//            permitAll[i]=permitAll[i].replace("*", "\\*");
//        }
        this.permitAll = permitAll;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeaderPrefix() {
        return headerPrefix;
    }

    public void setHeaderPrefix(String headerPrefix) {
        this.headerPrefix = headerPrefix;
    }
}
