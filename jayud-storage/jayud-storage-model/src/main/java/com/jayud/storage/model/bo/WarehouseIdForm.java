package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WarehouseIdForm {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "仓库id不能为空")
    private Long id;

}
