package com.jayud.customs.model.bo;

import com.jayud.common.utils.excel.ColNameCN;
import com.jayud.common.utils.excel.ExcelReaderParent;
import lombok.Data;

@Data
public class HandbookBlacklistForm extends ExcelReaderParent {
    @ColNameCN(value = "订单编号")
    private String orderNo;

    @ColNameCN(value = "分运单号")
    private String distributeNo;

    @ColNameCN(value = "商品名称")
    private String name;

    @ColNameCN(value = "替代名称")
    private String replacement;

    @ColNameCN(value = "HS编码")
    private String hsCode;

    @ColNameCN(value = "商品单价")
    private String price;

    @ColNameCN(value = "购买数量")
    private String purchaseQty;

    @ColNameCN(value = "件数")
    private String piece;

    @ColNameCN(value = "单品总净重(千克)")
    private String netWeight;

    @ColNameCN(value = "运抵国(地区)")
    private String destinationCountry;

    @ColNameCN(value = "指运港(最终目的港)")
    private String destinationPort;

    @ColNameCN(value = "收件人")
    private String consignee;

    @ColNameCN(value = "总包号")
    private String packageNo;

    @ColNameCN(value = "可能异常")
    private Boolean mayError;
}
