package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CounterCaseInfoVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "柜子清单信息表(counter_list_info id)")
    private Long bId;

    @ApiModelProperty(value = "清单名称(counter_list_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "箱号id(order_case id)")
    private Long caseId;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private String cartonNo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //**--箱子信息--**
    @ApiModelProperty(value = "FBA箱号")
    private String fabNo;

    @ApiModelProperty(value = "客户测量的重量，单位kg")
    private BigDecimal asnWeight;

    @ApiModelProperty(value = "预报长宽高计算得到的体积，单位m³")
    private BigDecimal asnVolume;

    @ApiModelProperty(value = "Amazon Reference ID(亚马逊引用ID)")
    private String amazonReferenceId;

    @ApiModelProperty(value = "扩展单号")
    private String extensionNumber;

}
