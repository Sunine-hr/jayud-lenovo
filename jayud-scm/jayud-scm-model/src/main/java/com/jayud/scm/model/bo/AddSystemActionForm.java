package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 按钮权限设置表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddSystemActionForm {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级ID")
    @NotNull(message = "父级id不为空")
    private Integer parentId;

    @ApiModelProperty(value = "权限名称")
    @NotNull(message = "权限名称不为空")
    private String actionName;

    @ApiModelProperty(value = "权限CODE")
    @NotNull(message = "权限CODE不为空")
    private String actionCode;

    @ApiModelProperty(value = "是否为审核按钮")
    private Boolean isAudit;

}
