package com.jayud.tms.model.vo.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SupplierBill {
    @ApiModelProperty("子订单id")
    private Long subOrderId;

    @ApiModelProperty("客户名称 (供应商是子订单操作主体)")
    private String customer;

    @ApiModelProperty("所属月份")
    private String month;

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("账单金额")
    private String billAmount;

    @ApiModelProperty("账单金额(没有格式化)")
    private String billAmountStr;

    public void assemblyCost(Map<String, BigDecimal> costMap) {
        StringBuilder billAmount = new StringBuilder();
        StringBuilder billAmountStr = new StringBuilder();
        if (costMap == null) {
            return;
        }
        costMap.forEach((k, v) -> {
            billAmount.append(v).append(k).append("</br>");
            billAmountStr.append(v).append(k).append(",");
        });
        this.billAmount = billAmount.toString();
        this.billAmountStr = billAmountStr.toString();
    }
}
