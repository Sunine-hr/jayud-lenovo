package com.jayud.auth.model.bo;

import com.jayud.common.entity.PageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 订单列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QuerySeaPortForm extends PageForm {

    @ApiModelProperty(value = "港口编码")
    private String code;

    @ApiModelProperty(value = "港口名称")
    private String name;

}
