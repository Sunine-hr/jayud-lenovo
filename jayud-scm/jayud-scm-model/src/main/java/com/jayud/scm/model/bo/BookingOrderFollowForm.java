package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 委托单跟踪记录表
 */
@Data
public class BookingOrderFollowForm {

    @ApiModelProperty(value = "自动id")
    private Integer id;

    @ApiModelProperty(value = "委托单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "跟进类型")
    private String sType;

    @ApiModelProperty(value = "内容")
    private String followContext;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否同步在线平台(1为同步)")
    private Integer isOnl;

}
