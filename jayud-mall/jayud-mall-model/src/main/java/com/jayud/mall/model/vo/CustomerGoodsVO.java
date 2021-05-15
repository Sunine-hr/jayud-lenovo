package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.po.GoodsServiceCost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerGoodsVO {

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "客户ID(customer id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer customerId;

    @ApiModelProperty(value = "SKU商品编码", position = 3)
    @JSONField(ordinal = 3)
    @NotEmpty(message = "SKU商品编码必填")
    private String sku;

    @ApiModelProperty(value = "中文名", position = 4)
    @JSONField(ordinal = 4)
    @NotEmpty(message = "中文名必填")
    private String nameCn;

    @ApiModelProperty(value = "英文名", position = 5)
    @JSONField(ordinal = 5)
    private String nameEn;

    @ApiModelProperty(value = "图片地址", position = 6)
    @JSONField(ordinal = 6)
    private String imageUrl;

    @ApiModelProperty(value = "条形码", position = 7)
    @JSONField(ordinal = 7)
    private String barcode;

    @ApiModelProperty(value = "商品重量(KG)", position = 8)
    @JSONField(ordinal = 8)
    private BigDecimal weight;

    @ApiModelProperty(value = "长", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal length;

    @ApiModelProperty(value = "宽", position = 10)
    @JSONField(ordinal = 10)
    private BigDecimal width;

    @ApiModelProperty(value = "高", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal height;

    @ApiModelProperty(value = "是否敏感货物，1是0否，默认为0", position = 12)
    @JSONField(ordinal = 12)
    private String isSensitive;

    @ApiModelProperty(value = "商品类型(1普货 2特货)", position = 13)
    @JSONField(ordinal = 13)
    private Integer types;

    @ApiModelProperty(value = "申报价值", position = 14)
    @JSONField(ordinal = 14)
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币单位(currency_info currency_code)", position = 15)
    @JSONField(ordinal = 15)
    private String declaredCurrency;

    @ApiModelProperty(value = "材质", position = 16)
    @JSONField(ordinal = 16)
    private String materialQuality;

    @ApiModelProperty(value = "用途", position = 17)
    @JSONField(ordinal = 17)
    private String purpose;

    @ApiModelProperty(value = "申报要素", position = 18)
    @JSONField(ordinal = 18)
    private String declareElements;

    @ApiModelProperty(value = "海关编码", position = 19)
    @JSONField(ordinal = 19)
    private String hsCode;

    @ApiModelProperty(value = "报关编码(customs_data id_code)", position = 20)
    @JSONField(ordinal = 20)
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)", position = 21)
    @JSONField(ordinal = 21)
    private String clearanceCode;

    @ApiModelProperty(value = "销售链接", position = 22)
    @JSONField(ordinal = 22)
    private String salesLink;

    @ApiModelProperty(value = "销售价格", position = 23)
    @JSONField(ordinal = 23)
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "销售价格的货币单位(currency_info currency_code)", position = 24)
    @JSONField(ordinal = 24)
    private String salesPriceCurrency;

    @ApiModelProperty(value = "商品描述", position = 25)
    @JSONField(ordinal = 25)
    private String description;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过", position = 26)
    @JSONField(ordinal = 26)
    private Integer status;

    @ApiModelProperty(value = "审核状态名称", position = 27)
    @JSONField(ordinal = 27)
    private String statusName;

    @ApiModelProperty(value = "备注", position = 28)
    @JSONField(ordinal = 28)
    private String remark;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "丢货赔偿金额")
    private BigDecimal lossCompensationAmount;

    @ApiModelProperty(value = "丢货赔偿金额币种(currency_info id)")
    private Integer lcaCid;

    /*客户名称*/
    @ApiModelProperty(value = "客户名称", position = 29)
    @JSONField(ordinal = 29)
    private String customerName;

    /*商品类型名称*/
    @ApiModelProperty(value = "商品类型名称", position = 30)
    @JSONField(ordinal = 30)
    private String typesName;

    //商品服务费用
    @ApiModelProperty(value = "商品服务费用list")
    private List<GoodsServiceCost> goodsServiceCostList;

    @ApiModelProperty(value = "是否需要附加费(1需要 2不需要)")
    private String isNeedFee;

    @ApiModelProperty(value = "商品服务的单价,附加费")
    private String serviceUnitPrice;

    @ApiModelProperty(value = "商品服务的币种(currency_info id)")
    private Integer serviceCid;

    @ApiModelProperty(value = "商品服务的单位(1公斤 2方 3票 4柜)")
    private Integer serviceUnit;

}
