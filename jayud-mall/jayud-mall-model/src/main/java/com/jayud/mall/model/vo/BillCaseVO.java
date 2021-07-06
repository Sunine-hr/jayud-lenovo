package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "BillCaseVO", description = "提单箱子")
@Data
public class BillCaseVO {

    @ApiModelProperty(value = "箱子id(order_case id)")
    private Long caseId;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = " FBA箱号")
    private String fabNo;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户(公司)名称")
    private String customerName;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "中文名")
    private String nameCn;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "报关编码(customs_data id_code)")
    private String dataCode;

    @ApiModelProperty(value = "材质")
    private String materialQuality;

    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "订单报关文件列表list")
    private List<OrderCustomsFileVO> orderCustomsFileList;

    @ApiModelProperty(value = "订单清关文件列表list")
    private List<OrderClearanceFileVO> orderClearanceFileList;


}
