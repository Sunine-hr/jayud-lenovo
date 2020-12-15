package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskGroupVO {

    @ApiModelProperty(value = "自增id", position = 1)
    private Long id;

    @ApiModelProperty(value = "分组代码", position = 2)
    private String idCode;

    @ApiModelProperty(value = "分组名", position = 3)
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "1提单任务分组 2运单任务分组", position = 5)
    private Integer types;

}
