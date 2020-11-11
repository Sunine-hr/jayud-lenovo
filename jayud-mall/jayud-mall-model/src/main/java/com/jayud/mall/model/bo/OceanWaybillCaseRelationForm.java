package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "运单对应箱号关联表", description = "1运单对应N箱号")
public class OceanWaybillCaseRelationForm {

    @ApiModelProperty(value = "主键Id，自增")
    private Long id;

    @ApiModelProperty(value = "运单id(ocean_waybill id)")
    private Long oceanWaybillId;

    @ApiModelProperty(value = "订单对应箱号信息id(order_case id)")
    private Long orderCaseId;

    @ApiModelProperty(value = "客户id(customer id)")
    private Long customerId;

}
