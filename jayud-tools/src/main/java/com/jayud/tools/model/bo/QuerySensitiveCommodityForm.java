package com.jayud.tools.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mfc
 */
@Data
public class QuerySensitiveCommodityForm extends BasePageForm{

    @ApiModelProperty(value = "品名")
    private String name;


}
