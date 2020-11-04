package com.jayud.tools.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "货品名称替换Form")
public class CargoNameReplaceForm {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "(原)货品名称")
    private String hpmc;

    @ApiModelProperty(value = "替换名称")
    private String replaceName;

}
