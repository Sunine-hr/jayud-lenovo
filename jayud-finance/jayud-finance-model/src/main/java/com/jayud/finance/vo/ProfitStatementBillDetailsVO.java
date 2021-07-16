package com.jayud.finance.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.finance.enums.BillEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 利润报表账单
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfitStatementBillDetailsVO extends Model<ProfitStatementBillDetailsVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账单号")
    private String billNo;

    @ApiModelProperty(value = "核算期")
    private String accountTerm;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal money;

    @ApiModelProperty(value = "账单状态")
    private String auditStatus;

    @ApiModelProperty(value = "应收折合金额")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "短款金额")
    private BigDecimal shortAmount;

    @ApiModelProperty(value = "核销状态")
    private String heXiaoStatus;


    public void setMoney(BigDecimal money) {
        this.money = money;
        heXiaoStatus = this.money.compareTo(BigDecimalUtil.add(this.discountMoney, this.shortAmount)) == 0 ? "已核销" : "未核销";
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = BillEnum.getDesc(auditStatus);
    }




}