package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomsGoodsExcelVO {

    @ApiModelProperty(value = "客户商品id")
    private Integer goodId;

    @ApiModelProperty(value = "箱子id")
    private Long caseId;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = "箱子里面商品的数量")
    private Integer quantity;

    @ApiModelProperty(value = "报关文件名")
    private String billCustomsInfoName;

    @ApiModelProperty(value = "IDcode，报关ID", notes = "商品报关id，统计这里商品")
    private String idCode;

    //****//

    @ApiModelProperty(value = "序号")
    private String serialNumber;

    //中文品名
    @ApiModelProperty(value = "中文品名", notes = "customs_data ch_name")
    private String chName;

    //英文品名
    @ApiModelProperty(value = "英文品名", notes = "customs_data en_name")
    private String enName;

    //海关编码
    @ApiModelProperty(value = "海关编码", notes = "customs_data hs_code")
    private String hsCode;

    //材质
    @ApiModelProperty(value = "customs_data texture")
    private String texture;

    //用途
    @ApiModelProperty(value = "用途", notes = "customs_data uses")
    private String uses;

    //单价
    @ApiModelProperty(value = "单价", notes = "customs_data declare_price")
    private String declarePrice;

    //商品净重
    @ApiModelProperty(value = "商品净重", notes = "customs_data suttle")
    private String suttle;

    //品牌
    @ApiModelProperty(value = "品牌", notes = "customs_data brand")
    private String brand;

    //型号
    @ApiModelProperty(value = "型号", notes = "customs_data specification")
    private String specification;

    //图片
    @ApiModelProperty(value = "图片", notes = "customs_data pic_url")
    private String picUrl;

    //毛重
    @ApiModelProperty(value = "毛重", notes = "order_case asn_weight")
    private String grossWeight;

    //立方
    @ApiModelProperty(value = "立方", notes = "order_case asn_volume")
    private String cbm;

    //要计算的
    @ApiModelProperty(value = "总箱数")
    private String packages;

    @ApiModelProperty(value = "总数量")
    private String qty;

    @ApiModelProperty(value = "总价")
    private String totalPrice;

    @ApiModelProperty(value = "总净重")
    private String jz;





}
