package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class OprSystemUserForm {

    @ApiModelProperty(value = "主键ID,修改和删除时必传")
    private Long id;

    @ApiModelProperty(value = "登录名",required = true)
    @NotEmpty(message = "登录名不能为空")
    private String name;

    @ApiModelProperty(value = "用户姓名",required = true)
    @NotEmpty(message = "用户姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "部门ID",required = true)
    @NotEmpty(message = "部门ID不能为空")
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID",required = true)
    @NotEmpty(message = "岗位ID不能为空")
    private Long workId;

    @ApiModelProperty(value = "岗位",required = true)
    @NotEmpty(message = "岗位不能为空")
    private String workName;

    @ApiModelProperty(value = "角色ID",required = true)
    @NotEmpty(message = "角色ID不能为空")
    private Long roleId;

    @ApiModelProperty(value = "所属公司ID",required = true)
    @NotEmpty(message = "所属公司ID不能为空")
    private Long companyId;

    @ApiModelProperty(value = "所属上级ID",required = true)
    @NotEmpty(message = "所属上级ID不能为空")
    private Long superiorId;

    @ApiModelProperty(value = "操作指令",required = true)
    @Pattern(regexp = "update|delete",message = "cmd requires 'update' or 'delete' only")
    private String cmd;



}
