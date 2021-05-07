package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryWarehouseAreaShelvesLocationForm extends BasePageForm{

    @ApiModelProperty(value = "仓库区域货架id")
    @NotNull
    private Long shelvesId;

}
