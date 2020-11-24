package com.jayud.finance.vo;

import lombok.Data;

/**
 * 账单列表预览表头字段
 */
@Data
public class ViewBilToOrderHeadVO {

    private String createdTimeStr = "创建日期";

    private String orderNo = "订单编号";

    private String subOrderNo = "子订单编号";

    private String unitAccount = "结算单位";

    private String startAddress = "起运地";

    private String endAddress = "目的地";

    private String licensePlate = "车牌号";

    private String vehicleSize = "车型";

    private String pieceNum = "件数";

    private String weight = "重量";

    private String yunCustomsNo = "报关单号";

}
