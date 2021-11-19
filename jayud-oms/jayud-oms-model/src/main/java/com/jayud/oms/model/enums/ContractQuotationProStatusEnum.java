package com.jayud.oms.model.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 合同报价操作流程
 * 1:未提交,2:待部门经理审核,3:待公司法务审核,4:待总审核,5:未通过,6:待完善,7:已完成
 */
@Getter
@AllArgsConstructor
public enum ContractQuotationProStatusEnum {

    ONE(1, "未提交", 2),
    TWO(2, "待公司法务审核", 3),
    THREE(3, "待部门经理审核", 4),
    FOUR(4, "待总经理审核", 6),
    FIVE(5, "未通过", null),
    SIX(6, "待完善", 7),
    SEVEN(7, "已完成", null),
    ;

    private Integer code;
    private String desc;
    private Integer nextOpt;

    public static String getDesc(Integer code) {
        for (ContractQuotationProStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (ContractQuotationProStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

    public static List<InitComboxStrVO> initCombox() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (ContractQuotationProStatusEnum value : ContractQuotationProStatusEnum.values()) {
            InitComboxStrVO tmp = new InitComboxStrVO();
            tmp.setName(value.getDesc());
            tmp.setId(value.getCode().longValue());
            list.add(tmp);
        }
        return list;
    }


    public static ContractQuotationProStatusEnum getEnum(Integer code) {
        for (ContractQuotationProStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
