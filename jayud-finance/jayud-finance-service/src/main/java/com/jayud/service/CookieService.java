package com.jayud.service;

import com.jayud.kingdeesettings.K3CloudConfigBase;
import com.jayud.constant.Constant;
import org.springframework.cache.annotation.Cacheable;

/**
 * kingdee 服务 登录cookie 服务接口
 * @author bocong.zheng
 */
public interface CookieService {


    /**
     * 获取登录cookie
     * @param config
     * @return
     */
    @Cacheable(cacheNames = Constant.KINGDEE_COOKIE_REDIS_KEY,key = "#config.dbId")
    public String getCookie(K3CloudConfigBase config);


    /**
     * 更新登录cookie
     * @param config
     * @return
     */
    @Cacheable(cacheNames = Constant.KINGDEE_COOKIE_REDIS_KEY,key = "#config.dbId")
    public String refreshCookie(K3CloudConfigBase config);

}
