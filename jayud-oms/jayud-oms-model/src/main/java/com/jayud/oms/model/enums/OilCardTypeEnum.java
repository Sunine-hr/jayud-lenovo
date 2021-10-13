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
public enum OilCardTypeEnum {

    ONE(1, "充值卡"),
    TWO(2, "共享卡"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (OilCardTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static OilCardTypeEnum getEnum(Integer code) {
        for (OilCardTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(Integer desc) {
        for (OilCardTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    public static List<InitComboxStrVO> initType() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (OilCardTypeEnum value : values()) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setId(value.getCode().longValue());
            initComboxStrVO.setName(value.getDesc());
            list.add(initComboxStrVO);
        }
        return list;
    }
}
