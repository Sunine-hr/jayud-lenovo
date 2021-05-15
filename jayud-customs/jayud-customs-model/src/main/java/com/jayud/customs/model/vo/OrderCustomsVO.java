package com.jayud.customs.model.vo;

import com.jayud.common.enums.OrderStatusEnum;
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

    @ApiModelProperty(value = "六联单号附件名称")
    private String cntrPicName;

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

    @ApiModelProperty(value = "附件临时属性")
    private String fileNameStr;

    @ApiModelProperty(value = "六联单号附件")
    private String encodePic;

    @ApiModelProperty(value = "六联单号附件名称")
    private String encodePicName;

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递)")
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件")
    private String airTransportPic;

    @ApiModelProperty(value = "提运单附件")
    private String airTransPicName;

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件")
    private String seaTransportPic;

    @ApiModelProperty(value = "提运单号附件名称")
    private String seaTransPicName;

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "状态描述CODE")
    private String status;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews;

    @ApiModelProperty(value = "委托单号")
    private String entrustNo;

    @ApiModelProperty(value = "监管方式")
    private String supervisionMode;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    public void setStatusDesc(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
//        if (OrderStatusEnum.CUSTOMS_C_0.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_0.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_1.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_1.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_2.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_2.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_3.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_3.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_4.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_4.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_5.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_5.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_5_1.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_5_1.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_6.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_6.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_6_1.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_6_1.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_6_2.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_6_2.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_7.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_7.getDesc();
//        } else if (OrderStatusEnum.CUSTOMS_C_8.getCode().equals(status)) {
//            this.statusDesc = OrderStatusEnum.CUSTOMS_C_8.getDesc();
//        }


    }
}
