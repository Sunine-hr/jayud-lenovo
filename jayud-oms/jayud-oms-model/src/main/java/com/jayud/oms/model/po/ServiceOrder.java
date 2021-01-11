package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderTransport对象", description = "中港运输订单")
public class ServiceOrder extends Model<ServiceOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "主订单(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "服务类型(0:费用补录,1:单证费用,2:存仓费用,快递费用)")
    private Integer type;

    @ApiModelProperty(value = "关联订单")
    private String associatedOrder;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建用户")
    private String createdUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改用户")
    private String updatedUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

