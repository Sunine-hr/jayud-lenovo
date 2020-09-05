package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddSystemUserForm {

    @ApiModelProperty(value = "用户的ID 修改时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "是否是负责人 1-是 0-否",required = true)
    private String isDepartmentCharge;

    @ApiModelProperty(value = "员工姓名",required = true)
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "岗位ID",required = true)
    private Long workId;

    @ApiModelProperty(value = "联系电话",required = true)
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;



}
