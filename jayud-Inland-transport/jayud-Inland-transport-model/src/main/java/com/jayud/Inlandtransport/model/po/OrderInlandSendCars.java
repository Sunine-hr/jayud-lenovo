package com.jayud.Inlandtransport.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@ApiModel(value="OrderInlandSendCars对象", description="内陆派车信息")
public class OrderInlandSendCars extends Model<OrderInlandSendCars> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "车型id")
    private Long vehicleId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审核意见")
    private String describes;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
