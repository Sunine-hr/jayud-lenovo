package com.jayud.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单对应收货地址
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderTakeAdr对象", description="订单对应收货地址")
public class OrderTakeAdr extends Model<OrderTakeAdr> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Integer deliveryId;

    @ApiModelProperty(value = "提货日期")
    private LocalDateTime takeTime;

    @ApiModelProperty(value = "货物描述")
    private String describe;

    @ApiModelProperty(value = "数量")
    private Double amount;

    @ApiModelProperty(value = "单位(1件 2其他)")
    private Integer unit;

    @ApiModelProperty(value = "重量")
    private Integer weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "业务类型(product_biz)")
    private String bizCode;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer types;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
