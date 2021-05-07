package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class QueryWarehouseAreaShelvesForm extends BasePageForm{

    @ApiModelProperty(value = "区域id")
    @NotNull
    private String id;

}
