package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class ListForm {

    @ApiModelProperty(value = "批量集合ID")
    private List<Long> ids;


}
