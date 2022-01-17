package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OperationEnum {

    INSERT("insert","添加数据id="),
    UPDATE("update","修改数据id="),
    DELETE("delete","删除数据id=")
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (OperationEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
