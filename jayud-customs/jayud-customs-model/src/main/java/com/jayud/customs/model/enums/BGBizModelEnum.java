package com.jayud.customs.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 报关业务模式
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum BGBizModelEnum {
    LAND_TRANSPORT("1", "LAND_TRANSPORT", "陆路运输"),
    AIR_TRANSPORT("2", "AIR_TRANSPORT", "空运"),
    SEA_TRANSPORT("3", "SEA_TRANSPORT", "海运"),
    EXPRESS("4", "EXPRESS", "快递"),
    INLAND("5", "INLAND", "内陆");

    private String code;
    private String dictCode;
    private String desc;

    public static String getDesc(String code) {
        for (BGBizModelEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (BGBizModelEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }

    public static BGBizModelEnum getEnum(String code) {
        for (BGBizModelEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
