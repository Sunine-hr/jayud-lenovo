package com.jayud.crm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum FileModuleEnum {

    CQ("ContractQuotation", "合同报价"),
    CCFC("CrmCustomerFollowCode", "客户管理_跟进记录_业务标识"),
    CA("ContractAgreement", "合同协议"),
    SUB_CA("ContractAgreementSub", "合同子协议"),
    CRM_FILE("crm_file", "附件"),
    ;


    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (FileModuleEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static String getCode(String desc) {
        for (FileModuleEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return "";
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
