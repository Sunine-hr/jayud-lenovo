package com.jayud.common;

import com.jayud.common.utils.HttpRequester;
import com.jayud.common.utils.SpringContextUtil;
import com.jayud.common.utils.StringUtils;

public class UserOperator {

    private static ThreadLocal<String> user = new ThreadLocal<String>();

    public static ThreadLocal<String> getUser() {
        return user;
    }

    public static void setUser(ThreadLocal<String> user) {
        UserOperator.user = user;
    }

    public static String getToken() {
//        String name = user.get();
//        if (StringUtils.isEmpty(name)) {
            String token = HttpRequester.getHead("token");
            if (StringUtils.isEmpty(token)) {
                return null;
            }
            RedisUtils redisUtils = SpringContextUtil.getBean(RedisUtils.class);
            return redisUtils.get(token);
//        }
//        return name;
    }

    public static void setToken(String token) {
        user.set(token);
    }
}
