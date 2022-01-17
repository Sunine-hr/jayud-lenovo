package com.jayud.scm.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chuanmei
 */
@Data
public class SystemRoleAndSystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    private Integer status;

    @ApiModelProperty(value = "角色优先级")
    private Integer sort;

    @ApiModelProperty(value = "描述")
    private List<SystemUserSimpleVO> systemUserVOS;
}
