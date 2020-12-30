package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomerGoodsForm extends BasePageForm {

    @ApiModelProperty(value = "客户ID,默认登录时后台就有", position = 1)
    @JSONField(ordinal = 1)
    private Integer customerId;

    @ApiModelProperty(value = "SKU商品编码", position = 2)
    @JSONField(ordinal = 2)
    private String sku;

    @ApiModelProperty(value = "中文名", position = 3)
    @JSONField(ordinal = 3)
    private String nameCn;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过", position = 4)
    @JSONField(ordinal = 4)
    private Integer status;

    @ApiModelProperty(value = "报关编码(customs_data id_code)", position = 5)
    @JSONField(ordinal = 5)
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)", position = 6)
    @JSONField(ordinal = 6)
    private String clearanceCode;

}
