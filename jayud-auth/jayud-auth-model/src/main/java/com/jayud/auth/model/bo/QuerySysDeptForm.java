package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySysDeptForm extends BasePageForm {

    @ApiModelProperty(value = "机构/部门名称")
    private String departName;

    @ApiModelProperty(value = "创建时间")
    private String[] createTimeArr;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

}
