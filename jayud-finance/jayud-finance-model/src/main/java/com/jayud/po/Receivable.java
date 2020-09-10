package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 应收单数据单号查询使用
 *
 * @author william
 * @description
 * @Date: 2020-05-27 10:50
 */
@Data
@K3Entity(value = {"FBillNo"}, formId = FormIDEnum.RECEIVABLE, fName = "FAR_Remark")
public class Receivable implements InvoiceBase, Serializable {
    private static final long serialVersionUID = 5633024057951945878L;
    String FBillNo;
}
