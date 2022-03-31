package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryForm extends BasePageForm {

    @ApiModelProperty("搜索条件")
    private String condition;

    @ApiModelProperty("搜索key")
    private String key;

    @ApiModelProperty("模糊条件")
    private String name;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("业务类型")
    private Integer modelType;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;
}
