package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单服务表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderService对象", description="订单服务表")
public class OrderService extends Model<OrderService> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自动增长")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "服务id(service_item id)")
    private Long serviceId;

    @ApiModelProperty(value = "服务名称(service_item name)")
    private String serviceName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
