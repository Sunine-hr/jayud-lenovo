package com.jayud.oms.model.bo;

import com.jayud.oms.model.vo.LogisticsTrackVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 物流轨迹跟踪表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
@Data
public class LogisticsTrackForm {

    @ApiModelProperty(value = "确认反馈对象")
    private List<LogisticsTrackVO> logisticsTrackForms;

    @ApiModelProperty(value = "关联主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "关联订单ID")
    private Long orderId;

}
