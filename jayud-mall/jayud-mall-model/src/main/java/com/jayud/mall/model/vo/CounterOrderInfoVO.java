package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CounterOrderInfoVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "柜子清单信息表(counter_list_info id)")
    @JsonProperty(value = "bId")
    private Long bId;

    @ApiModelProperty(value = "清单名称(counter_list_info file_name)")
    @JsonProperty(value = "bName")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单类型(1普通运单 2留仓运单)")
    private Integer orderType;


    //destination_warehouse_name,t1.need_declare,t1.is_pick,

    @ApiModelProperty(value = "目的仓库代码(fab_warehouse warehouse_code)',")
    private String destinationWarehouseCode;

    @ApiModelProperty(value = "是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file) 0 否 对应 买关 1 是 对应 独立")
    private Integer needDeclare;

    @ApiModelProperty(value = "是否上门提货(0否 1是,order_pick)")
    private Integer isPick;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "服务名")
    private String serviceName;


    //需要汇总计算
    @ApiModelProperty(value = "进仓单号，多个")
    private String warehouseNo;

    @ApiModelProperty(value = "扩展单号,即FBA箱号，多个，去重")
    private String extensionNumber;

    @ApiModelProperty(value = "已配箱数")
    private Integer hasboxNumber;

    @ApiModelProperty(value = "未配箱数")
    private Integer notboxNumber;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;


}
