package com.jayud.wms.model.enums;

import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/17 15:26
 * @description: 批属性字段值枚举
 */
@Getter
public enum BatchAttributeFiledEnum {

    BATCH_CODE(1,"id.batch_code"),
    PRODUCTION_DATE(2,"id.material_production_date"),
    CUSTOMER_FILED1(3,"id.custom_field1"),
    CUSTOMER_FILED2(4,"id.custom_field2"),
    CUSTOMER_FILED3(5,"id.custom_field3");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 字段值
     */
    private String filedValue;

    private BatchAttributeFiledEnum(final Integer type,final String filedValue){
        this.type = type;
        this.filedValue = filedValue;
    }

    public static String getFiledValue(Integer type) {
        BatchAttributeFiledEnum[] filedEnums = values();
        for (BatchAttributeFiledEnum filedEnum : filedEnums) {
            if (filedEnum.getType().equals(type)){
                return filedEnum.getFiledValue();
            }
        }
        return null;
    }
}
