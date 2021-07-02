package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 海运订单列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QuerySeaPortForm extends BasePageForm {

    @ApiModelProperty(value = "港口编码")
    private String code;

    @ApiModelProperty(value = "港口名称")
    private String name;

}
