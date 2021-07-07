package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBusinessLogForm extends BasePageForm {

    @ApiModelProperty(value = "操作人id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "操作人name(system_user name)")
    private String userName;

    @ApiModelProperty(value = "业务表tb")
    private String businessTb;

    @ApiModelProperty(value = "业务表(中文)name")
    private String businessName;
}
