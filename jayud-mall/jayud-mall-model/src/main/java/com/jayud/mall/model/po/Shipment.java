package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 南京新智慧-运单装货信息
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Shipment对象", description="南京新智慧-运单装货信息")
public class Shipment extends Model<Shipment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "shipment_id", type = IdType.INPUT)
    private String shipment_id;

    @ApiModelProperty(value = "client_reference")
    private String client_reference;

    @ApiModelProperty(value = "服务")
    private String service;

    @ApiModelProperty(value = "件数")
    private Integer parcel_count;

    @ApiModelProperty(value = "收费重")
    private String chargeable_weight;

    @ApiModelProperty(value = "费用")
    private String charge_amount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "picking_time")
    private LocalDateTime picking_time;

    @ApiModelProperty(value = "rates_time")
    private LocalDateTime rates_time;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime creat_time;

    @ApiModelProperty(value = "创建人")
    private Long created;

    @ApiModelProperty(value = "shipmentJson")
    @TableField("shipmentJson")
    private String shipmentJson;


    @Override
    protected Serializable pkVal() {
        return this.shipment_id;
    }

}
