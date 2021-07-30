package com.jayud.scm.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 添加商品表单
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCommodityModelForm {

    @ExcelProperty("商品型号")
    @ApiModelProperty(value = "型号")
    @NotNull(message = "商品型号不为空")
    private String skuModel;

    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "商品名称不为空")
    private String skuName;

    @ExcelProperty("报关名称")
    @ApiModelProperty(value = "报关名称")
    private String skuNameHs;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位")
    @NotNull(message = "单位不为空")
    private String skuUnit;

    @ExcelProperty("品牌")
    @ApiModelProperty(value = "品牌")
    @NotNull(message = "品牌不为空")
    private String skuBrand;

    @ExcelProperty("产地")
    @ApiModelProperty(value = "产地")
    private String skuOrigin;

    @ExcelProperty("商品描述")
    @ApiModelProperty(value = "商品描述")
    private String skuNotes;

    @ExcelProperty("配件")
    @ApiModelProperty(value = "配件")
    private String accessories;

    @ExcelProperty("单位净重")
    @ApiModelProperty(value = "单位净重")
    private BigDecimal unitNw;

    @ExcelProperty("参考价")
    @ApiModelProperty(value = "参考价")
    private BigDecimal referencePrice;

//    @ApiModelProperty(value = "最高价")
//    private BigDecimal maxPrice;
//
//    @ApiModelProperty(value = "最低价")
//    private BigDecimal minPrice;
//
//    @ApiModelProperty(value = "平均价")
//    private BigDecimal avgPrice;

    @ExcelProperty("料号")
    @ApiModelProperty(value = "料号")
    private String itemNo;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注")
    private String remark;



}
