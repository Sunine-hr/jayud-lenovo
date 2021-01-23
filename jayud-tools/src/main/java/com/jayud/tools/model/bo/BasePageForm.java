package com.jayud.tools.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础分页表单
 */
@Data
public class BasePageForm {

    @ApiModelProperty(value = "页码", position = 101)
    @JSONField(ordinal = 101)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页长", position = 102)
    @JSONField(ordinal = 102)
    private Integer pageSize = 10;


}
