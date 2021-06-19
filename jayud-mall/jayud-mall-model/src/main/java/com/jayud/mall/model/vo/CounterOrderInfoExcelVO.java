package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CounterOrderInfoExcelVO {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    //序号
    @ApiModelProperty(value = "序号")
    private String serialNumber;
    //渠道-->指服务名称
    @ApiModelProperty(value = "渠道-->指服务名称")
    private String serviceName;
    //订单号
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    //进仓编号
    @ApiModelProperty(value = "进仓编号，多个")
    private String warehouseNo;

    //Amazon Reference ID(亚马逊引用ID)
    @ApiModelProperty(value = "亚马逊引用ID")
    private String amazonReferenceId;
    //FBA号
    @ApiModelProperty(value = "FBA号，多个")
    private String extensionNumber;
    //箱数
    @ApiModelProperty(value = "箱数")
    private String cartons;
    //重量
    @ApiModelProperty(value = "重量")
    private String weight;
    //体积
    @ApiModelProperty(value = "体积")
    private String volume;
    //仓库代码-->目的仓库代码
    @ApiModelProperty(value = "仓库代码-->目的仓库代码")
    private String destinationWarehouseCode;

    //**--下面的数据都没有--**
    //派送方式
    @ApiModelProperty(value = "派送方式")
    private String deliveryWay;
    //胶带颜色
    @ApiModelProperty(value = "胶带颜色")
    private String tapeColor;
    //制标
    @ApiModelProperty(value = "制标")
    private String systemStandard;
    //回传
    @ApiModelProperty(value = "回传")
    private String passBack;
    //装柜顺序
    @ApiModelProperty(value = "装柜顺序")
    private String loadingSequence;
    //操作备注
    @ApiModelProperty(value = "操作备注")
    private String operationNote;
    //仓库备注
    @ApiModelProperty(value = "仓库备注")
    private String warehouseNote;
}