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

    @ApiModelProperty(value = "评价(创建人评价)", position = 19)
    @JSONField(ordinal = 19)
    @NotNull(message = "评价不能为空")
    private String evaluation;

}
