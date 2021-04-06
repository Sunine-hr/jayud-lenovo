package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransportVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输单号", position = 2)
    @JSONField(ordinal = 2)
    private String transportNo;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "追踪号", position = 4)
    @JSONField(ordinal = 4)
    private String trackingNo;

    @ApiModelProperty(value = "总重量", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal weight;

    @ApiModelProperty(value = "总体积", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal volume;

    @ApiModelProperty(value = "费用,金额", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 8)
    @JSONField(ordinal = 8)
    private Integer cid;

    @ApiModelProperty(value = "运输状态(1在途 2已送达)", position = 9)
    @JSONField(ordinal = 9)
    private Integer transportStatus;

    @ApiModelProperty(value = "创建日期", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
