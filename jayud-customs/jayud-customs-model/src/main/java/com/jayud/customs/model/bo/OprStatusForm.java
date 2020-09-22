package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class OprStatusForm {
    @ApiModelProperty(value = "主订单ID",required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单ID",required = true)
    private Long orderId;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "操作人")
    @NotNull(message = "操作人不能为空")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    @NotNull(message = "操作时间不能为空")
    private LocalDateTime operatorTime;

    @ApiModelProperty(value = "附件")
    private String statusPic;

    @ApiModelProperty(value = "备注")
    private String description;

}
