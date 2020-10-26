package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author mfc
 */
@Data
public class SaveUserForm {

    @ApiModelProperty(value = "用户ID 修改时必传")
    private Long id;

    @ApiModelProperty(value = "用户姓名",required = true)
    @NotEmpty(message = "员工姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "所属公司")
//    @NotEmpty(message = "所属公司不能为空")
    private Long companyId;

    //工号

    @ApiModelProperty(value = "英文名")
    @NotEmpty(message = "英文名不能为空")
    private String enUserName;

    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    //角色



}
