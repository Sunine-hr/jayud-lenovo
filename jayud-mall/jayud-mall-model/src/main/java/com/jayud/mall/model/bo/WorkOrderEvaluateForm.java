package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WorkOrderEvaluateForm {

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "工单id不能为空")
    private Long id;

    @ApiModelProperty(value = "客户评价", position = 15)
    @JSONField(ordinal = 15)
    @NotNull(message = "客户评价不能为空")
    private String customerEvaluation;

}
