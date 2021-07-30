package com.jayud.scm.model.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CorrespondEnum {

    SKU_NO ("skuNo","sku_no","商品编号"),

    SKU_MODUL("skuModel","sku_model","商品型号"),

    SKU_NAME("skuName","sku_name","商品名称"),

    SKU_NAME_HS("skuNameHs","sku_name_hs","报关名称"),
    SKU_UNIT("skuUnit","sku_unit","单位"),
    SKU_BRAND("skuBrand","sku_brand","品牌"),
    SKU_ORIGIN("skuOrigin","sku_origin","产地"),
    SKU_NOTES("skuNotes","sku_notes","商品描述"),
    ACCESSORIES("accessories","accessories","配件"),
    HS_CODE_NO("hsCodeNo","hs_code_no","海关编码"),
    HK_CODE_NO("hkCodeNo","hk_code_no","香港海关报关编码"),
    HK_CODE_NAME("hkCodeName","hk_code_name","香港海关报关名称"),
    HK_CONTROL("hkControl","hk_control","香港管制"),
    UNIT_NW("unitNw","unit_nw","单位净重"),
    REFERENCE_PRICE("referencePrice","reference_price","参考价"),
    MAX_PRICE("maxPrice","max_price","最高价"),
    MIN_PRICE("minPrice","min_price","最低价"),
    AVG_PRICE("avgPrice","avg_price","平均价"),
    ITEM_NO("itemNo","item_no","料号"),
    TAX_CODE("taxCode","tax_code","税收分类编码"),
    TAX_CODE_NAME("taxCodeName","tax_code_name","税收分类名称"),
    SKU_ELEMENTS("skuElements","sku_elements","申报要素"),
    CASS_BY_NAME("cassByName","cass_by_name","归类人"),
    REMARK("remark","remark","备注"),
    CRT_BY_NAME("crtByName","crt_by_name","创建人名称"),
    MDY_BY_NAME("mdyByName","mdy_by_name","最后修改人名称"),
    VOIDED_BY_NAME("voidedByName","voided_by_name","删除人名称"),

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
    ELEMENTS("elements","elements","申报要素");




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
