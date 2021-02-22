package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SaveCounterCaseForm {

    @ApiModelProperty(value = "提单柜号id(ocean_counter id)", position = 1, required = true)
    @JSONField(ordinal = 1)
    @NotNull(message = "提单柜号id不能为空")
    private Long oceanCounterId;

    @ApiModelProperty(value = "运单箱号id(order_case id) list", position = 2, required = true)
    @JSONField(ordinal = 2)
    @NotEmpty(message = "运单箱号id不能为空")
    private List<Long> orderCaseIds;

}
