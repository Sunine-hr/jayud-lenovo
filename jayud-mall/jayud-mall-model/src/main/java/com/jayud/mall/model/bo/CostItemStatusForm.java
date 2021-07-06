package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CostItemStatusForm {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;


}
