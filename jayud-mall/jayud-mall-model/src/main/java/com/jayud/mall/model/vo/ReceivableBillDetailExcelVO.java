package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceivableBillDetailExcelVO {

    // 序号
    @ApiModelProperty(value = "序号")
    private String sequenceNumber;
    // 日期
    @ApiModelProperty(value = "日期")
    private String createTime;
    // 运单号
    @ApiModelProperty(value = "运单号")
    private String orderNo;
    // 客户运单号

    // 转单号

    // FBA号

    // 邮编

    // 服务类型
    private String serviceType;

    // 发往国家
    @ApiModelProperty(value = "发往国家")
    private String countryName;
    // 件数
    @ApiModelProperty(value = "件数")
    private String totalCartons;
    // 收费重
    @ApiModelProperty(value = "收费重")
    private String count;
    // 单价
    @ApiModelProperty(value = "单价")
    private String unitPrice;
    // 金额
    @ApiModelProperty(value = "金额")
    private String amount;
    // 费用类型
    @ApiModelProperty(value = "费用类型")
    private String costName;
    // 描述
    @ApiModelProperty(value = "描述")
    private String remarks;


}
