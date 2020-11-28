package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationTeamVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "小组代码")
    private String groupCode;

    @ApiModelProperty(value = "小组名称")
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creationTime;

}
