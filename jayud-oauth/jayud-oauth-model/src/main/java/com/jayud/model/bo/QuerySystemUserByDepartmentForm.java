package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySystemUserByDepartmentForm extends BasePageForm{

    @ApiModelProperty("部门ID")
    private String departmentId;

    @ApiModelProperty("用户姓名")
    private String userName;

}
