package com.jayud.oms.order.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OmsOrderEntry 实体类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="订单管理-订单明细表对象", description="订单管理-订单明细表")
public class InputOmsOrderEntryForm extends SysBaseEntity {


    @ApiModelProperty(value = "工作单类型")
    @JsonProperty(value = "sType")
    private String sType;

    @ApiModelProperty(value = "订单主表ID")
    private Long omsOrderId;

    @ApiModelProperty(value = "工作单主表ID")
    private Long workOrderId;

    @ApiModelProperty(value = "商品ID")
    private Long itemId;

    @ApiModelProperty(value = "商品序号")
    private Integer itemNum;

    @ApiModelProperty(value = "商品编码")
    private String itemNo;

    @ApiModelProperty(value = "商品分类")
    private String itemClassification;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "型号")
    private String itemModel;

    @ApiModelProperty(value = "名称")
    private String itemName;

    @ApiModelProperty(value = "品牌")
    private String itemBrand;

    @ApiModelProperty(value = "产地")
    private String itemOrigin;

    @ApiModelProperty(value = "商品描述")
    private String itemNotes;

    @ApiModelProperty(value = "配件")
    private String accessories;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "采购单价")
    private BigDecimal buyPrice;

    @ApiModelProperty(value = "报关单价")
    private BigDecimal hgPrice;

    @ApiModelProperty(value = "海关编码")
    private String customsCode;

    @ApiModelProperty(value = "申报要素")
    private String itemElements;

    @ApiModelProperty(value = "监管条件")
    @JsonProperty(value = "controlMa")
    private String controlMa;

    @ApiModelProperty(value = "香港管制")
    @JsonProperty(value = "hkControl")
    private String hkControl;

    @ApiModelProperty(value = "是否商检")
    @JsonProperty(value = "isCiq")
    private Integer isCiq;

    @ApiModelProperty(value = "商检类型")
    @JsonProperty(value = "ciqType")
    private String ciqType;

    @ApiModelProperty(value = "商检编码")
    private String ciqCode;

    @ApiModelProperty(value = "包装方式")
    private String packingType;

    @ApiModelProperty(value = "件数")
    private Integer packages;

    @ApiModelProperty(value = "板数")
    private Integer pallets;

    @ApiModelProperty(value = "毛重")
    @JsonProperty(value = "gw")
    private BigDecimal gw;

    @ApiModelProperty(value = "净重")
    @JsonProperty(value = "nw")
    private BigDecimal nw;

    @ApiModelProperty(value = "体积")
    @JsonProperty(value = "cbm")
    private BigDecimal cbm;

    @ApiModelProperty(value = "唛头")
    @JsonProperty(value = "shippingM")
    private String shippingM;

    @ApiModelProperty(value = "料号")
    @JsonProperty(value = "pn")
    private String pn;

    @ApiModelProperty(value = "批号")
    @JsonProperty(value = "bn")
    private String bn;

    @ApiModelProperty(value = "箱号")
    @JsonProperty(value = "ctnsNo")
    private String ctnsNo;

    @ApiModelProperty(value = "尺寸")
    private String itemSize;

    @ApiModelProperty(value = "功能")
    @JsonProperty(value = "isFunction")
    private String isFunction;

    @ApiModelProperty(value = "是否结关税(0为结,1为不结)")
    @JsonProperty(value = "isHgTax")
    private Integer isHgTax;

    @ApiModelProperty(value = "指定关税率")
    @JsonProperty(value = "appointHgTax")
    private BigDecimal appointHgTax;

    @ApiModelProperty(value = "关税率")
    @JsonProperty(value = "hgTax")
    private BigDecimal hgTax;

    @ApiModelProperty(value = "消费税")
    @JsonProperty(value = "consumeTax")
    private BigDecimal consumeTax;

    @ApiModelProperty(value = "退税率")
    @JsonProperty(value = "tlxTax")
    private BigDecimal tlxTax;

    @ApiModelProperty(value = "品牌类型")
    private Integer itemBrandType;

    @ApiModelProperty(value = "出口享惠情况")
    private Integer exportDiscounts;

    @ApiModelProperty(value = "用途/生产厂家")
    @JsonProperty(value = "useTo")
    private BigDecimal useTo;

    @ApiModelProperty(value = "境内目的地/境内货源地，进口指境内目的地，出口指境内货源地")
    private String districtCode;

    @ApiModelProperty(value = "原产地区代码【原产国内的生产区域，如州、省等】")
    private String origPlaceCode;

    @ApiModelProperty(value = "组织机构ID")
    @JsonProperty(value = "orgId")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
    @JsonProperty(value = "tenantId")
    private Long tenantId;

    @ApiModelProperty(value = "备注")
    private String remark;






    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "删除人")
    private Long deletedUserId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteUserName;


}
