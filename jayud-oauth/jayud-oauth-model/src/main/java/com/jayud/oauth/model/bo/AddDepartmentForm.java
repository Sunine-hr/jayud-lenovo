package com.jayud.oauth.model.bo;

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

    @ApiModelProperty(value = "父级部门ID")
    private Long fId;

    @ApiModelProperty(value = "部门ID,修改时必传")
    private Long id;
}
