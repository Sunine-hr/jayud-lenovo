package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 订单附件类型
 */
@Getter
@AllArgsConstructor
public enum OrderAttachmentTypeEnum {
    MANIFEST_ATTACHMENT("舱单附件"),
    CUSTOMS_ATTACHMENT("报关附件"),
    SIX_SHEET_ATTACHMENT("六联单号附件");
    private String desc;

//    public static String getDesc(String code) {
//        for (OrderAttachmentTypeEnum value : values()) {
//            if (Objects.equals(code, value.getCode())) {
//                return value.getDesc();
//            }
//        }
//        return "";
//    }
//
//    public static Integer getCode(String desc) {
//        for (OrderAttachmentTypeEnum value : values()) {
//            if (Objects.equals(desc, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }



}
