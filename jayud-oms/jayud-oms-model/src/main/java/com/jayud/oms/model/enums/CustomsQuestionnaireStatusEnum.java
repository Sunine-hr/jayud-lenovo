package com.jayud.oms.model.enums;

import com.jayud.common.entity.InitComboxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomsQuestionnaireStatusEnum {
    ONE(1, "正常"),
    TWO(2, "提醒"),
    THREE(3, "失效");

    private Integer code;
    private String desc;

    public static List<InitComboxVO> getDropDownList() {
        List<InitComboxVO> comboxStrVOS = new ArrayList<>();
        for (CustomsQuestionnaireStatusEnum value : values()) {
            InitComboxVO comboxStrVO = new InitComboxVO();
            comboxStrVO.setId(Long.valueOf(value.getCode()));
            comboxStrVO.setName(value.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }


    public static String getDesc(Integer code) {
        for (CustomsQuestionnaireStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CustomsQuestionnaireStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
