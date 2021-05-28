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

    @ApiModelProperty(value = "前端状态代码\n" +
            "    草稿:0\n" +
            "    补资料:9\n" +
            "    已下单:10\n" +
            "    已收货:20\n" +
            "    转运中:30\n" +
            "    已签收:40\n" +
            "    已完成:50\n" +
            "    已取消:-1")
    private String frontStatusCode;

    @ApiModelProperty(value = "后端状态代码\n" +
            "    草稿:0\n" +
            "    补资料:9\n" +
            "    已下单:10\n" +
            "        -- 内部小状态，这个不是流程状态\n" +
            "        -- 已审单\n" +
            "        -- 未审单\n" +
            "    已收货:20\n" +
            "    订单确认:30\n" +
            "    转运中:31\n" +
            "    已签收:40\n" +
            "    已完成:50\n" +
            "    已取消:-1")
    private String afterStatusCode;



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
