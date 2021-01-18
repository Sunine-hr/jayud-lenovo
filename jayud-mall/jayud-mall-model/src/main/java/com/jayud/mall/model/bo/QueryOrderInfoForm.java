package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryOrderInfoForm extends BasePageForm {

    /*产品订单表:order_info*/
    @ApiModelProperty(value = "客户ID(customer id)", position = 1)
    @JSONField(ordinal = 1)
    private Integer customerId;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "目的仓库代码(fab_warehouse warehouse_code)", position = 3)
    @JSONField(ordinal = 3)
    private String destinationWarehouseCode;

    @ApiModelProperty(value = "状态码" +
            "n枚举: -1,0,10,20,30,40,50" +
            "枚举备注: " +
            "-1 已取消 查看详情 " +
            "0 草稿-----提交、取消、查看订单详情（后台不记录数据） " +
            "10 已下单：编辑、查看订单详情 " +
            "20 已收货：编辑、查看订单详情 " +
            "30 订单确认：确认计柜重（不可修改订单信息） " +
            "40 转运中：查看订单详情 " +
            "50 已签收：账单确认、查看订单详情", position = 4
    )
    @JSONField(ordinal = 4)
    private Integer status;

    /*订单关联运价(报价offer_info，报价模板quotation_template)*/
    @ApiModelProperty(value = "起运港(harbour_info id_code)", position = 5)
    @JSONField(ordinal = 5)
    private String startShipment;

    @ApiModelProperty(value = "目的港(harbour_info id_code)", position = 6)
    @JSONField(ordinal = 6)
    private String destinationPort;

    @ApiModelProperty(value = "开船日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

}
