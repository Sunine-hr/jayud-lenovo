package com.jayud.finance.vo;

import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderBillCostTotal;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 账单数列表(应付应收一致)
 */
@Data
public class OrderPaymentBillNumVO {

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "核算期 年月")
    private String accountTermStr;

    @ApiModelProperty(value = "账单编号维度下的订单数")
    private Integer billNum;

    @ApiModelProperty(value = "人民币")
    private BigDecimal rmb;

    @ApiModelProperty(value = "美元")
    private BigDecimal dollar;

    @ApiModelProperty(value = "欧元")
    private BigDecimal euro;

    @ApiModelProperty(value = "港币")
    private BigDecimal hKDollar;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "已收金额，即财务已核销金额")
    private BigDecimal heXiaoAmount;

    @ApiModelProperty(value = "账单状态")
    private String billStatus;

    @ApiModelProperty(value = "账单状态")
    private String billStatusDesc;

    @ApiModelProperty(value = "制单人")
    private String makeUser;

    @ApiModelProperty(value = "制单时间")
    private String makeTimeStr;

    @ApiModelProperty(value = "结算汇率")
    private String settlementRate;

    @ApiModelProperty(value = "账单金额(结算金额)")
    private String amountStr;


    public String getBillStatusDesc() {
        if (!StringUtil.isNullOrEmpty(this.billStatus)) {
            return BillEnum.getDesc(this.billStatus);
        }
        return "";
    }


    public void assembleSettlementRate(List<OrderBillCostTotal> costTotals,
                                       Map<String, String> currencyMap) {

        Iterator<OrderBillCostTotal> iterator = costTotals.iterator();
        StringBuilder sb = new StringBuilder();
        BigDecimal amount = new BigDecimal(0);
        String currencyName = "";
        while (iterator.hasNext()) {
            OrderBillCostTotal next = iterator.next();
            if (this.billNo.equals(next.getBillNo())) {
                String currentCurrency = currencyMap.get(next.getCurrentCurrencyCode());
                String outOfCurrency = currencyMap.get(next.getCurrencyCode());
                sb.append(currentCurrency == null ? "" : currentCurrency)
                        .append("-")
                        .append(outOfCurrency == null ? "" : outOfCurrency)
                        .append(" 汇率").append(next.getExchangeRate())
                        .append("<br/>");
                amount = amount.add(next.getMoney());
                currencyName = currencyMap.get(next.getCurrencyCode());
                iterator.remove();
            }
        }
        this.settlementRate = sb.toString();
        this.amountStr = amount.toString() + " " + currencyName;
    }

}
