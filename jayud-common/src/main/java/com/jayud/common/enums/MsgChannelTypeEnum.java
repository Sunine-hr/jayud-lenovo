package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 消息推送渠道
 */
@Getter
@AllArgsConstructor
public enum MsgChannelTypeEnum {

    MAIL(1, "邮件"),
//    PAYMENT(1, "应付"),
    ;
    private Integer code;
    private String desc;

    public static List<InitComboxStrVO> initComboxStrVO() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (MsgChannelTypeEnum value : values()) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(value.getDesc());
            initComboxStrVO.setId(value.getCode().longValue());
            list.add(initComboxStrVO);
        }
        return list;
    }

    public static String getDesc(String code) {
        for (MsgChannelTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static MsgChannelTypeEnum getEnum(Integer code) {
        for (MsgChannelTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (MsgChannelTypeEnum value : values()) {
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
