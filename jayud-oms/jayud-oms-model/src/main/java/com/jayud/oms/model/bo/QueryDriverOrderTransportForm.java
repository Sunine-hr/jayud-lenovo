package com.jayud.oms.model.bo;

import com.jayud.oms.model.enums.DriverOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
public class QueryDriverOrderTransportForm extends BasePageForm {


    @ApiModelProperty(value = "订单状态 (0:全部,1:待提货(首页也是用这个状态),2:运输中,3:已完结)")
    @NotEmpty(message = "状态不能为空")
    private String status;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "后台使用，前端不用理会这个参数")
    private List<Long> orderIds;

    @ApiModelProperty(value = "后台使用，前端不用理会这个参数，排除订单信息")
    private List<Long> excludeOrderIds;

    @ApiModelProperty(value = "后台使用，前端不用理会这个参数,司机id")
    private Long driverId;



    public void assemblyOrder(Set<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }

        //首页待接货，排除已经接单订单状态是待接货
        switch (Objects.requireNonNull(DriverOrderStatusEnum.getEnumObj(this.status))) {
            case ALL:
                this.excludeOrderIds = new ArrayList<>();
                this.excludeOrderIds.addAll(orderIds);
                break;
            case PENDING:
                this.excludeOrderIds = new ArrayList<>();
                this.excludeOrderIds.addAll(orderIds);
                break;
            case IN_TRANSIT:
            case FINISHED:
                this.orderIds = new ArrayList<>();
                this.orderIds.addAll(orderIds);
                break;
            default:
        }
    }

}
