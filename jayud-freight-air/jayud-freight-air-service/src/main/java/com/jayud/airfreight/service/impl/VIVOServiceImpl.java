package com.jayud.airfreight.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.service.VIVOService;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.RsaEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * vivo数据接口服务
 *
 * @author william
 * @description
 * @Date: 2020-09-14 13:48
 */
@Service
@Slf4j
public class VIVOServiceImpl implements VIVOService {
    @Value("${vivo.default.username}")
    String defaultUsername;
    @Value("${vivo.default.password}")
    String defaultPassword;
    @Value("${vivo.default.scope}")
    String defaultScope;
    @Value("${vivo.urls.token}")
    String tokenUrl;


    private String getToken(String userName, String password, String scope) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(scope)) {
            //只要有一个参数为空，即调用默认的登录设置
            userName = defaultUsername;
            password = defaultPassword;
            scope = defaultScope;
            log.info("尝试使用配置文件中的默认配置进行vivo接口授权...");

            String encryptedPassword = null;
            try {
                encryptedPassword = RsaEncryptUtil.getEncryptedPassword(userName, password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (StringUtils.isBlank(encryptedPassword)) {
                Asserts.fail(ResultEnum.UNAUTHORIZED, "VIVO RSA加密失败");
            }

            String feedback = HttpRequest.post(tokenUrl)
                    .header(Header.CONTENT_TYPE.name(), "application/x-www-form-urlencoded")
                    .body(String.format("grant_type=%s&username=%s&password=%s&scope=%s",
                            "password",
                            userName,
                            encryptedPassword,
                            scope)
                    ).execute().body();
            Map resultMap = JSONUtil.toBean(feedback, Map.class);
            String access_token = MapUtil.getStr(resultMap, "access_token");
            if (!StringUtils.isEmpty(access_token)) {
                return access_token;
            }
            Asserts.fail(ResultEnum.UNAUTHORIZED, "vivo 授权失败");
        }


        return null;
    }


}
