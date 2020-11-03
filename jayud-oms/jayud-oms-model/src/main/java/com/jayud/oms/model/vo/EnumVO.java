package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 枚举
 */
@Data
public class EnumVO {
    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("描述")
    private String desc;

    public EnumVO(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public EnumVO() {
    }
}
