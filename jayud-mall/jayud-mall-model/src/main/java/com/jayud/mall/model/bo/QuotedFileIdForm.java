package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuotedFileIdForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    @NotNull(message = "id不能为空")
    private Long id;
}
