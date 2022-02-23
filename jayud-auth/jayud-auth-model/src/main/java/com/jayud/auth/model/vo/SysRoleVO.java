package com.jayud.auth.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * SysRole 实体类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "角色表对象", description = "角色表")
public class SysRoleVO extends SysBaseEntity {


    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "是否启用：0-否；1-是")
    private Integer status;

    @ApiModelProperty(value = "是否启用：0-否；1-是")
    private String statusDesc;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDesc = status == 0 ? "否" : "是";
    }
}
