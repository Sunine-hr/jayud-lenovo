package com.jayud.auth.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SysRole 实体类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="角色表对象", description="角色表")
public class AddSysRole extends SysBaseEntity {


    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称必填")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    @NotBlank(message = "角色编码必填")
    private String roleCode;

    @ApiModelProperty(value = "是否启用：0-否；1-是")
    private Integer status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    public void checkAddOrUpdate() {


    }
}
