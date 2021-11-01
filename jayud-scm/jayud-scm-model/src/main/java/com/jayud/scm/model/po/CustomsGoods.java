package com.jayud.scm.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 委托单-商品列表
 *
 * @author william
 * @description
 * @Date: 2020-09-07 18:31
 */
@Data
public class CustomsGoods {
    @ApiModelProperty(value = "序号")
    @JsonProperty("num_no")
    @SerializedName("num_no")
    private Integer numNo;

    @ApiModelProperty(value = "项号")
    @JsonProperty("item_no")
    @SerializedName("item_no")
    private String itemNo;

    @ApiModelProperty(value = "商品编号")
    @JsonProperty("goods_no")
    @SerializedName("goods_no")
    private String goodsNo;

    @ApiModelProperty(value = "品名")
    @JsonProperty("goods_name")
    @SerializedName("goods_name")
    private String goodsName;

    @ApiModelProperty(value = "规格")
    @JsonProperty("goods_spec")
    @SerializedName("goods_spec")
    private String goodsSpec;

    @ApiModelProperty(value = "版本号")
    @JsonProperty("ver_no")
    @SerializedName("ver_no")
    private String verNo;

    @ApiModelProperty(value = "成交数量(18,5)")
    private BigDecimal amount;

    @ApiModelProperty(value = "成交单位编号")
    @JsonProperty("unit_no")
    @SerializedName("unit_no")
    private String unitNo;

    @ApiModelProperty(value = "法1数量(18,5)")
    private BigDecimal amount02;

    @ApiModelProperty(value = "法1单位编号")
    @JsonProperty("unit02_no")
    @SerializedName("unit02_no")
    private String unit02No;

    @ApiModelProperty(value = "法2数量(18,5)")
    private BigDecimal amount03;

    @ApiModelProperty(value = "法2单位编号")
    @JsonProperty("unit03_no")
    @SerializedName("unit03_no")
    private String unit03No;

    @ApiModelProperty(value = "目的国/原产国")
    @JsonProperty("country_no")
    @SerializedName("country_no")
    //出口填写目的国，进口填写原产国
    private String countryNo;

    @ApiModelProperty(value = "原产国/目的国")
    @JsonProperty("other_country_no")
    @SerializedName("other_country_no")
    //出口填写原产国，进口填写目的国
    private String otherCountryNo;

    @ApiModelProperty(value = "总价(18,4)")
    private BigDecimal sum;

    @ApiModelProperty(value = "单价(18,4)")
    private BigDecimal price;

    @ApiModelProperty(value = "币种编号")
    @JsonProperty("currency_no")
    @SerializedName("currency_no")
    private String currencyNo;

    @ApiModelProperty(value = "征免性质编号")
    @JsonProperty("impose_no")
    @SerializedName("impose_no")
    private String imposeNo;


    @ApiModelProperty(value = "毛重(18,4)")
    @JsonProperty("grossweight")
    @SerializedName("grossweight")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重(18,4)")
    @JsonProperty("netweight")
    @SerializedName("netweight")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "件数")
    private Integer piece;

    @ApiModelProperty(value = "货号")
    @JsonProperty("exg_no")
    @SerializedName("exg_no")
    private String exgNo;

    @ApiModelProperty(value = "工缴费")
    private Integer gjf;

    @ApiModelProperty(value = "检验检疫代码")
    @JsonProperty("sjgoods_no")
    @SerializedName("sjgoods_no")
    private String sjgoodsNo;

    @ApiModelProperty(value = "检验检疫名称")
    @JsonProperty("ciq_name")
    @SerializedName("ciq_name")
    private String ciqName;

    @ApiModelProperty(value = "境内货源地")
    @JsonProperty("districtcode")
    @SerializedName("districtcode")
    private String districtCode;

    @ApiModelProperty(value = "地区代码")
    @JsonProperty("origincode")
    @SerializedName("origincode")
    private String originCode;
}
