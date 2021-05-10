package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FabWarehouseParaForm {

    @ApiModelProperty(value = "应收FBA仓库ID", required = true, position = 1)
    @NotNull(message = "应收FBA仓库ID不能为空")
    private Integer id;

}
