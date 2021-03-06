package com.jayud.tms.model.vo;

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
public class OrderTakeAdrVO extends Model<OrderTakeAdrVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

    @ApiModelProperty(value = "提货日期")
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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "提货送货文件")
    private String file;

    @ApiModelProperty(value = "提货送货文件名称")
    private String fileName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
