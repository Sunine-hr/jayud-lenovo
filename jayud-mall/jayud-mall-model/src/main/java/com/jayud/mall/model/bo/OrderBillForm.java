package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.OrderCopeReceivableVO;
import com.jayud.mall.model.vo.OrderCopeWithVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderBillForm {

    @ApiModelProperty(value = "订单id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "订单应收费用", position = 4)
    @JSONField(ordinal = 4)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOS;

    @ApiModelProperty(value = "订单应付费用", position = 5)
    @JSONField(ordinal = 5)
    private List<OrderCopeWithVO> orderCopeWithVOS;



}
