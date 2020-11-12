package com.jayud.oms.model.po;

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
 * 司机接单信息(微信小程序)
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DriverOrderInfo对象", description="司机接单信息(微信小程序)")
public class DriverOrderInfo extends Model<DriverOrderInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接单id订单")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "中港订单编号")
    private String orderNo;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "状态（2:运输中,3:已完结）")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
