package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/3/4 9:13
 * @description: 角色编码枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    TENANT_ROLE_ADMIN("tenant_role_admin","租户管理员"),
    SUPER_ADMIN("super_admin","超级管理员"),
    SALE_ROLE("sale_role","销售员");

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;


}
