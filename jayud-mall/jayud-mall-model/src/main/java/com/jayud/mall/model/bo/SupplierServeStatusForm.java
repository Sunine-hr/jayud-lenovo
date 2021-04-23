package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SupplierServeStatusForm {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 2)
    @JSONField(ordinal = 2)
    @NotBlank(message = "状态不能为空")
    private String status;

}
