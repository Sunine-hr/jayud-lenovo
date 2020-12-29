package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationTeamVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "小组代码", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "小组名称", position = 3)
    @JSONField(ordinal = 3)
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)", position = 5)
    @JSONField(ordinal = 5)
    private Long creator;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

}
