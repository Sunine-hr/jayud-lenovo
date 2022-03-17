package com.jayud.wms.model.bo;

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
public class CloseTheBoxForm {


    @ApiModelProperty(value = "发运复核id")
    private List<Long> ids;

    @ApiModelProperty(value = "箱号")
    private String boxNumber;


}
