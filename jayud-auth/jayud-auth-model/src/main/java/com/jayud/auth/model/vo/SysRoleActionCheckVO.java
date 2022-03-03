package com.jayud.auth.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * SysRoleActionCheckVO 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
public class SysRoleActionCheckVO extends SysBaseEntity {


    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "权限ID")
    private Long actionId;

    @ApiModelProperty(value = "审核CODE")
    private String actionCode;

    @ApiModelProperty(value = "按钮名称")
    private String actionName;

    @ApiModelProperty(value = "审核级别")
    private Integer checkLevel;

    @ApiModelProperty(value = "权限最高金额,0不限制")
    private BigDecimal checkMoney;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "删除标记")
    @TableLogic
    private Boolean isDeleted;

}
