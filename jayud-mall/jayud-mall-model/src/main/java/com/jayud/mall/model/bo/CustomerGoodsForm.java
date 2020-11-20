package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerGoodsForm {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "商品编码")
    private String sku;

    @ApiModelProperty(value = "中文名")
    private String nameCn;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "条码")
    private String barcode;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "长")
    private BigDecimal length;

    @ApiModelProperty(value = "宽")
    private BigDecimal width;

    @ApiModelProperty(value = "高")
    private BigDecimal height;

    @ApiModelProperty(value = "是否敏感货物，1是0否，默认为0")
    private Boolean isSensitive;

    @ApiModelProperty(value = "商品类型(1普货 2特货)")
    private Integer types;

    @ApiModelProperty(value = "申报价值")
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币")
    private String declaredCurrency;

    @ApiModelProperty(value = "材质")
    private String materialQuality;

    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "申报要素")
    private String declareElements;

    @ApiModelProperty(value = "海关编码")
    private String hsCode;

    @ApiModelProperty(value = "报关编码(customs_data id_code)")
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)")
    private String clearanceCode;

    @ApiModelProperty(value = "销售链接")
    private String salesLink;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "销售价格的货币单位")
    private String salesPriceCurrency;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过")
    private Integer status;

    @ApiModelProperty(value = "审核状态名称")
    private String statusName;

    @ApiModelProperty(value = "备注")
    private String remark;

}
