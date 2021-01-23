package com.jayud.tools.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CargoNameReplaceForm", description = "货品名称替换Form")
public class CargoNameReplaceForm {

    @ApiModelProperty(value = "主键ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "(原)货品名称", position = 2)
    @JSONField(ordinal = 2)
    private String hpmc;

    @ApiModelProperty(value = "替换名称", position = 3)
    @JSONField(ordinal = 3)
    private String replaceName;

    @ApiModelProperty(value = "查询关键字(货品名称or替换名称)", position = 4)
    @JSONField(ordinal = 4)
    private String name;

}
