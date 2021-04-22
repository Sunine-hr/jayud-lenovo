package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CurrencyRateVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "本币(currency_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer dcid;

    @ApiModelProperty(value = "他币(currency_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer ocid;

    @ApiModelProperty(value = "汇率", position = 4)
    @JSONField(ordinal = 4)
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @JSONField(ordinal = 5)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 7)
    @JSONField(ordinal = 7)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "本币-币种代码", position = 9)
    @JSONField(ordinal = 9)
    private String dcCurrencyCode;

    @ApiModelProperty(value = "本币-币种名称", position = 10)
    @JSONField(ordinal = 10)
    private String dcCurrencyName;

    @ApiModelProperty(value = "本币-国家代码", position = 11)
    @JSONField(ordinal = 11)
    private String dcCountryCode;

    @ApiModelProperty(value = "他币-币种代码", position = 12)
    @JSONField(ordinal = 12)
    private String ocCurrencyCode;

    @ApiModelProperty(value = "他币-币种名称", position = 13)
    @JSONField(ordinal = 13)
    private String ocCurrencyName;

    @ApiModelProperty(value = "他币-国家代码", position = 14)
    @JSONField(ordinal = 14)
    private String ocCountryCode;

    @ApiModelProperty(value = "汇率", position = 15)
    @JSONField(ordinal = 15)
    private String rate;

}
