package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * larry 20210427
 * 清关对应箱号信息
 */
@Data
public class ClearanceInfoCaseForm extends BasePageForm{

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "提单对应清关信息id(bill_clearance_info id)")
    private Long bId;

    @ApiModelProperty(value = "提单对应清关信息(bill_clearance_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "箱号id(order_case id)")
    private Long caseId;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private String cartonNo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

}
