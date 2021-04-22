package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.OrderCopeWithVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PayBillForm {


    @ApiModelProperty(value = "订单id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "订单应付费用list", position = 5)
    @JSONField(ordinal = 5)
    @NotEmpty(message = "订单应付费用不能为空")
    private List<OrderCopeWithVO> orderCopeWithVOS;


}
