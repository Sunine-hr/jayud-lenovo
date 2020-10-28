package com.jayud.customs.model.bo;

import com.jayud.common.utils.excel.ColProperties;
import com.jayud.common.utils.excel.ExcelPageBase;
import lombok.Data;

@Data
public class HandbookBlacklistForm extends ExcelPageBase {
    @ColProperties(name = "订单编号")
    private String orderNo;

    @ColProperties(name = "分运单号")
    private String distributeNo;

    @ColProperties(name = "商品名称")
    private String name;

    @ColProperties(name = "替代名称")
    private String replacement;

    @ColProperties(name = "HS编码")
    private String hsCode;

    @ColProperties(name = "商品单价")
    private String price;

    @ColProperties(name = "购买数量")
    private String purchaseQty;

    @ColProperties(name = "件数")
    private String piece;

    @ColProperties(name = "单品总净重(千克)")
    private String netWeight;

    @ColProperties(name = "运抵国(地区)")
    private String destinationCountry;

    @ColProperties(name = "指运港(最终目的港)")
    private String destinationPort;

    @ColProperties(name = "收件人")
    private String consignee;

    @ColProperties(name = "总包号")
    private String packageNo;

    @ColProperties(name = "可能异常")
    private Boolean mayError;
}
