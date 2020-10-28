package com.jayud.customs.model.bo;

import com.jayud.common.utils.excel.ColProperties;
import com.jayud.common.utils.excel.ExcelPageBase;
import lombok.Data;

/**
 * 先达香港-客户模板抓取定义
 */
@Data
public class BlacklistCheckXiandaForm extends ExcelPageBase {
    public BlacklistCheckXiandaForm(Integer startRow) {
        this.setStartRow(startRow);
    }

    public BlacklistCheckXiandaForm() {
        this.setStartRow(3);
    }

    @ColProperties(name = "快递单号", col = 4)
    private String trackingNo;

    @ColProperties(name = "商品重量", col = 7)
    private String weightPerPiece;

    @ColProperties(name = "商品名称", col = 9)
    private String goodName;

    @ColProperties(name = "商品备注", col = 8)
    private String content;

    @ColProperties(name = "商品数量", col = 10)
    private String pcs;

    @ColProperties(name = "收货人", col = 16)
    private String consignee;

    @ColProperties(name = "收货地址", col = 17)
    private String consigneeAddress;

    @ColProperties(name = "收货人联系方式", col = 18)
    private String consigneeTel;
}
