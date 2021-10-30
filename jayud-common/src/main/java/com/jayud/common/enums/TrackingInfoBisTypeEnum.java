package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 跟踪业务类型
 */
@Getter
@AllArgsConstructor
public enum TrackingInfoBisTypeEnum {

    ONE(1, "合同报价");
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (TrackingInfoBisTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (TrackingInfoBisTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

    public static List<InitComboxStrVO> initCombox() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (TrackingInfoBisTypeEnum value : TrackingInfoBisTypeEnum.values()) {
            InitComboxStrVO tmp = new InitComboxStrVO();
            tmp.setName(value.getDesc());
            tmp.setId(value.getCode().longValue());
            list.add(tmp);
        }
        return list;
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
