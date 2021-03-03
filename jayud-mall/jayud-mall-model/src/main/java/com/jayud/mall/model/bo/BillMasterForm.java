package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BillMasterForm {

    @ApiModelProperty(value = "账单主单id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

}
