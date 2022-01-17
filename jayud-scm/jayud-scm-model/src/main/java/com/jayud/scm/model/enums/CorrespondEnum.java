package com.jayud.scm.model.enums;

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
    ELEMENTS("elements","elements","申报要素"),

  //b_tax_class_code
    TAX_CODE1("taxCode","tax_code","分类代码"),
    TAX_ITEM_NAME("taxItemName","tax_item_name","分类名称"),
    TAX_CLASS_NAME("taxClassName","tax_class_name","类型名称"),

    //system_role_action
    TITLE("title","title","父级菜单"),
    ACTION_NAME("actionName","action_name","权限名称"),
    ACTION_CODE("actionCode","action_code","权限code"),
    NAME("name","name","角色"),

    //customer
    CUSTOMERNO("customerNo","customer_no","编号"),
    CUSTOMERNAME("customerName","customer_name","名称"),
    CUSTOMERTYPE("customerType","customer_type","客户等级"),
    CUSTOMERABBR("customerAbbr","customer_abbr","简称"),
    FOLLOWERNAME("followerName","follower_name","商务"),
    BUSINESSTYPE("businessType","business_type","业务类型"),
    LEGALDEPUTY("legalDeputy","legal_deputy","法人"),
    REGADDRESS("regAddress","reg_address","注册地址"),
    REGTEL("regTel","reg_tel","注册电话"),
    TAXNO("taxNo","tax_no","纳税识别号"),
    COMPANYNET("companyNet","company_net","网址"),
    PROVINCENAME("provinceName","province_name","省"),

    CITYNAME("cityName","city_name","市"),
    COUNTYNAME("countyName","county_name","县、区"),
    CRTBYNAME("crtByName","crt_by_name","创建人"),
    MDYBYNAME("mdyByName","mdy_by_name","修改人"),
    FSALESMAN("fsalesMan","fsales_man","业务员姓名"),

    CUSTOMERAGNO("customerAgNo","customer_ag_no","客户协议编号"),
    ENCUSTOMERNAME("enCustomerName","en_customer_name","英文名称"),
    ENCUSTOMERADDRESS("enCustomerAddress","en_customer_address","英文地址"),
    CUSTOMERSTYLE("customerStyle","customer_style","客户类型"),
    CUSTOMERSTATE("customerState","customer_state","客户跟进状态"),
    AREA("area","area","区域")
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
