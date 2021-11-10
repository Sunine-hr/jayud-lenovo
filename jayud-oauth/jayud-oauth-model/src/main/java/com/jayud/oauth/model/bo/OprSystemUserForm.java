package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

//    @ApiModelProperty(value = "所属公司ID",required = true)
//    @NotEmpty(message = "所属公司ID不能为空")
//    private Long companyId;
//
//    @ApiModelProperty(value = "所属上级ID",required = true)
//    @NotEmpty(message = "所属上级ID不能为空")
//    private Long superiorId;

    @ApiModelProperty(value = "法人主体ID集合",required = true)
    @NotNull(message = "legalEntityIds is required")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "操作指令",required = true)
    @Pattern(regexp = "update|delete",message = "cmd requires 'update' or 'delete' only")
    private String cmd;

    @ApiModelProperty(value = "兼职部门Ids", required = true)
    private Set<Long> partTimeDepIds;

    @ApiModelProperty(value = "兼职部门Id", required = true)
    private String partTimeDepId;

    public void setPartTimeDepIds(Set<Long> partTimeDepIds) {
        this.partTimeDepIds = partTimeDepIds;
        if (!CollectionUtils.isEmpty(partTimeDepIds)) {
            StringBuilder sb = new StringBuilder();
            partTimeDepIds.forEach(e -> {
                sb.append(e).append(",");
            });
            this.partTimeDepId = sb.toString();
        }else {
            this.partTimeDepId="";
        }
    }
}
