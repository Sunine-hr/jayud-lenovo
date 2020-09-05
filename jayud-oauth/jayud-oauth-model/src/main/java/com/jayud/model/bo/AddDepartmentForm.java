package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 新增部门
 * @author chuanmei
 */
@Data
public class AddDepartmentForm {

    @ApiModelProperty(value = "部门名称", required = true)
    @NotEmpty(message = "部门名称不能为空")
    private String name;
}
