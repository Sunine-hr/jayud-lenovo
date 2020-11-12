package com.jayud.tms.model.bo;

import com.jayud.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QueryDriverOrderTransportForm extends BasePageForm {

    @ApiModelProperty(value = "司机id")
    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单状态 (0:全部,1:待提货,2:运输中,3:已完结)")
    private String status;

    @ApiModelProperty(value = "后台使用，前端不用理会这个参数")
    private List<Long> orderIds;

    @ApiModelProperty(value = "后台使用，前端不用理会这个参数，排除订单信息")
    private List<Long> excludeOrderIds;

    public void convertOrderTransStatus() {
        if (StringUtils.isEmpty(this.status)) {
            return;
        }
        switch (this.status) {
            case "1":
                this.status = OrderStatusEnum.TMS_T_4.getCode();
                break;
        }
    }
}
