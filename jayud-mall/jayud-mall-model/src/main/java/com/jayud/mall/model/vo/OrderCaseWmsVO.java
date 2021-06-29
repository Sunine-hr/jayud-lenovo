package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderCaseWmsVO {

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = "仓库测量的长度，单位cm")
    private BigDecimal wmsLength;

    @ApiModelProperty(value = "仓库测量的宽度，单位cm")
    private BigDecimal wmsWidth;

    @ApiModelProperty(value = "仓库测量的高度，单位cm")
    private BigDecimal wmsHeight;

    @ApiModelProperty(value = "仓库测量的重量，单位kg")
    private BigDecimal wmsWeight;

    @ApiModelProperty(value = "仓库计量长宽高得到的体积，单位m³")
    private BigDecimal wmsVolume;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Integer orderId;

}
