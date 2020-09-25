package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 物流轨迹跟踪表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
@Data
public class QueryLogisticsTrackForm  {

    @ApiModelProperty(value = "关联主订单ID")
    private String mainOrderId;

    @ApiModelProperty(value = "关联订单ID")
    private String orderId;

}
