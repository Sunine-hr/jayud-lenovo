package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 征信类型
 */
@Getter
@AllArgsConstructor
public enum CreditStatusEnum {

    ABNORMAL(0, "异常"),
    NON_ANOMALY(1,"非异常");
    private Integer code;
    private String desc;

    public static List<InitComboxVO> getDropDownList() {
        List<InitComboxVO> comboxStrVOS = new ArrayList<>();
        for (CreditStatusEnum value : values()) {
            InitComboxVO comboxStrVO = new InitComboxVO();
            comboxStrVO.setId(Long.valueOf(value.getCode()));
            comboxStrVO.setName(value.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }

    public static String getDesc(String code) {
        for (CreditStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CreditStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    /**
     * main|zgys|bg|ky
     * @param cmd
     * @return
     */
//    public static Integer getCode(String cmd) {
//        for (BusinessTypeEnum value : values()) {
//            if (Objects.equals(cmd, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
