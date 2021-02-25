package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 应收应付录入费用表，财务编辑费用类型
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class OrderCostForm {

    @ApiModelProperty(value = "费用ID")
    private Long costId;

    @ApiModelProperty(value = "费用类型")
    private Long costGenreId;

    @ApiModelProperty(value = "当前登录用户")
    private String loginUserName;

    @ApiModelProperty(value = "开票申请操作:本金金额")
    private BigDecimal localMoney;

    @ApiModelProperty(value = "开票申请操作:本金汇率")
    private BigDecimal localMoneyRate;

}
