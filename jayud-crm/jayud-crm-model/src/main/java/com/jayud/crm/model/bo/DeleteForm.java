package com.jayud.crm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author jay
 *
 */
@Data
public class DeleteForm {


    @ApiModelProperty(value = "idçéå")
    private List<Long> ids;


}
