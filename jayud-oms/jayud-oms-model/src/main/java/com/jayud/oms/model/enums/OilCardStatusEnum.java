package com.jayud.oms.model.enums;

import com.jayud.oms.model.vo.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum OilCardStatusEnum {

    ONE(1, "使用中"),
    TWO(2, "闲置中"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (OilCardStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static OilCardStatusEnum getEnum(Integer code) {
        for (OilCardStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (OilCardStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    public static List<InitComboxStrVO> initStatus() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (OilCardStatusEnum value : values()) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setId(value.getCode().longValue());
            initComboxStrVO.setName(value.getDesc());
            list.add(initComboxStrVO);
        }
        return list;
    }
}
