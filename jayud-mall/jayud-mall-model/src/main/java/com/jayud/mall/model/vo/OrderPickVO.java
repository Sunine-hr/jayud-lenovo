package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderPickVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long orderId;

    @ApiModelProperty(value = "提货单号", position = 3)
    @JSONField(ordinal = 3)
    private String pickNo;

    @ApiModelProperty(value = "提货状态(1未提货 2正在提货 3已提货 4已到仓)")
    private Integer pickStatus;

    @ApiModelProperty(value = "进仓单号", position = 4)
    @JSONField(ordinal = 4)
    private String warehouseNo;

    @ApiModelProperty(value = "提货时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pickTime;

    @ApiModelProperty(value = "指定提货时间(后台拼车提货时指定)", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignPickTime;

    @ApiModelProperty(value = "重量", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal weight;

    @ApiModelProperty(value = "总体积(预报体积)", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal volume;

    @ApiModelProperty(value = "总箱数", position = 8)
    @JSONField(ordinal = 8)
    private Integer totalCarton;

    @ApiModelProperty(value = "备注", position = 9)
    @JSONField(ordinal = 9)
    private String remark;

    @ApiModelProperty(value = "提货地址id(delivery_address id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer addressId;

    @ApiModelProperty(value = "创建时间", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 11, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "运输id(transport id)")
    @JSONField(ordinal = 11)
    private Long transportId;

    /*提货地址基础数据表：delivery_address*/
    @ApiModelProperty(value = "联系人", position = 12)
    @JSONField(ordinal = 12)
    private String contacts;

    @ApiModelProperty(value = "联系电话", position = 13)
    @JSONField(ordinal = 13)
    private String phone;

    @ApiModelProperty(value = "国家代码", position = 14)
    @JSONField(ordinal = 14)
    private String countryCode;

    @ApiModelProperty(value = "国家名称", position = 15)
    @JSONField(ordinal = 15)
    private String countryName;

    @ApiModelProperty(value = "省/州代码", position = 16)
    @JSONField(ordinal = 16)
    private String stateCode;

    @ApiModelProperty(value = "省/州名称", position = 17)
    @JSONField(ordinal = 17)
    private String stateName;

    @ApiModelProperty(value = "城市代码", position = 18)
    @JSONField(ordinal = 18)
    private String cityCode;

    @ApiModelProperty(value = "城市名称", position = 19)
    @JSONField(ordinal = 19)
    private String cityName;

    @ApiModelProperty(value = "地址1", position = 20)
    @JSONField(ordinal = 20)
    private String address;

    @ApiModelProperty(value = "提货地址", position = 21)
    private String pickAddress;

    @ApiModelProperty(value = "订单编号", position = 22)
    private String orderNo;

    @ApiModelProperty(value = "集货仓库代码", position = 23)
    private String shippingWarehouseCode;

    @ApiModelProperty(value = "集货仓库名称", position = 24)
    private String shippingWarehouseName;

    @ApiModelProperty(value = "客户简称", position = 25)
    private String customerName;

    @ApiModelProperty(value = "截仓日期", position = 26)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 26, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    //运输
    @ApiModelProperty(value = "运输单号", position = 27)
    private String transportNo;

    @ApiModelProperty(value = "供应商名称", position = 28)
    private String supplierName;

}