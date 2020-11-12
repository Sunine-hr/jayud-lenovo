package com.jayud.oms.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 司机小程序录用费用
 */
@Data
public class AddDriverOrderPaymentCostForm {

    @ApiModelProperty("中港订单id")
    @NotNull(message = "中港订单id不能为空")
    private Long orderId;

    @ApiModelProperty("费用代码")
    @NotEmpty(message = "费用代码不能为空")
    private String costCode;

    @ApiModelProperty("费用金额")
    @NotNull(message = "费用金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "附件地址集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "币种单位")
    @NotEmpty(message = "币种单位不能为空")
    private String currencyCode;
}
