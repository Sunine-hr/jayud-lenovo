package com.jayud.oms.model.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 合同报价标记
 *
 */
@Getter
@AllArgsConstructor
public enum ContractQuotationSignEnum {

    ONE(1, "已到期"),
    TWO(2, "即将到期"),
    THREE(3, "无标记"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ContractQuotationSignEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (ContractQuotationSignEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

    public static List<InitComboxStrVO> initCombox() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (ContractQuotationSignEnum value : ContractQuotationSignEnum.values()) {
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
