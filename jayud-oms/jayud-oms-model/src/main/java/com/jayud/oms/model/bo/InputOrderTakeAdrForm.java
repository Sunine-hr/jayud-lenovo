package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提货/收货地址
 */
@Data
public class InputOrderTakeAdrForm {

    @ApiModelProperty(value = "提货/送货地址")
    private String address;

    //以上部分是加自定义地址使用
    //以下部分是选择地址使用,通过deliveryId区分

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

    @ApiModelProperty(value = "提货日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime takeTime;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer oprType;

    @ApiModelProperty(value = "入仓号")
    private String enterWarehouseNo;

}
