package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ListForm {

    @ApiModelProperty(value = "批量集合ID",required = true)
    @NotNull(message = "ids is required")
    private List<Long> ids;

    @ApiModelProperty(value = "操作指令")
    private String cmd;


}
