package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaybillTaskRelevanceQueryForm {

    @ApiModelProperty(value = "是否个人任务(1 仅看自己的任务 2查看所有的任务)")
    public String isPersonal;

}
