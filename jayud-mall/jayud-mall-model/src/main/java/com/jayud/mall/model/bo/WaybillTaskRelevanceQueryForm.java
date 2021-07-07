package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WaybillTaskRelevanceQueryForm {

    @ApiModelProperty(value = "是否个人任务(1 仅看自己的任务 2查看所有的任务)")
    @NotBlank(message = "是否个人任务不能为空")
    private String isPersonal;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

}
