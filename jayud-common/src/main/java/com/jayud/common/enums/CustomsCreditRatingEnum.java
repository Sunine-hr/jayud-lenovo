package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomsCreditRatingEnum {
    ZERO(0, "一般认证企业"),
    ONE(1, "一般信用企业"),
    TWO(2, "高级信用企业"),
    THREE(3, "失信企业");

    private Integer code;
    private String desc;

    public static List<InitComboxVO> getDropDownList() {
        List<InitComboxVO> comboxStrVOS = new ArrayList<>();
        for (CustomsCreditRatingEnum value : values()) {
            InitComboxVO comboxStrVO = new InitComboxVO();
            comboxStrVO.setId(Long.valueOf(value.getCode()));
            comboxStrVO.setName(value.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }


    public static String getDesc(Integer code) {
        for (CustomsCreditRatingEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CustomsCreditRatingEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
