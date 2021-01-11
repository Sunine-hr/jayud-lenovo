package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InputOrderServiceVO {
    @ApiModelProperty(value = "服务单订单ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "服务单订单号,修改时传")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "服务类型(0:费用补录,1:单证费用,2:存仓费用,快递费用)")
    private Integer type;

    @ApiModelProperty(value = "服务类型(0:费用补录,1:单证费用,2:存仓费用,快递费用)")
    private String typeDesc;

    @ApiModelProperty(value = "关联订单")
    private String associatedOrder;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderTakeAdrVO> takeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<InputOrderTakeAdrVO> takeAdrForms2 = new ArrayList<>();
}
