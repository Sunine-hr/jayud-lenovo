//package com.jayud.oms.security.util;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * @author jiangziyuan
// * @date 2019/7/13 / 15:37
// */
//@Component
//public class SpringContextUtil implements ApplicationContextAware {
//    //spring上下文
//    private static ApplicationContext applicationContext;
//
//    /**
//     *      * 实现ApplicationContextAware接口的回调方法，设置上下文环境
//     *      * @param applicationContext
//     *     
//     */
////    public static void setApplicationContext(ApplicationContext applicationContext) {
////        if (null == SpringContextUtil.applicationContext) {
////            SpringContextUtil.applicationContext = applicationContext;
////        }
////    }
////    @Override
////    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
////        this.applicationContext=applicationContext;
////    }
//
//    public static ApplicationContext getApplicationContext() {
//        return applicationContext;
//    }
//
//    /**
//     *      * 通过name获取 Bean.
//     *      *
//     *      * @param name
//     *      * @return
//     *      
//     */
//    public static Object getBean(String name) {
//        return getApplicationContext().getBean(name);
//    }
//
//    /**
//     *      * 通过name获取 Bean.
//     *      *
//     *      * @param clazz
//     *      * @return
//     *      
//     */
//    public static <T> T getBean(Class<T> clazz) {
//        return getApplicationContext().getBean(clazz);
//    }
//
//    /**
//     *      * 通过name,以及Clazz返回指定的Bean
//     *      *
//     *      * @param name
//     *      * @param clazz
//     *      * @return
//     *      
//     */
//    public static <T> T getBean(String name, Class<T> clazz) {
//        return getApplicationContext().getBean(name, clazz);
//    }
//
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        SpringContextUtil.applicationContext = applicationContext;
//    }
//}