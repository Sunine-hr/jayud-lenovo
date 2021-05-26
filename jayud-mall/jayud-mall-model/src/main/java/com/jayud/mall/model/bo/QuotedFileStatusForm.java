package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuotedFileStatusForm {

    @ApiModelProperty(value = "自增加id")
    private Long id;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;
}
