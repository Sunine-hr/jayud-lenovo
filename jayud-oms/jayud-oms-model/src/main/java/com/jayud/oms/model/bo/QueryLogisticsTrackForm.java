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

    @ApiModelProperty(value = "子订单ID")
    private String orderId;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "状态,仅后台使用")
    private String status;

}
