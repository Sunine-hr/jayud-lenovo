package com.jayud.oms.order.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class OmsOrderEntryVO extends SysBaseEntity {


    @ApiModelProperty(value = "工作单类型")
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
    private String controlMa;

    @ApiModelProperty(value = "香港管制")
    private String hkControl;

    @ApiModelProperty(value = "是否商检")
    private Integer isCiq;

    @ApiModelProperty(value = "商检类型")
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
    private BigDecimal gw;

    @ApiModelProperty(value = "净重")
    private BigDecimal nw;

    @ApiModelProperty(value = "体积")
    private BigDecimal cbm;

    @ApiModelProperty(value = "唛头")
    private String shippingM;

    @ApiModelProperty(value = "料号")
    private String pn;

    @ApiModelProperty(value = "批号")
    private String bn;

    @ApiModelProperty(value = "箱号")
    private String ctnsNo;

    @ApiModelProperty(value = "尺寸")
    private String itemSize;

    @ApiModelProperty(value = "功能")
    private String isFunction;

    @ApiModelProperty(value = "是否结关税(0为结,1为不结)")
    private Integer isHgTax;

    @ApiModelProperty(value = "指定关税率")
    private BigDecimal appointHgTax;

    @ApiModelProperty(value = "关税率")
    private BigDecimal hgTax;

    @ApiModelProperty(value = "消费税")
    private BigDecimal consumeTax;

    @ApiModelProperty(value = "退税率")
    private BigDecimal tlxTax;

    @ApiModelProperty(value = "品牌类型")
    private Integer itemBrandType;

    @ApiModelProperty(value = "出口享惠情况")
    private Integer exportDiscounts;

    @ApiModelProperty(value = "用途/生产厂家")
    private BigDecimal useTo;

    @ApiModelProperty(value = "境内目的地/境内货源地，进口指境内目的地，出口指境内货源地")
    private String districtCode;

    @ApiModelProperty(value = "原产地区代码【原产国内的生产区域，如州、省等】")
    private String origPlaceCode;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
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
