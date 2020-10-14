package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "orderId is required")
    private String orderId;

    @ApiModelProperty(value = "业务类型")
    @NotEmpty(message = "classCode is required")
    private String classCode;

    @ApiModelProperty(value = "状态,仅后台使用")
    private String status;

}
