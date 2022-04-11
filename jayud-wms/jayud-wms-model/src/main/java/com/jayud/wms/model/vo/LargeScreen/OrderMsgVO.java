package com.jayud.wms.model.vo.LargeScreen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * @author ciro
 * @date 2022/4/11 14:49
 * @description:    工单数据
 */
@Data
@ApiModel(value = "工单数量", description = "工单数量")
public class OrderMsgVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "订单类型(1-入库通知单,2-入库单,3-质检单,4-出库通知单,5-出库单,6-发运复核单)")
    private Integer orderType;

    @ApiModelProperty(value = "订单类型-文本")
    private String orderType_text;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单状态-文本")
    private String orderStatus_text;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
