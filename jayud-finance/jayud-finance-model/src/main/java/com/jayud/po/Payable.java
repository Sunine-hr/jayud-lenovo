package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于通过报关单号（业务单号）查询金蝶的应付单号
 *
 * @author william
 * @description
 * @Date: 2020-05-28 16:34
 */
@Data
@K3Entity(value = {"FBillNo"}, formId = FormIDEnum.PAYABLE, fName = "F_JYD_Text")
public class Payable implements InvoiceBase, Serializable {
    private static final long serialVersionUID = 7438064471906073583L;
    String FBillNo;
}
