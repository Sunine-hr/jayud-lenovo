package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerGoodsIsValidForm {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "是否有效(0无效 1有效)")
    private Integer isValid;

}
