package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 海运订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@Slf4j
public class SeaOrderProfitVO {

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "起运港")
    private String portDeparture;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "目的港")
    private String portDestination;

    @ApiModelProperty(value = "预计离港时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime etd;

    @ApiModelProperty(value = "预计到港时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eta;

    @ApiModelProperty(value = "散货件数")
    private Integer bulkCargoAmount;

    @ApiModelProperty(value = "散货单位")
    private String bulkCargoUnit;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务员")
    private String bizName;

    @ApiModelProperty(value = "利润")
    private BigDecimal profit;

    @ApiModelProperty(value = "利润")
    private String writeOff;

}
