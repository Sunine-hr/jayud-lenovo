package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 柜子订单信息
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CounterOrderInfo对象", description = "柜子订单信息")
public class CounterOrderInfo extends Model<CounterOrderInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "柜子清单信息表(counter_list_info id)")
    private Long bId;

    @ApiModelProperty(value = "清单名称(counter_list_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单类型(1普通运单 2留仓运单)")
    private Integer orderType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
