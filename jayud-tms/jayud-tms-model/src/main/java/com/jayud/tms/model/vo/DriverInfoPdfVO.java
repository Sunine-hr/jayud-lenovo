package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 司机资料PDF
 */
@Data
public class DriverInfoPdfVO {
    //法人主体信息
    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "tel")
    private String legalPhone;

    @ApiModelProperty(value = "fax")
    private String fax;

    //司机信息
    @ApiModelProperty(value = "司机名称")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "香港手提")
    private String hkPhone;

    @ApiModelProperty(value = "国内手提")
    private String phone;

    //货车信息
    @ApiModelProperty(value = "运输牌头")
    private String ptCompany;

    @ApiModelProperty(value = "海关编号")
    private String customsCode;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "骑师,暂时无值")
    private String qs;

    @ApiModelProperty("车辆吨位")
    private String vehicleTonnage;

    @ApiModelProperty(value = "牌头电话")
    private String ptPhone;

    @ApiModelProperty(value = "牌头传真")
    private String ptFax;

    @ApiModelProperty(value = "寮步密码")
    private String steppingCode;

    @ApiModelProperty(value = "载货清单,暂无值")
    private String loadList;

    @ApiModelProperty(value = "企业代码")
    private String enterpriseCode;

    @ApiModelProperty("吉车重量")
    private String weight;

    //订单信息
    @ApiModelProperty(value = "口岸")
    private String portCodeDesc;

}
