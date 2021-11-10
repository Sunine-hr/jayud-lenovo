package com.jayud.oauth.model.bo;

import com.jayud.oauth.model.po.MsgUserChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
public class AddSystemUserForm {

    @ApiModelProperty(value = "用户ID 修改时必传")
    private Long id;

    @ApiModelProperty(value = "是否是负责人 1-是 0-否", required = true)
    @NotEmpty(message = "是否是负责人不能为空")
    private String isDepartmentCharge;

    @ApiModelProperty(value = "员工姓名", required = true)
    @NotEmpty(message = "员工姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "岗位ID", required = true)
    @NotEmpty(message = "岗位ID不能为空")
    private Long workId;

    @ApiModelProperty(value = "岗位描述", required = true)
    @NotEmpty(message = "岗位描述不能为空")
    private String workName;

    @ApiModelProperty(value = "联系电话", required = true)
    @NotEmpty(message = "联系电话不能为空")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID", required = true)
    @NotEmpty(message = "部门ID不能为空")
    private Long departmentId;

    @ApiModelProperty(value = "兼职部门Ids", required = true)
    private Set<Long> partTimeDepIds;

    @ApiModelProperty(value = "兼职部门Id", required = true)
    private String partTimeDepId;

    @ApiModelProperty(value = "消息渠道集合")
    private List<MsgUserChannel> msgUserChannelList;

    public void setPartTimeDepIds(Set<Long> partTimeDepIds) {
        this.partTimeDepIds = partTimeDepIds;
        if (!CollectionUtils.isEmpty(partTimeDepIds)) {
            StringBuilder sb = new StringBuilder();
            partTimeDepIds.forEach(e -> {
                sb.append(e).append(",");
            });
            this.partTimeDepId = sb.toString();
        } else {
            this.partTimeDepId = "";
        }
    }

    public void setPartTimeDepId(String partTimeDepId) {
        this.partTimeDepId = CollectionUtils.isEmpty(partTimeDepIds) ? "" : partTimeDepId;
    }

}
