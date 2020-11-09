package com.jayud.oms.security.factory;

import com.jayud.oms.security.enums.LoginTypeEnum;
import com.jayud.oms.security.strategy.LoginStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;

/**
 * 登录工厂
 */
@Slf4j
public class LoginFactory {

    private LoginFactory() {
    }

    //3、 同样提供静态方法获取实例
    //这里加final是为了防止子类重写父类
    public static final LoginFactory getInstance() {
        return LazyHolder.INSTANCE;
    }

    public LoginStrategy create(Integer type) {
        Class clazz = LoginTypeEnum.getClassByCode(type);

        if (clazz == null) {
            log.error("你选择的登录方式不存在，请选择其他登录方式");
            throw new ShiroException("你选择的登录方式不存在，请选择其他登录方式");
        }
        try {
            return (LoginStrategy) clazz.newInstance();
        } catch (Exception e) {
            log.error("创建工厂失败 message={}", e.getMessage());
            throw new ShiroException("创建工厂失败");
        }
    }


    //2、先声明一个静态内部类
    //内部类的初始化需要依赖主类，需要先等主类实例化之后，内部类才能开始实例化
    private static class LazyHolder {
        //这里加final是为了防止内部将这个属性覆盖掉
        private static final LoginFactory INSTANCE = new LoginFactory();
    }


}


