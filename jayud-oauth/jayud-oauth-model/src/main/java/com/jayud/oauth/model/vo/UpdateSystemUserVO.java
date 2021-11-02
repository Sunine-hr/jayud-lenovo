package com.jayud.oauth.model.vo;

import com.jayud.common.utils.StringUtils;
import com.jayud.oauth.model.po.MsgUserChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author bocong.zheng
 */
@Data
public class UpdateSystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "部门")
    private String departmentName;

    @ApiModelProperty(value = "岗位id")
    private Long workId;

    @ApiModelProperty(value = "岗位")
    private String workName;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "所属公司id")
    private Long companyId;

    @ApiModelProperty(value = "所属公司")
    private String companyName;

//    @ApiModelProperty(value = "所属上级id")
//    private Long superiorId;
//
//    @ApiModelProperty(value = "所属上级")
//    private String superiorName;

    @ApiModelProperty(value = "法人主体ID集合", required = true)
    @NotNull(message = "legalEntityIds is required")
    private List<Long> legalEntityIds = new ArrayList<>();

    //以下字段是为了给组织架构处编辑员工回显
    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否部门负责人 1-是 0-否")
    private String isDepartmentCharge;

    @ApiModelProperty(value = "消息渠道集合")
    private List<MsgUserChannel> msgUserChannelList;

    @ApiModelProperty(value = "兼职部门Id", required = true)
    private String partTimeDepId;

    @ApiModelProperty(value = "兼职部门Ids", required = true)
    private Set<Long> partTimeDepIds;

    public void setPartTimeDepId(String partTimeDepId) {
        this.partTimeDepId = partTimeDepId;
        if (!StringUtils.isEmpty(partTimeDepId)) {
            String[] split = partTimeDepId.split(",");
            partTimeDepIds = new HashSet<>();
            for (String id : split) {
                partTimeDepIds.add(Long.valueOf(id));
            }
        }
    }
}

