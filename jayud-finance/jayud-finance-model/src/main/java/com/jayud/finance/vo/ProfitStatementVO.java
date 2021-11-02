package com.jayud.finance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.bo.QueryProfitStatementForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 利润报表
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProfitStatement对象", description = "利润报表")
public class ProfitStatementVO extends Model<ProfitStatementVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "是否主订单")
    private Boolean isMain;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "法人名称")
    private String legalName;

    @ApiModelProperty(value = "法人id")
    private Long legalId;

    @ApiModelProperty(value = "操作部门")
    private String department;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "结算单位代码")
    private String unitCode;

    @ApiModelProperty(value = "结算单位名称")
    private String unitName;

    @ApiModelProperty(value = "业务员名称")
    private String bizUname;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "应收金额(不包含内部)")
    private String reAmount;

//    @ApiModelProperty(value = "应收金额(不包含内部)")
//    private List<String> reAmounts;

    @ApiModelProperty(value = "应收折合金额(RMB)(不包含内部)")
    private BigDecimal reEquivalentAmount;

    @ApiModelProperty(value = "应付金额(不包含内部)")
    private String payAmount;

//    @ApiModelProperty(value = "应付金额(不包含内部)")
//    private List<String> payAmounts;

    @ApiModelProperty(value = "应付折合金额(RMB)(不包含内部)")
    private BigDecimal payEquivalentAmount;

    @ApiModelProperty(value = "利润(不包含内部)")
    private BigDecimal profit;

    @ApiModelProperty(value = "应收金额(包含内部)")
    private String reInAmount;

//    @ApiModelProperty(value = "应收金额(包含内部)")
//    private List<String> reInAmounts;

    @ApiModelProperty(value = "应收折合金额(RMB)(内部)")
    private BigDecimal reInEquivalentAmount;

    @ApiModelProperty(value = "应付金额(内部)")
    private String payInAmount;

//    @ApiModelProperty(value = "应付金额(内部)")
//    private List<String> payInAmounts;

    @ApiModelProperty(value = "应付折合金额(RMB)(内部)")
    private BigDecimal payInEquivalentAmount;

    @ApiModelProperty(value = "利润(内部)")
    private BigDecimal inProfit;

//    @ApiModelProperty(value = "同步时间")
//    private LocalDateTime syncTime;

    @ApiModelProperty(value = "应收费用id集合(多个逗号隔开)")
    private String reCostIds;

    @ApiModelProperty(value = "应付费用id集合(多个逗号隔开)")
    private String payCostIds;

    @ApiModelProperty(value = "应收费用id集合(多个逗号隔开)(内部)")
    private String reInCostIds;

    @ApiModelProperty(value = "应付费用id集合(多个逗号隔开)(内部)")
    private String payInCostIds;

    @ApiModelProperty(value = "应付子订单金额(内部结算统计子订单金额)")
    private String paySubAmount = "";

    @ApiModelProperty(value = "应付子订单折合金额(RMB)(内部结算统计子订单金额)")
    private BigDecimal paySubEquivalentAmount = new BigDecimal(0);

    @ApiModelProperty(value = "应付子订单费用id集合(内部结算统计子订单金额)")
    private String paySubCostIds = "";

    @ApiModelProperty(value = "利润(内部结算统计子订单金额)")
    private BigDecimal subProfit = new BigDecimal(0);

    @ApiModelProperty(value = "是否打开内部往来费用")
    private Boolean isOpenInternal = false;

    @ApiModelProperty(value = "主订单id")
    private Long mainOrderId;

    @ApiModelProperty(value = "订单类别(product_classify表)")
    private String classCode;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime operationTime;

    public static void main(String[] args) {
        Utilities.printFieldsInfo(QueryProfitStatementForm.class);
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void totalInternalExpenses(Boolean isOpenInternal) {
        if (this.isMain) {
            //所属部门
            String payCostIds = this.payCostIds;
            String payAmount = this.payAmount;
            BigDecimal payEquivalentAmount = this.payEquivalentAmount;
            BigDecimal profit = this.profit;
            //不包内部结算(内部结算不是录用费用那个内部往里)
            this.reAmount = StringUtils.add("", this.reAmount, this.reInAmount);
            this.payAmount = StringUtils.add("", this.payAmount, this.payInAmount);
            this.reEquivalentAmount = BigDecimalUtil.add(this.reEquivalentAmount, this.reInEquivalentAmount);
            this.payEquivalentAmount = BigDecimalUtil.add(this.payEquivalentAmount, this.payInEquivalentAmount);
            this.reCostIds = StringUtils.add("", this.reCostIds, this.reInCostIds);
            this.payCostIds = StringUtils.add("", this.payCostIds, this.payInCostIds);
            this.profit = BigDecimalUtil.add(this.profit, this.inProfit);
            //内部结算(内部结算不是录用费用那个内部往里)
            this.payInAmount = StringUtils.add("", payAmount, this.paySubAmount);
            this.payInEquivalentAmount = BigDecimalUtil.add(payEquivalentAmount, this.paySubEquivalentAmount);
            this.payInCostIds = StringUtils.add("", payCostIds, this.paySubCostIds);
            this.inProfit = BigDecimalUtil.subtract(profit, this.subProfit);

            this.reInAmount =this.reAmount;
            this.reInEquivalentAmount = this.reEquivalentAmount;
            this.reInCostIds =this.reCostIds;

        } else {
            //操作部门
            this.reAmount = StringUtils.add("", this.reAmount, this.reInAmount);
            this.payAmount = StringUtils.add("", this.payAmount, this.payInAmount);
            this.reEquivalentAmount = BigDecimalUtil.add(this.reEquivalentAmount, this.reInEquivalentAmount);
            this.payEquivalentAmount = BigDecimalUtil.add(this.payEquivalentAmount, this.payInEquivalentAmount);
            this.reCostIds = StringUtils.add("", this.reCostIds, this.reInCostIds);
            this.payCostIds = StringUtils.add("", this.payCostIds, this.payInCostIds);
            this.profit = BigDecimalUtil.add(this.profit, this.inProfit);
        }

        //        if (isOpenInternal) {
//            this.reAmount += this.reInAmount;
//            this.payAmount += this.payInAmount;
//            this.reEquivalentAmount = BigDecimalUtil.add(this.reEquivalentAmount, this.reInEquivalentAmount);
//            this.payEquivalentAmount = BigDecimalUtil.add(this.payEquivalentAmount, this.payInEquivalentAmount);

//        }
    }
}
