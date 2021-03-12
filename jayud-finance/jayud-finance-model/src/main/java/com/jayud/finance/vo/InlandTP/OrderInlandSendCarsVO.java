package com.jayud.finance.vo.InlandTP;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 内陆派车信息
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInlandSendCarsVO extends Model<OrderInlandSendCarsVO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "运输订单号")
    private String transportNo;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(例如:3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审核意见")
    private String describes;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
