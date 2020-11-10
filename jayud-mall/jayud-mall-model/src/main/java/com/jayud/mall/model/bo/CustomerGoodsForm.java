package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerGoodsForm {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过")
    private Integer status;

    @ApiModelProperty(value = "报关编码(customs_data id_code)")
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)")
    private String clearanceCode;

}
