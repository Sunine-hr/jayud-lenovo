package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomsClearanceForm extends BasePageForm {

    @ApiModelProperty(value = "IDcode，清关ID")
    private String idCode;

    @ApiModelProperty(value = "清关国家代码")
    private String customsCode;

    @ApiModelProperty(value = "清关HSCODE")
    private String hsCode;

    @ApiModelProperty(value = "审核状态(0待审核 1已审核 2已取消)")
    private Integer auditStatus;

}
