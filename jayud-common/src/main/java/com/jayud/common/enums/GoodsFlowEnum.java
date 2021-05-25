package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxVO;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 货物流向(1进口 2出口)
 */
@Getter
@AllArgsConstructor
public enum GoodsFlowEnum {

    IMPORT(1, "进口"),
    EXPORT(2, "出口"),
    ;
    private Integer code;
    private String desc;


    public static List<InitComboxVO> getDropDownList() {
        List<InitComboxVO> comboxStrVOS = new ArrayList<>();
        for (GoodsFlowEnum value : values()) {
            InitComboxVO comboxStrVO = new InitComboxVO();
            comboxStrVO.setId(Long.valueOf(value.getCode()));
            comboxStrVO.setName(value.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }

    public static String getDesc(Integer code) {
        for (GoodsFlowEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (GoodsFlowEnum value : values()) {
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
