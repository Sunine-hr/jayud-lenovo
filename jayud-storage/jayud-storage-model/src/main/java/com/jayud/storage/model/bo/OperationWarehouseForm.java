package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OperationWarehouseForm {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty(value = "0为无效，1为有效")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
