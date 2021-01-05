package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mfc
 */
@Data
public class SystemRoleVO {

    @ApiModelProperty(value = "主键ID", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "角色名称", position = 2)
    @JSONField(ordinal = 2)
    private String roleName;

    @ApiModelProperty(value = "角色描述", position = 3)
    @JSONField(ordinal = 3)
    private String roleDescribe;

    @ApiModelProperty(value = "创建人", position = 4)
    @JSONField(ordinal = 4)
    private String createBy;

    @ApiModelProperty(value = "创建时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "该角色所拥有的菜单Ids", required = true, position = 6)
    @JSONField(ordinal = 6)
    private List<Long> menuIds;


}
