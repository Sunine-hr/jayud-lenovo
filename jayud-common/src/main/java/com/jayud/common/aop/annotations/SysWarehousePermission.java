package com.jayud.common.aop.annotations;

import com.jayud.common.entity.SysDataPermissionEntity;


import java.lang.annotation.*;

/**
 * @author ciro
 * @date 2021/12/22 13:41
 * @description:    仓库权限
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysWarehousePermission {

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
