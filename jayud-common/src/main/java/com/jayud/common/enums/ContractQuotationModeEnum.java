package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 订单标识
 */
@Getter
@AllArgsConstructor
public enum ContractQuotationModeEnum {

    MAIN("main", "主订单"),
    KY("ky", "空运"),
    BG("bg", "报关"),
    ZGYS("zgys", "中港运输"),
    HKPS("hkps", "香港配送"),
    NLYS("nlys", "内陆运输"),
    HY("hy", "海运"),
    NL("nl", "内陆运输"),
    TC("tc", "拖车"),
    CCI("cci", "仓储入库"),
    CCE("cce", "仓储出库"),
    CCF("ccf", "仓储快进快出");
    //    private Integer code;
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (ContractQuotationModeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ContractQuotationModeEnum getEnum(String code) {
        for (ContractQuotationModeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (ContractQuotationModeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }
}
