package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderInfoCustomerForm {

    @ApiModelProperty(value = "客户ID(customer id)", position = 3)
    @JSONField(ordinal = 3)
    @NotNull(message = "客户ID不能为空")
    private Integer customerId;

}
