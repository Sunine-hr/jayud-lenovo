package com.jayud.tools.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
public class DeleteForm {


    @ApiModelProperty(value = "所属仓库id")
    private List<Long> ids;


}
