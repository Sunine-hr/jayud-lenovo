package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "订单对应箱号信息VO")
public class OrderCaseVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer orderId;

    @ApiModelProperty(value = "箱号", position = 3)
    @JSONField(ordinal = 3)
    private String cartonNo;

    @ApiModelProperty(value = "FAB箱号", position = 4)
    @JSONField(ordinal = 4)
    private String fabNo;

    @ApiModelProperty(value = "客户测量的长度，单位cm", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal asnLength;

    @ApiModelProperty(value = "客户测量的宽度，单位cm", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal asnWidth;

    @ApiModelProperty(value = "客户测量的高度，单位cm", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal asnHeight;

    @ApiModelProperty(value = "客户测量的重量，单位kg 实际重", position = 8)
    @JSONField(ordinal = 8)
    private BigDecimal asnWeight;

    @ApiModelProperty(value = "预报长宽高计算得到的体积，单位m³", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal asnVolume;

    @ApiModelProperty(value = "客户填写时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime asnWeighDate;

    @ApiModelProperty(value = "仓库测量的长度，单位cm", position = 10)
    @JSONField(ordinal = 10)
    private BigDecimal wmsLength;

    @ApiModelProperty(value = "仓库测量的高度，单位cm", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal wmsHeight;

    @ApiModelProperty(value = "仓库测量的宽度，单位cm", position = 12)
    @JSONField(ordinal = 12)
    private BigDecimal wmsWidth;

    @ApiModelProperty(value = "仓库测量的重量，单位kg", position = 13)
    @JSONField(ordinal = 13)
    private BigDecimal wmsWeight;

    @ApiModelProperty(value = "仓库计量长宽高得到的体积，单位m³", position = 14)
    @JSONField(ordinal = 14)
    private BigDecimal wmsVolume;

    @ApiModelProperty(value = "仓库测量时间", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime wmsWeighDate;

    @ApiModelProperty(value = "最终确认长度，单位cm", position = 16)
    @JSONField(ordinal = 16)
    private BigDecimal confirmLength;

    @ApiModelProperty(value = "最终确认高度，单位cm", position = 17)
    @JSONField(ordinal = 17)
    private BigDecimal confirmHeight;

    @ApiModelProperty(value = "最终确认宽度，单位cm", position = 18)
    @JSONField(ordinal = 18)
    private BigDecimal confirmWidth;

    @ApiModelProperty(value = "最终确认重量，单位kg", position = 19)
    @JSONField(ordinal = 19)
    private BigDecimal confirmWeight;

    @ApiModelProperty(value = "最终确认体积，单位 m³", position = 20)
    @JSONField(ordinal = 20)
    private BigDecimal confirmVolume;

    @ApiModelProperty(value = "最终确定时间", position = 21)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 21, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmWeighDate;

    @ApiModelProperty(value = "是否已确认（0-未确认,1-已确认）", position = 22)
    @JSONField(ordinal = 22)
    private Integer status;

    @ApiModelProperty(value = "备注", position = 23)
    @JSONField(ordinal = 23)
    private String remark;

    @ApiModelProperty(value = "Amazon Reference ID(亚马逊引用ID)")
    private String amazonReferenceId;

    @ApiModelProperty(value = "扩展单号")
    private String extensionNumber;

    /*客户预报长宽高*/
    @ApiModelProperty(value = "客户预报长宽高", position = 24)
    @JSONField(ordinal = 24)
    private String asnLwg;

    /*订单信息*/
    @ApiModelProperty(value = "订单号", position = 25)
    @JSONField(ordinal = 25)
    private String orderNo;

    @ApiModelProperty(value = "客户ID，对应customer.id", position = 26)
    @JSONField(ordinal = 26)
    private Integer customerId;

    /*客户信息*/
    @ApiModelProperty(value = "中文名，联系人", position = 27)
    @JSONField(ordinal = 27)
    private String nameCn;

    @ApiModelProperty(value = "公司名", position = 28)
    @JSONField(ordinal = 28)
    private String company;

    /**订单箱号配载信息**/
    /*运单表：ocean_waybill 被删除了*/
    @ApiModelProperty(value = "运单号", position = 29)
    @JSONField(ordinal = 29)
    private String waybillNo;

    /*提单对应货柜信息：ocean_counter*/
    @ApiModelProperty(value = "柜号", position = 30)
    @JSONField(ordinal = 30)
    private String cntrNo;

    /*提单表：ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)", position = 31)
    @JSONField(ordinal = 31)
    private String oceanBillNo;

    /*配载单:order_conf*/
    @ApiModelProperty(value = "配载单号", position = 32)
    @JSONField(ordinal = 32)
    private String orderConfNo;

    /*材积重 = (长cm * 宽cm * 高cm) / 计泡系数 */
    @ApiModelProperty(value = "材积重", position = 33)
    private BigDecimal volumeWeight;

    /* 收费重，谁大取谁 */
    @ApiModelProperty(value = "收费重", position = 20)
    private BigDecimal chargeWeight;

    @ApiModelProperty(value = "订单类型(1普通运单 2留仓运单)")
    private Integer orderType;

    //订单箱号对应商品信息
    @ApiModelProperty(value = "订单箱号对应商品信息 list")
    private List<OrderCaseShopVO> orderCaseShopList;

}
