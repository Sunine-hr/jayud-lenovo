package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "应收应付费用名称Form")
public class ReceivableCostForm {

    @ApiModelProperty(value = "自增ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "IDcode", position = 2)
    @JSONField(ordinal = 2)
    private String idCode;

    @ApiModelProperty(value = "名称", position = 3)
    @JSONField(ordinal = 3)
    private String costName;

    @ApiModelProperty(value = "辨认(1应收 2应付)", position = 4)
    @JSONField(ordinal = 4)
    private Integer identifying;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @TableField(value = "`status`")
    @JSONField(ordinal = 5)
    private String status;

}
