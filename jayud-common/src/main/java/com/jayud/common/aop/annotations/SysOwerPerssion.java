package com.jayud.common.aop.annotations;



import com.jayud.common.entity.SysDataPermissionEntity;

import java.lang.annotation.*;

/**
 * @author ciro
 * @date 2021/12/22 13:47
 * @description:    货主权限
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysOwerPerssion {

    /**
     * 注解货主集合字段
     */
    String owerIdListField() default "owerIdList";


    /**
     * 对象类型
     *
     * @return
     */
    Class<?> clazz() default SysDataPermissionEntity.class;

}
