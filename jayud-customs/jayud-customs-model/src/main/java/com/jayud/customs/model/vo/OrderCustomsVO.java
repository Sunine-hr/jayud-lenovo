package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询子订单实体
 */
@Data
public class OrderCustomsVO {

    @ApiModelProperty(value = "子订单ID")
    private Long subOrderId;

    @ApiModelProperty(value = "通关口岸code")
    private String portCode;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址")
    private String cntrPic;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关子订单")
    private String orderNo;

    @ApiModelProperty(value = "报关抬头")
    private String title;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "附件临时属性")
    private String fileStr;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews;


}
