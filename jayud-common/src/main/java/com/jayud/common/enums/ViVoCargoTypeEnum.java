package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ViVoCargoTypeEnum {
    GENERAL_CARGO(1,"普货"),
    CELL(2,"电池"),
    CHEMICALS(3,"化学品"),
    DANGEROUS_GOODS(4,"危险品"),
    TEMPORARY_SERVICE(5,"临时业务"),
     ;
    public Integer code;
    public String message;
}
