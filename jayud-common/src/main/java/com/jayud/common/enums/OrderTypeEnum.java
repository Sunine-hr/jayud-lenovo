package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    //订单类型
    JYD("JYD", "主单号"),
    TI("TI", "运输进口"),
    TE("TE", "运输出口"),
    TL("TL", "内陆运输"),
    TTI("TTI", "拖车进口"),
    TTE("TTE", "拖车出口"),
    AI("AI", "空运进口"),
    AE("AE", "空运出口"),
    SI("SI", "海运进口"),
    SE("SE", "海运出口"),
    WH("WH", "仓储订单"),
    EC("EC", "电商订单"),
    BI("BI", "报关进口"),
    BE("BE", "报关出口"),
    P("P", "派车单号"),
    ZD("ZD", "对账单号"),
    FW("FW", "服务单号"),
    RK("RK", "入库订单号"),
    CK("CK", "出库订单号"),

    //账单编号
    AR("AR", "应收账单"),
    AP("AP", "应付账单"),
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (OrderTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static String getCode(String desc) {
        for (OrderTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return "";
    }
}
