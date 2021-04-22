package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliverInfoVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输管理id(transport id)", position = 2)
    @JSONField(ordinal = 2)
    private Long transportId;

    @ApiModelProperty(value = "提货id(order_pick id)", position = 2)
    @JSONField(ordinal = 2)
    private Long orderPickId;

    @ApiModelProperty(value = "进仓单号(order_pick warehouse_no)", position = 3)
    @JSONField(ordinal = 3)
    private String warehouseNo;

    @ApiModelProperty(value = "提货单号(order_pick pick_no)", position = 4)
    @JSONField(ordinal = 4)
    private String pickNo;

    @ApiModelProperty(value = "箱数(order_pick total_carton)", position = 5)
    @JSONField(ordinal = 5)
    private Integer totalCarton;

    @ApiModelProperty(value = "预计时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;

    @ApiModelProperty(value = "集货仓库代码(shipping_area warehouse_code)", position = 7)
    @JSONField(ordinal = 7)
    private String warehouseCode;

    @ApiModelProperty(value = "集货仓库名称(shipping_area warehouse_name)", position = 8)
    @JSONField(ordinal = 8)
    private String warehouseName;

    @ApiModelProperty(value = "联系人(shipping_area contacts)", position = 9)
    @JSONField(ordinal = 9)
    private String contacts;

    @ApiModelProperty(value = "联系手机(shipping_area contact_phone)", position = 10)
    @JSONField(ordinal = 10)
    private String contactPhone;

    //送货地址(集货仓库)
    @ApiModelProperty(value = "送货地址(集货仓库)(shipping_area pname + cname + address_first)", position = 11)
    @JSONField(ordinal = 11)
    private String deliveryAddress;

}
