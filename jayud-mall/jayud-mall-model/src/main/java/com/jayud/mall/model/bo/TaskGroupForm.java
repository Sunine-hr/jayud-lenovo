package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "提单任务分组Form")
public class TaskGroupForm {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "分组代码")
    private String idCode;

    @ApiModelProperty(value = "分组名")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "1提单任务分组 2运单任务分组")
    private Integer types;

}
