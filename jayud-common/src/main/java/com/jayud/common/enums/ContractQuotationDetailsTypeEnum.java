package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 合同报价详情类型
 */
@Getter
@AllArgsConstructor
public enum ContractQuotationDetailsTypeEnum {

    ONE(1, "整车", SubOrderSignEnum.ZGYS.getSignOne()),
    TWO(2, "其他", SubOrderSignEnum.ZGYS.getSignOne()),
    ;
    private Integer code;
    private String desc;
    private String subType;

    public static String getDesc(String code) {
        for (ContractQuotationDetailsTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ContractQuotationDetailsTypeEnum getEnum(Integer code) {
        for (ContractQuotationDetailsTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ContractQuotationDetailsTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    /**
     * main|zgys|bg|ky
     * @param cmd
     * @return
     */
//    public static Integer getCode(String cmd) {
//        for (BusinessTypeEnum value : values()) {
//            if (Objects.equals(cmd, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
