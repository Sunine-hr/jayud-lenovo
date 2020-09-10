package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddSystemUserForm {

    @ApiModelProperty(value = "用户ID 修改时必传")
    private Long id;

    @ApiModelProperty(value = "是否是负责人 1-是 0-否",required = true)
    @NotEmpty(message = "是否是负责人不能为空")
    private String isDepartmentCharge;

    @ApiModelProperty(value = "员工姓名",required = true)
    @NotEmpty(message = "员工姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "岗位ID",required = true)
    @NotEmpty(message = "岗位ID不能为空")
    private Long workId;

    @ApiModelProperty(value = "联系电话",required = true)
    @NotEmpty(message = "联系电话不能为空")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID",required = true)
    @NotEmpty(message = "部门ID不能为空")
    private Long departmentId;



}
