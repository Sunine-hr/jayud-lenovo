package com.jayud.common.enums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel(value = "客户商品审核状态枚举")
@Getter
@AllArgsConstructor
public enum CustomerGoodsEnum {

    NO_PASS(-1, "审核不通过"),
    WAIT(0, "等待审核"),
    PASS(1, "审核通过")
    ;

    private int code;
    private String name;

    /**
     * 通过id，获取name
     * @param id
     * @return
     */
    public static String getName(int id) {
        CustomerGoodsEnum[] customerGoodsEnums = values();
        for (CustomerGoodsEnum customerGoodsEnum : customerGoodsEnums) {
            if (customerGoodsEnum.code == id) {
                return customerGoodsEnum.getName();
            }
        }
        return null;
    }

    /**
     * 通过name，获取id
     * @param name
     * @return
     */
    public static Integer getCode(String name) {
        CustomerGoodsEnum[] customerGoodsEnums = values();
        for (CustomerGoodsEnum customerGoodsEnum : customerGoodsEnums) {
            if (customerGoodsEnum.name.equals(name)) {
                return customerGoodsEnum.getCode();
            }
        }
        return null;
    }


}
