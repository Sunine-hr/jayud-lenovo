package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/17 9:43
 * @description: 包装规格
 */
@Getter
@AllArgsConstructor
public enum PackingEnum {

    MAIN_PACKING(1,"主包装"),
    SMALL_PACKING(2,"小包装"),
    MEDIUM_PACKING(3,"中包装"),
    LARGE_PACKING(4,"大包装");







    int specsType;

    String specsTypeName;


}
