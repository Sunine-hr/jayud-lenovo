package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单对应提货信息表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderPick对象", description="订单对应提货信息表")
public class OrderPick extends Model<OrderPick> {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "总体积", position = 7)
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
