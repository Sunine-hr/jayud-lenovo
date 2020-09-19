package com.jayud.finance.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 应收，应付公用此明细表单
 * @author william
 * @description
 * @Date: 2020-04-28 17:00
 */
@Data
public class APARDetailForm {
    /**
     * FMATERIALID 物料编码
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    private String materialIdPacked;

    /**
     * FMaterialDesc 物料名称
     */
    @NotEmpty(message = "费用项目名称不允许为空")
    private String expenseName;

    /**
     * F_JYD_Assistant 费用类别代码
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "费用类别名称不允许为空")
    private String expenseCategoryName;

    /**
     * F_JYD_Assistant1 费用类型代码（区分是否收税的关键字）
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "费用类型名称不允许为空")
    private String expenseTypeName;

    /**
     * FPriceQty 计价数量
     */
    @NotNull(message = "计费数量不允许为空")
    private BigDecimal priceQty;

    /**
     * FTaxPrice 含税单价
     */
    @NotNull(message = "含税单价不允许为空")
    private BigDecimal taxPrice;

    /**
     * FEntryTaxRate 税率
     */
    @NotNull(message = "税率不允许为空")
    private BigDecimal taxRate;

    //todo 推送时要确认是否要以不含税价格进行计算
}