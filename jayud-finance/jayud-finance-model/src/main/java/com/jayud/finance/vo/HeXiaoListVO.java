package com.jayud.finance.vo;


import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 核销列表
 */
@Data
public class HeXiaoListVO {

    @ApiModelProperty(value = "核销ID")
    private Long id;

    @ApiModelProperty(value = "对账单编号")
    private String billNo;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal realReceiveMoney;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "折合金额")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "核销方式")
    private String oprMode;

    @ApiModelProperty(value = "实际收款时间")
    private LocalDateTime realReceiveTime;

    @ApiModelProperty(value = "实际收款时间显示")
    private String realReceiveTimeStr;

    public String getRealReceiveTimeStr() {
        if(this.realReceiveTime != null){
            return DateUtils.getLocalToStr(this.realReceiveTime);
        }
        return "";
    }


}
