package com.jayud.oms.model.enums;

import com.jayud.common.entity.InitComboxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomerRiskLevelEnum {
    ONE(1, "不可接受之信用风险"),
    TWO(2, "高度信用风险"),
    THREE(3, "中度信用风险"),
    FOUR(4, "低信用风险");

    private Integer code;
    private String desc;

    public static List<InitComboxVO> getDropDownList() {
        List<InitComboxVO> comboxStrVOS = new ArrayList<>();
        for (CustomerRiskLevelEnum value : values()) {
            InitComboxVO comboxStrVO = new InitComboxVO();
            comboxStrVO.setId(Long.valueOf(value.getCode()));
            comboxStrVO.setName(value.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }


    public static String getDesc(Integer code) {
        for (CustomerRiskLevelEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CustomerRiskLevelEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
