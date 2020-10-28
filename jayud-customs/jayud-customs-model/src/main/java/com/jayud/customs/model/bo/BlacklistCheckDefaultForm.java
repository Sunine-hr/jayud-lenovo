package com.jayud.customs.model.bo;

import com.jayud.common.utils.excel.ColProperties;
import com.jayud.common.utils.excel.ExcelPageBase;
import lombok.Data;

/**
 * 佳裕达手册黑名单标准接收模板
 */
@Data
public class BlacklistCheckDefaultForm extends ExcelPageBase {
    public BlacklistCheckDefaultForm() {
        this.setStartRow(3);
    }

    @ColProperties(name = "订单编号", col = 1)
    private String orderNo;

    @ColProperties(name = "分运单号", col = 2)
    private String distributeNo;

    @ColProperties(name = "商品名称", col = 3)
    private String goodName;

    @ColProperties(name = "替代名称")
    private String replacement;

    @ColProperties(name = "HS编码", col = 4)
    private String hsCode;

    @ColProperties(name = "商品单价", col = 5)
    private String price;

    @ColProperties(name = "购买数量", col = 6)
    private String purchaseQty;

    @ColProperties(name = "件数", col = 7)
    private String piece;

    @ColProperties(name = "单品总净重(千克)", col = 8)
    private String netWeight;

    @ColProperties(name = "运抵国(地区)", col = 9)
    private String destinationCountry;

    @ColProperties(name = "指运港(最终目的港)", col = 10)
    private String destinationPort;

    @ColProperties(name = "收件人", col = 11)
    private String consignee;

    @ColProperties(name = "总包号", col = 12)
    private String packageNo;

    @ColProperties(name = "可能异常")
    private Boolean mayError;

}
