package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceivableBillDetailExcelVO {

    // 序号
    @ApiModelProperty(value = "序号", notes = "自增+1")
    private String sequenceNumber;
    // 日期
    @ApiModelProperty(value = "日期", notes = "日期 order_info.create_time")
    private String createTime;
    // 运单号
    @ApiModelProperty(value = "运单号", notes = "运单号 order_info.order_no")
    private String orderNo;
    // 客户运单号
    @ApiModelProperty(value = "客户运单号", notes = "客户运单号-》进仓编号  order_info.warehouse_no  / order_info.is_pick 1是  -> order_pick.warehouse_no")
    private String warehouseNo;
    // 转单号
    @ApiModelProperty(value = "转单号", notes = "转单号 null")
    private String trackingNumber;
    // FBA号
    @ApiModelProperty(value = "FBA号", notes = "FBA号 order_info  -> order_case.fab_no")
    private String fabNo;
    // 邮编
    @ApiModelProperty(value = "邮编", notes = "邮编  order_info.destination_warehouse_code -> fab_warehouse.zip_code")
    private String zipCode;

    // 服务类型
    @ApiModelProperty(value = "服务类型", notes = "服务类型 order_info.offer_info_id -> offer_info.id -> quotation_template.id -> service_group.id -> service_group.code_name")
    private String serviceType;

    // 发往国家
    @ApiModelProperty(value = "发往国家", notes = "发往国家 order_info.destination_warehouse_code -> fab_warehouse.country_name")
    private String countryName;
    // 件数
    @ApiModelProperty(value = "件数", notes = "件数 order_info.totalCase")
    private String totalCartons;
    // 收费重
    @ApiModelProperty(value = "收费重", notes = "收费重 receivable_bill_detail.order_receivable_id -> order_cope_receivable.count")
    private String count;
    // 单价
    @ApiModelProperty(value = "单价", notes = "单价 receivable_bill_detail.order_receivable_id -> order_cope_receivable.unit_price")
    private String unitPrice;
    // 金额
    @ApiModelProperty(value = "金额", notes = "金额    receivable_bill_detail.amount")
    private String amount;
    // 费用类型
    @ApiModelProperty(value = "费用类型", notes = "费用类型 receivable_bill_detail.cost_name")
    private String costName;
    // 描述
    @ApiModelProperty(value = "描述", notes = "描述    receivable_bill_detail.remarks")
    private String remarks;

    @ApiModelProperty(value = "币种名称")
    private String cname;

    // ... 扩展的字段 ...
    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "币种id")
    private String cid;

    @ApiModelProperty(value = "填充格式")
    private String fillFormat;


}
