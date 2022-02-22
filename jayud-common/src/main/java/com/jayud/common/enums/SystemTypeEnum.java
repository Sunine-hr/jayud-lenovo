package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/2/22 13:46
 * @description: 系统类型枚举
 */
@Getter
@AllArgsConstructor
public enum SystemTypeEnum {

    AUTH(1,"权限系统","AUTH(权限系统)"),
    OMS(2,"OMS系统","OMS(订单管理系统)"),
    CRM(3,"CRM系统","CRM(客户管理系统)"),
    SCV(4,"SCV系统","SCV(物流塔)"),
    WMS(5,"WMS系统","WMS(仓库系统)"),
    TMS(6,"TMS系统","TMS(运输系统)"),
    LMS(7,"LMS系统","LMS(货代系统)"),
    SCM(8,"SCM系统","SCM(供应链)"),
    BMS(9,"BMS系统","BMS(财务结算系统)"),
    BI(10,"BI系统","BI(数据分析系统)");


    /**
     * 类型
     */
    private Integer type;

    /**
     *  系统名称
     */
    private String systemName;

    /**
     * 描述
     */
    private String desc;
}
