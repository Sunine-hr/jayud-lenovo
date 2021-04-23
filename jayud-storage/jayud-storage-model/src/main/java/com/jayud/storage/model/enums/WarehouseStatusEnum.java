package com.jayud.storage.model.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 仓库状态枚举
 */
@Getter
@AllArgsConstructor
public enum WarehouseStatusEnum {

    DISABLE(0, "无效"),
    ENABLE(1,"有效");

    @ApiModelProperty(value = "代码")
    private Integer code;

    @ApiModelProperty(value = "描述")
    private String desc;


    public static String getDesc(String code) {
        for (WarehouseStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (WarehouseStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
