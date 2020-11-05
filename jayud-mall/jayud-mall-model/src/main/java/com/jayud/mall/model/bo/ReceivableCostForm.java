package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "应收应付费用名称Form")
public class ReceivableCostForm {

    @ApiModelProperty(value = "自增ID")
    private Long id;

    @ApiModelProperty(value = "IDcode")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String costName;

    @ApiModelProperty(value = "1应收 2应付")
    private Integer identifying;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    private String status;

}
