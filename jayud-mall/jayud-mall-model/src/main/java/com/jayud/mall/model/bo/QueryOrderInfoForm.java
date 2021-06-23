package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryOrderInfoForm extends BasePageForm {

    /*产品订单表:order_info*/
    @ApiModelProperty(value = "客户ID(customer id)")
    private Integer customerId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "目的仓库代码(fab_warehouse warehouse_code)")
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

    @ApiModelProperty(value = "是否审核单据 1已审核 2未审核", notes = "OrderEnum.IS_AUDIT_ORDER")//是否审核单据(1已审单 2未审单)
    private String isAuditOrderStatusFlag;

    @ApiModelProperty(value = "开船日期(offer_info.sail_time)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "服务名称 (quotation_template.sid -> service_group.id)")
    private Integer sid;

    @ApiModelProperty(value = "运输方式 (quotation_template.tid -> transport_way.id)")
    private Integer tid;

    @ApiModelProperty(value = "起运港 (quotation_template.start_shipment -> harbour_info.id_code)")
    private String startShipment;

    @ApiModelProperty(value = "目的港 (quotation_template.destination_port -> harbour_info.id_code)")
    private String destinationPort;


}
