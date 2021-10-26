package com.jayud.tms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运输公司信息
 */
@Data
public class ScmTransportationInformationForm {

    @ApiModelProperty(value = "订单号")
    private String truckNo;

    @ApiModelProperty(value = "运输公司")
    private String truckCompany;

    @ApiModelProperty(value = "大陆车牌")
    private String cnTruckNo;

    @ApiModelProperty(value = "香港车牌")
    private String hkTruckNo;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverTel;

    @ApiModelProperty(value = "操作人")
    private String userName;

}
