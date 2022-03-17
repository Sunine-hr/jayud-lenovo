package com.jayud.common.aop.annotations;

import com.jayud.common.entity.SysDataPermissionEntity;

import java.lang.annotation.*;

/**
 * @author ciro
 * @date 2021/12/6 16:22
 * @description: 数据权限
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysDataPermission {

    /**
     * 注解机构id集合字段
     */
    String orgIdsField() default "orgIds";

    /**
     * 注解是否负责人字段
     */
    String isChargeField() default "isCharge";

    /**
     * 注解货主集合字段
     */
    String owerIdListField() default "owerIdList";

    /**
     * 注解仓库集合字段
     */
    String warehouseIdListField() default "warehouseIdList";


    /**
     * 对象类型
     *
     * @return
     */
    Class<?> clazz() default SysDataPermissionEntity.class;

}
