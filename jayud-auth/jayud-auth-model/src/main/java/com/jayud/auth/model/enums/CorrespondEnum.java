package com.jayud.auth.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CorrespondEnum {

//hs_code
    CODE_NO("codeNo","code_no","海关税号"),
    CODE_NAME("codeName","code_name","税号名称"),
    CODE_HK_NO("codeHkNo","code_hk_no","香港海关税号"),
    LOW_RATE("lowRate","low_rate","最惠国税率"),
    HIGHT_rate("hightRate","hight_rate","普通国税率"),
    OUT_RATE("outRate","out_rate","出口税率"),
    TAX_RATE("taxRate","tax_rate","增值税率"),
    TSL_RATE("tslRate","tsl_rate","退税率"),
    CONSUMPTION_RATE("consumptionRate","consumption_rate","消费税率"),
    UNIT_1("unit1","unit_1","单位1"),
    UNIT_2("unit2","unit_2","单位2"),
    CONTROL_MA("controlMa","control_ma","监管条件"),
    CONTROL_CIQ("controlCiq","control_ciq","商检条件"),
    CONTROL_CHECK("controlCheck","control_check","商检监定"),
    NOTE_S("noteS","note_s","描述"),
    TEMP_IN_RATE("tempInRate","temp_in_rate","暂定进口税率"),
    TEMP_OUT_RATE("tempOutRate","temp_out_rate","暂定出口税率"),
    ELEMENTS("elements","elements","申报要素"),

    ;

    private String code;
    private String name;
    private String desc;

    public static String getDesc(String code) {
        for (CorrespondEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (CorrespondEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getName();
            }
        }
        return "";
    }
}
