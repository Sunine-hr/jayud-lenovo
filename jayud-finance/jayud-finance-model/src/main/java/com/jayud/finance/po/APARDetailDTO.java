package com.jayud.finance.po;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 应收，应付公用此明细表单
 *
 * @author william
 * @description
 * @Date: 2020-04-28 17:00
 */
@Data
public class APARDetailDTO {
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 账单号
     */
    private String billNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 费用名称
     */
    private String expenseName;

    /**
     * 费用类别
     */
    private String expenseCategoryName;

    /**
     * 费用类型
     */
    private String expenseTypeName;

    /**
     * 币别
     */
    private String currency;

    /**
     * 计价数量
     */
    private BigDecimal priceQty;

    /**
     * 含税单价
     */
    private BigDecimal taxPrice;

    /**
     * FEntryTaxRate 税率
     */
    private BigDecimal taxRate;

    /**
     * 不含税单价
     */
    private BigDecimal noTaxPrice;

    /**
     * 总税额
     */
    private BigDecimal taxTotal;

    /**
     * 含税总价
     */
    private BigDecimal taxAmount;


}
