//package com.jayud.oauth.model.enums;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Objects;
//
//@Getter
//@AllArgsConstructor
//public enum UserTypeEnum {
//
//    EMPLOYEE_TYPE("1", "员工类型"),
//    CUSTOMER_TYPE("2", "客户类型"),
//    SUPPLIER_TYPE("3", "供应商");
//
//    private String code;
//    private String desc;
//
//    public static String getDesc(String code) {
//        for (UserTypeEnum value : values()) {
//            if (Objects.equals(code, value.getCode())) {
//                return value.getDesc();
//            }
//        }
//        return "";
//    }
//
//    public static UserTypeEnum getEnum(String code) {
//        for (UserTypeEnum value : values()) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
//        return null;
//    }
//}
