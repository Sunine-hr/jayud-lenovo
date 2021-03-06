package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 导出应付对账单
 */
@Data
public class ExportOrderFBillDetailVO {

    @ApiModelProperty(value = "主键用于排序")
    private Long id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "供应商,应付时取值")
    private String supplierChName;

    @ApiModelProperty(value = "开始核算期 年月")
    private String accountTermStr;

    @ApiModelProperty(value = "人民币")
    private BigDecimal rmb;

    @ApiModelProperty(value = "美元")
    private BigDecimal dollar;

    @ApiModelProperty(value = "欧元")
    private BigDecimal euro;

    @ApiModelProperty(value = "港币")
    private BigDecimal hKDollar;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal heXiaoAmount;

    @ApiModelProperty(value = "未核销金额")
    private BigDecimal notHeXiaoAmount;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "审核状态")
    private String auditStatusDesc;

    @ApiModelProperty(value = "付款申请")
    private String applyStatus;

    @ApiModelProperty(value = "生成账单人")
    private String makeUser;

    @ApiModelProperty(value = "生成账单时间")
    private String makeTimeStr;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private String auditTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "核销人")
    private String heXiaoUser;

    @ApiModelProperty(value = "核销时间")
    private String heXiaoTimeStr;

    @ApiModelProperty(value = "推金蝶次数")
    private Integer pushKingdeeCount;





}
