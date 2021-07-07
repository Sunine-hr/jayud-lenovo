package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class OrderTrackNoticeForm {

    @ApiModelProperty(value = "关联订单ID(order_info id)")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @ApiModelProperty(value = "当前节点信息发生时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;


}
