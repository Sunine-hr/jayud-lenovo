package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomerGoodsForm extends BasePageForm {

    @ApiModelProperty(value = "商品编码")
    private String sku;

    @ApiModelProperty(value = "中文名")
    private String nameCn;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "报关编码(customs_data id_code)")
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)")
    private String clearanceCode;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过")
    private Integer status;

}
