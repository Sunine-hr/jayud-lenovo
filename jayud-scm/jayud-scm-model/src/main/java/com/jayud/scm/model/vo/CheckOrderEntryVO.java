package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 提验货单明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckOrderEntryVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "验货主表ID")
    private Integer checkId;

    @ApiModelProperty(value = "订单明细ID")
    private Integer bookingEntryId;

    @ApiModelProperty(value = "订单主表ID")
    private Integer bookingId;

    @ApiModelProperty(value = "订单编号")
    private String bookingNo;

    @ApiModelProperty(value = "订单项次号")
    private Integer itemNum;

    @ApiModelProperty(value = "型号")
    private String itemModel;

    @ApiModelProperty(value = "名称")
    private String itemName;

    @ApiModelProperty(value = "品牌")
    private String itemBrand;

    @ApiModelProperty(value = "产地")
    private String itemOrigin;

    @ApiModelProperty(value = "描述")
    private String itemNotes;

    @ApiModelProperty(value = "配件")
    @JsonProperty(value = "itemAccs")
    private String itemAccs;

    @ApiModelProperty(value = "数量")
    @JsonProperty(value = "qty")
    private BigDecimal qty;

    @ApiModelProperty(value = "单位")
    @JsonProperty(value = "unit")
    private String unit;

    @ApiModelProperty(value = "批号")
    @JsonProperty(value = "pn")
    private String pn;

    @ApiModelProperty(value = "BN")
    @JsonProperty(value = "bn")
    private String bn;

    @ApiModelProperty(value = "箱号")
    @JsonProperty(value = "ctnsNo")
    private String ctnsNo;

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

    @ApiModelProperty(value = "采购单号")
    private String demandNo;

    @ApiModelProperty(value = "入库单号")
    private String inStoreNum;

    @ApiModelProperty(value = "包装方式")
    private String packagesType;

    @ApiModelProperty(value = "件数")
    private BigDecimal packages;

    @ApiModelProperty(value = "板数")
    private BigDecimal pallets;

    @ApiModelProperty(value = "箱数")
    private BigDecimal cartons;

    @ApiModelProperty(value = "散箱")
    @JsonProperty(value = "lcartons")
    private BigDecimal lcartons;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "验货备注")
    private String checkRemark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

}
