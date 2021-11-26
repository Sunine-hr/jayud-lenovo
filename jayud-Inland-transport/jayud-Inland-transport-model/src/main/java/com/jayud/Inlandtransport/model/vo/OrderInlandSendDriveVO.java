package com.jayud.Inlandtransport.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送派车信息到客户
 */
@Data
public class OrderInlandSendDriveVO {

    @ApiModelProperty(value = "第三方单号third_party_order_no")
    private String orderNo;

    @ApiModelProperty(value = "司机证件号码")
    private String idCode;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "车牌")
    private String truckNo;

    @ApiModelProperty(value = "司机电话")
    private String driverTel;

    @ApiModelProperty(value = "配送人司机名称")
    private String deliverName;

    @ApiModelProperty(value = "时间")
    private String deliverTime;


}
