package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户收入表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerEarnings对象", description="客户收入表")
public class CustomerEarnings extends Model<CustomerEarnings> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
      private Integer id;

    @ApiModelProperty(value = "年营业额")
    private BigDecimal yearBusiness;

    @ApiModelProperty(value = "总资产")
    private BigDecimal totalAssets;

    @ApiModelProperty(value = "资产负债率")
    private BigDecimal assetLiabilityRatio;

    @ApiModelProperty(value = "存货周转天数")
    private Integer inventoryDay;

    @ApiModelProperty(value = "应收账款周转天数")
    private Integer accountsReceivableDay;

    @ApiModelProperty(value = "净利润")
    private BigDecimal nwProfit;

    @ApiModelProperty(value = "毛利率")
    private BigDecimal gwMargin;

    @ApiModelProperty(value = "净利率")
    private BigDecimal nwMargin;

    @ApiModelProperty(value = "税负率")
    private BigDecimal taxBearingRate;

    @ApiModelProperty(value = "银行借款")
    private BigDecimal bankLoan;

    @ApiModelProperty(value = "财务报告开始日期")
    private LocalDateTime reportBeginDate;

    @ApiModelProperty(value = "财务报告结束日期")
    private LocalDateTime reportEndDate;

    private Integer crtBy;

    private String crtByName;

    private LocalDateTime crtDtm;

    private Integer mdyBy;

    private String mdyByName;

    private LocalDateTime mdyDtm;

    private Integer voidedBy;

    private String voidedByName;

    private LocalDateTime voidedDtm;

    private Integer voided;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "存货")
    private BigDecimal inventory;

    @ApiModelProperty(value = "应收账款")
    private BigDecimal accountsReceivable;

    @ApiModelProperty(value = "应付账款")
    private BigDecimal accountsPayable;

    @ApiModelProperty(value = "固定资产")
    private BigDecimal fixedAssets;

    @ApiModelProperty(value = "营业利润")
    private BigDecimal operatingProfit;

    @ApiModelProperty(value = "流动比率")
    private BigDecimal flowRate;

    @ApiModelProperty(value = "速动比率")
    private BigDecimal quickRatio;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
