package com.jayud.tms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中港运输订单信息
 * </p>
 *
 * @author cyc
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderTransport对象", description = "中港运输订单")
public class OutOrderTransportVO extends Model<OutOrderTransportVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中港订单ID")
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

}
