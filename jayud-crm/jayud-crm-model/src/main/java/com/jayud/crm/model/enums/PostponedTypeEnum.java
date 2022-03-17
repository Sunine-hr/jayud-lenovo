package com.jayud.crm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum PostponedTypeEnum {

    ONE(1, "顺延6个月"),
    TWO(2, "顺延12个月"),
    THREE(3, "顺延24个月"),
    ;


    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (PostponedTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (PostponedTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }

//    public static List<InitComboxStrVO> initCombox() {
//        List<InitComboxStrVO> list = new ArrayList<>();
//        for (FileModuleEnum value : FileModuleEnum.values()) {
//            InitComboxStrVO tmp = new InitComboxStrVO();
//            tmp.setName(value.getDesc());
//            tmp.setId(value.getCode().longValue());
//            list.add(tmp);
//        }
//        return list;
//    }
}
