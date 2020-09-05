package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class OprSystemUserForm {

    @ApiModelProperty(value = "主键ID",required = true)
    private Long id;

    @ApiModelProperty(value = "登录名",required = true)
    private String name;

    @ApiModelProperty(value = "用户姓名",required = true)
    private String userName;

    @ApiModelProperty(value = "英文名",required = true)
    private String enUserName;

    @ApiModelProperty(value = "部门ID",required = true)
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID",required = true)
    private Long workId;

    @ApiModelProperty(value = "角色ID",required = true)
    private Long roleId;

    @ApiModelProperty(value = "所属公司ID",required = true)
    private Long companyId;

    @ApiModelProperty(value = "所属上级ID",required = true)
    private Long superiorId;

    @ApiModelProperty(value = "操作指令",required = true)
    @Pattern(regexp = "save|update|delete",message = "cmd requires 'save' or 'update' or 'delete' only")
    private String cmd;



}
