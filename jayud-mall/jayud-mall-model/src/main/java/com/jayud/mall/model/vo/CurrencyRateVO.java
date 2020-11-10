package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CurrencyRateVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "本币(currency_info id)")
    private Integer dcid;

    @ApiModelProperty(value = "他币(currency_info id)")
    private Integer ocid;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "本币-币种代码")
    private String dcCurrencyCode;

    @ApiModelProperty(value = "本币-币种名称")
    private String dcCurrencyName;

    @ApiModelProperty(value = "本币-国家代码")
    private String dcCountryCode;

    @ApiModelProperty(value = "他币-币种代码")
    private String ocCurrencyCode;

    @ApiModelProperty(value = "他币-币种名称")
    private String ocCurrencyName;

    @ApiModelProperty(value = "他币-国家代码")
    private String ocCountryCode;

    @ApiModelProperty(value = "汇率")
    private String rate;

}
