package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuerySysDeptForm extends BasePageForm {

    @ApiModelProperty(value = "机构/部门名称")
    private String departName;

    @ApiModelProperty(value = "创建时间")
    private String[] createTimeArr;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "机构类别 1集团，2公司，3部门")
    private String orgCategory;

    @ApiModelProperty(value = "机构类别 1集团，2公司，3部门")
    private List<String> notInOrgCategory;

}
