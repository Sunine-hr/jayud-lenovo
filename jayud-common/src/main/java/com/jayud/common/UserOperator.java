package com.jayud.common;

public class UserOperator {

    private static ThreadLocal<String> user = new ThreadLocal<String>();

    public static ThreadLocal<String> getUser() {
        return user;
    }

    public static void setUser(ThreadLocal<String> user) {
        UserOperator.user = user;
    }

    public static String getToken() {
        return user.get();
    }

    public static void setToken(String token) {
        user.set(token);
    }
}
