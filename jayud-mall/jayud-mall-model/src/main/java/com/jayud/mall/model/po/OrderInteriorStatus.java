package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 订单内部状态表(非流程状态)
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OrderInteriorStatus对象", description = "订单内部状态表(非流程状态)")
public class OrderInteriorStatus extends Model<OrderInteriorStatus> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "主状态类型(front前端 after后端)")
    private String mainStatusType;

    @ApiModelProperty(value = "主状态代码")
    private String mainStatusCode;

    @ApiModelProperty(value = "主状态名称")
    private String mainStatusName;

    @ApiModelProperty(value = "内部状态唯一码")
    private String interiorStatusCode;

    @ApiModelProperty(value = "内部状态名称")
    private String interiorStatusName;

    @ApiModelProperty(value = "状态标志")
    private String statusFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
