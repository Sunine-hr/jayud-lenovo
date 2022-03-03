package com.jayud.crm.model.enums;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/3/3 16:21
 * @description: 客户风险类型枚举
 */
@Getter
@AllArgsConstructor
public enum CustRiskTypeEnum {
    RISK_CUST("1","风险客户"),
    BLACKLIST_CUST("2","黑名单客户");

    /**
     * 类型
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

}
