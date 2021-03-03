package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BillMasterForm {

    @ApiModelProperty(value = "账单主单id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "账单主单id不能为空")
    private Long id;

}
