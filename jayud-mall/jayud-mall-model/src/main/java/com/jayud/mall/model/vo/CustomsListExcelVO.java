package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomsListExcelVO {

    @ApiModelProperty(value = "bill_customs_info id")
    private Long billCustomsInfoId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    //日期
    @ApiModelProperty(value = "日期")
    private String createTime;

    //Shipper address
    @ApiModelProperty(value = "Shipper address")
    private String shipperAddress;

    //Consignee address
    @ApiModelProperty(value = "Consignee address")
    private String consigneeAddress;

    @ApiModelProperty(value = "总箱数")
    private String packages;

    @ApiModelProperty(value = "总数量")
    private String qty;

    @ApiModelProperty(value = "总价")
    private String totalPrice;

    @ApiModelProperty(value = "总净重")
    private String jz;

    @ApiModelProperty(value = "总毛重")
    private String grossWeight;

    @ApiModelProperty(value = "总立方")
    private String cbm;

    @ApiModelProperty(value = "报关商品Excel list")
    private List<CustomsGoodsExcelVO> customsGoodsExcelList;


}
