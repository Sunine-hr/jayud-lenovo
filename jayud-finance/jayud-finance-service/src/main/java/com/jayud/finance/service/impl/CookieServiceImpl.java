package com.jayud.finance.service.impl;

import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.finance.kingdeesettings.K3CloudConfigBase;
import com.jayud.finance.service.CookieService;
import com.jayud.finance.service.KingdeeService;
import com.jayud.finance.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * kingdee 服务 登录cookie 服务接口实现
 * @author bocong.zheng
 */
@Slf4j
@Service
public class CookieServiceImpl implements CookieService {

    @Autowired
    private KingdeeService kingdeeService;

    @Override
    public String getCookie(K3CloudConfigBase config) {
        return loginCookie(config);
    }

    private String loginCookie(K3CloudConfigBase config) {
        String loginParam = BaseUtil.buildLogin(config,2052);

        CommonResult login = kingdeeService.login(config.getLogin(), loginParam);

        if (!login.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            Asserts.fail(ResultEnum.LOGIN_FAIL);
        }
        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();
        return cookie;
    }

    @Override
    public String refreshCookie(K3CloudConfigBase config) {
        return loginCookie(config);
    }
}
