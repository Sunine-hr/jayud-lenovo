package com.jayud.finance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
    private LocalDateTime createTime;

    @ApiModelProperty(value = "应收金额(不包含内部)")
    private String reAmount;

    @ApiModelProperty(value = "应收折合金额(RMB)(不包含内部)")
    private BigDecimal reEquivalentAmount;

    @ApiModelProperty(value = "应付金额(不包含内部)")
    private String payAmount;

    @ApiModelProperty(value = "应付折合金额(RMB)(不包含内部)")
    private BigDecimal payEquivalentAmount;

    @ApiModelProperty(value = "利润(不包含内部)")
    private BigDecimal profit;

    @ApiModelProperty(value = "应收金额(包含内部)")
    private String reInAmount;

    @ApiModelProperty(value = "应收折合金额(RMB)(内部)")
    private BigDecimal reInEquivalentAmount;

    @ApiModelProperty(value = "应付金额(内部)")
    private String payInAmount;

    @ApiModelProperty(value = "应付折合金额(RMB)(内部)")
    private BigDecimal payInEquivalentAmount;

    @ApiModelProperty(value = "利润(内部)")
    private BigDecimal inProfit;

    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "应收费用id集合(多个逗号隔开)")
    private String reCostIds;

    @ApiModelProperty(value = "应付费用id集合(多个逗号隔开)")
    private String payCostIds;

    @ApiModelProperty(value = "应收费用id集合(多个逗号隔开)(内部)")
    private String reInCostIds;

    @ApiModelProperty(value = "应付费用id集合(多个逗号隔开)(内部)")
    private String payInCostIds;

    public static void main(String[] args) {
        Utilities.printFieldsInfo(QueryProfitStatementForm.class);
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void totalInternalExpenses(Boolean isOpenInternal) {
        if (isOpenInternal) {
            this.reAmount += this.reInAmount;
            this.payAmount += this.payInAmount;
            this.reEquivalentAmount = BigDecimalUtil.add(this.reEquivalentAmount, this.reInEquivalentAmount);
            this.payEquivalentAmount = BigDecimalUtil.add(this.payEquivalentAmount, this.payInEquivalentAmount);
        }
    }
}
