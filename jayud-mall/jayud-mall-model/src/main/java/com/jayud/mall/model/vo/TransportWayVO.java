package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransportWayVO {

    @ApiModelProperty(value = "自增加id")
    private Long id;

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField("`status`")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
