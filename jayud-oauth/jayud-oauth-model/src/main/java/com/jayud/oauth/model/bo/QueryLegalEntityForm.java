package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryLegalEntityForm extends BasePageForm{

    @ApiModelProperty("法人主体")
    private String legalName;

    @ApiModelProperty("审核状态")
    private String auditStatus;

    @ApiModelProperty("操作指令cmd为cw时获取1-待审核和0-拒绝的数据,为zjb时获取1-待审核的数据")
    private String cmd;

}
