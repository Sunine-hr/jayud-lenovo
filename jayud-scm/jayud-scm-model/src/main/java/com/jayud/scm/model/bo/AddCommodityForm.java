package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class AddCommodityForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品编号")
    private String skuNo;

    @ApiModelProperty(value = "型号")
    @NotNull(message = "商品型号不为空")
    private String skuModel;

    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "商品名称不为空")
    private String skuName;

    @ApiModelProperty(value = "报关名称")
    private String skuNameHs;

    @ApiModelProperty(value = "单位")
    @NotNull(message = "单位不为空")
    private String skuUnit;

    @ApiModelProperty(value = "品牌")
    @NotNull(message = "品牌不为空")
    private String skuBrand;

    @ApiModelProperty(value = "产地")
    private String skuOrigin;

    @ApiModelProperty(value = "商品描述")
    private String skuNotes;

    @ApiModelProperty(value = "配件")
    private String accessories;

    @ApiModelProperty(value = "单位净重")
    private BigDecimal unitNw;

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

    @ApiModelProperty(value = "料号")
    private String itemNo;

    @ApiModelProperty(value = "备注")
    private String remark;



}
