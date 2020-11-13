package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "运单")
public class OceanWaybillForm {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "描述")
    private String describe;

    @ApiModelProperty(value = "送货信息")
    private String shippingInformation;

    @ApiModelProperty(value = "货柜信息id(ocean_counter id)")
    private Integer oceanCounterId;

    //1运单对应N箱号
//    @ApiModelProperty(value = "1运单对应N箱号")
//    private List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList;

    @ApiModelProperty(value = "1个柜子对应N个运单")
    private List<OceanWaybillForm> oceanWaybillFormList;

}
