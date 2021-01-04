package com.jayud.airfreight.model.enums;

import com.jayud.common.enums.CreateUserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 空运异常原因
 */
@Getter
@AllArgsConstructor
public enum ExceptionCausesEnum {
    ONE(1, "货物破损"),
    TWO(2, "潮湿"),
    THREE(3, "丢失"),
    FOUR(4, "其他"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ExceptionCausesEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static List<ExceptionCausesEnum> getExceptionCauses(Integer createUserType) {
        createUserType = createUserType == null ? CreateUserTypeEnum.LOCAL.getCode() : CreateUserTypeEnum.VIVO.getCode();
        List<ExceptionCausesEnum> list = new ArrayList<>();
        switch (createUserType) {
            case 1:
                list.add(values()[0]);
                list.add(values()[1]);
                list.add(values()[2]);
                list.add(values()[3]);
                return list;
            default:
                return Arrays.asList(values());
        }
    }

    public static void main(String[] args) {
        int[] indexs = new int[]{1, 2, 3};
//        for (int i = 0; i < values().length; i++) {
//            indexs[i] = i;
//        }
        for (int index : indexs) {
            System.out.println(index);
        }

    }

//    public static Integer getCode(String desc) {
//        for (AirAbnormalCausesEnum value : values()) {
//            if (Objects.equals(desc, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return CIF.getCode();
//    }
}
