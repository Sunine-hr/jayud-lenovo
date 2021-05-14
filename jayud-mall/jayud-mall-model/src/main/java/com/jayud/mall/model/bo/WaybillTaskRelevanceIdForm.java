package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WaybillTaskRelevanceIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "延期天数，默认延期1天")
    private Integer postponeDays;
}
