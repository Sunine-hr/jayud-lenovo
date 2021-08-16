package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色数据权限
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddSystemRoleActionDataForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "权限ID")
    private List<Integer> actionId;

    @ApiModelProperty(value = "权限CODE")
    private String actionCode;

    @ApiModelProperty(value = "菜单CODE")
    private String menuCode;

    @ApiModelProperty(value = "权限类型（0无权限，1个人权限，2团队权限，3所有数据权限）")
    private Integer dateType;

    @ApiModelProperty(value = "备注")
    private String remark;
}
