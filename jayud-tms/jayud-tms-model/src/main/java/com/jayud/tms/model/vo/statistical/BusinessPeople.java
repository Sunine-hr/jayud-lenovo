package com.jayud.tms.model.vo.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 业务人员
 */
@Data
public class BusinessPeople {

    @ApiModelProperty("业务人员名称")
    private String createdUser;

    @ApiModelProperty("订单数")
    private Integer orderNum;

    @ApiModelProperty("总订单占比")
    private Integer proportionTotalOrders;

//    @ApiModelProperty("业务人员名称")
//    private String name;


    public void calculatePercentage(BigDecimal total) {
        this.proportionTotalOrders = new BigDecimal(orderNum).divide(total,4,BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
    }
}
