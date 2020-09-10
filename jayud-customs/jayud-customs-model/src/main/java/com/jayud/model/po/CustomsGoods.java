package com.jayud.model.po;

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
    private Integer num_no;
    @ApiModelProperty(value = "项号")
    private String item_no;
    @ApiModelProperty(value = "商品编号")
    private String goods_no;
    @ApiModelProperty(value = "品名")
    private String goods_name;
    @ApiModelProperty(value = "规格")
    private String goods_spec;
    @ApiModelProperty(value = "版本号")
    private String ver_no;
    @ApiModelProperty(value = "成交数量(18,5)")
    private BigDecimal amount;

    @ApiModelProperty(value = "成交单位编号")
    private String unit_no;
    @ApiModelProperty(value = "法1数量(18,5)")
    private BigDecimal amount02;
    @ApiModelProperty(value = "法1单位编号")
    private String unit02_no;
    @ApiModelProperty(value = "法2数量(18,5)")
    private BigDecimal amount03;
    @ApiModelProperty(value = "法2单位编号")
    private String unit03_no;
    @ApiModelProperty(value = "目的国/原产国")
    //出口填写目的国，进口填写原产国
    private String country_no;
    @ApiModelProperty(value = "原产国/目的国")
    //出口填写原产国，进口填写目的国
    private String other_country_no;
    @ApiModelProperty(value = "总价(18,4)")
    private BigDecimal sum;
    @ApiModelProperty(value = "单价(18,4)")
    private BigDecimal price;
    @ApiModelProperty(value = "币种编号")
    private String currency_no;
    @ApiModelProperty(value = "征免性质编号")
    private String impose_no;
    ;

    @ApiModelProperty(value = "毛重(18,4)")
    private BigDecimal grossweight;

    @ApiModelProperty(value = "净重(18,4)")
    private BigDecimal netweight;

    @ApiModelProperty(value = "件数")
    private Integer piece;
    @ApiModelProperty(value = "货号")
    private String exg_no;
    @ApiModelProperty(value = "工缴费")
    private Integer gjf;
    @ApiModelProperty(value = "检验检疫代码")
    private String sjgoods_no;
    @ApiModelProperty(value = "检验检疫名称")
    private String ciq_name;
    @ApiModelProperty(value = "境内货源地")
    private String districtcode;
    @ApiModelProperty(value = "地区代码")
    private String origincode;
}
