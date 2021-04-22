package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TradingRecordQueryForm {

    @ApiModelProperty(value = "客户ID(customer id)", position = 1)
    @JSONField(ordinal = 1)
    private Long customerId;

    //交易单号
    @ApiModelProperty(value = "交易单号", position = 2)
    @JSONField(ordinal = 2)
    private String tradingNo;

    //交易类型
    @ApiModelProperty(value = "交易类型(1充值 2支付)", position = 3)
    @JSONField(ordinal = 3)
    private Integer tradingType;

    //币种
    @ApiModelProperty(value = "币种(currency_info id)", position = 4)
    @JSONField(ordinal = 4)
    private Long cid;

}
