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
public class DeleteForm{


    @ApiModelProperty(value = "所属仓库id")
    private List<Long> ids;

    @ApiModelProperty(value = "编码集合")
    private List<String> numberList;


}
