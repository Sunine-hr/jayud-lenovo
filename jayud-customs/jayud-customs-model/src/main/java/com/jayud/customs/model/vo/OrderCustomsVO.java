package com.jayud.customs.model.vo;

import com.jayud.common.utils.FileView;
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

    @ApiModelProperty(value = "单双抬头")
    private String isTitle;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "附件临时属性")
    private String fileStr;

    @ApiModelProperty(value = "六联单号附件")
    private String encodePic;

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递)")
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件")
    private String airTransportPic;

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件")
    private String seaTransportPic;

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews;


}
