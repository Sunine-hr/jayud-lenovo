package com.jayud.finance.po;

import com.jayud.finance.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * k3 财务汇集-总账-费用 实体
 * @author hugo
 */


@Data
@K3Entity(value = {"FEXPID","FName","FNumber","FDescription","FCurrencyID.FNumber","FDisType","FBCalTax","FRATE"},formId = FormIDEnum.EXPENSE)
public class Expense implements Serializable{

    private static final long serialVersionUID = 6576323011180112728L;
    /**
     * 实体主键
     */
    private Long FEXPID;

    /**
     * 名称
     */
    private String FName;

    /**
     * 编码
     */
    private String FNumber;

    /**
     * 描述
     */
    private String FDescription;

    /**
     * 适用币种
     */
    private String FCurrencyID_FNumber;

    /**
     * 费用分配依据
     */
    private String FDisType;

    /**
     * 计税
     */
    private Boolean FBCalTax;

    /**
     * 税率
     */
    private BigDecimal FRATE;

}
