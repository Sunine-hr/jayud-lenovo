package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 入库明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HubReceivingEntryVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "入库主表ID")
    private Integer receivingId;

    @ApiModelProperty(value = "序号")
    private Integer itemNo;

    @ApiModelProperty(value = "订单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "订单编号")
    private String bookingNo;

    @ApiModelProperty(value = "订单明细ID")
    private Integer bookingEntryId;

    @ApiModelProperty(value = "商品库ID")
    private Integer itemId;

    @ApiModelProperty(value = "型号")
    private String itemModel;

    @ApiModelProperty(value = "名称")
    private String itemName;

    @ApiModelProperty(value = "品牌")
    private String itemBrand;

    @ApiModelProperty(value = "产地")
    private String itemOrigin;

    @ApiModelProperty(value = "商品备注")
    private String itemNotes;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    private BigDecimal hgPrice;

    @ApiModelProperty(value = "件数")
    private Integer packages;

    @ApiModelProperty(value = "板数")
    private Integer pallets;

    @ApiModelProperty(value = "箱数")
    private Integer cartons;

    @ApiModelProperty(value = "散箱数")
    private Integer lCartons;

    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;

    @ApiModelProperty(value = "净重")
    private BigDecimal nw;

    @ApiModelProperty(value = "体积")
    private BigDecimal cbm;

    @ApiModelProperty(value = "LOT_LIST")
    private String lotList;

    @ApiModelProperty(value = "采购单号")
    private String po;

    @ApiModelProperty(value = "尺寸")
    private String ctnsSize;

    @ApiModelProperty(value = "箱号")
    private String ctnsNo;

    @ApiModelProperty(value = "报关表ID")
    private Integer billId;

    @ApiModelProperty(value = "报关编号")
    private String billNo;

    @ApiModelProperty(value = "外箱型号")
    private String ctnsItemModel;

    @ApiModelProperty(value = "是否已打印")
    private Integer isPrint;

    @ApiModelProperty(value = "打印次数")
    private Integer printNum;

    @ApiModelProperty(value = "打印总张数")
    private BigDecimal printSum;

    @ApiModelProperty(value = "料号")
    private String pn;

    @ApiModelProperty(value = "批号")
    private String bn;

    @ApiModelProperty(value = "入库明细状态(0：待验货，1：已验货，2：验货异常，3：已复核，4：已入库)")
    private BigDecimal stateFlag;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "是否同步在线")
    private BigDecimal isOnl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "备用字段1")
    private String temp01;

    @ApiModelProperty(value = "备用字段2")
    private String temp02;

    @ApiModelProperty(value = "备用字段3")
    private String temp03;

    @ApiModelProperty(value = "备用字段4")
    private String temp04;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

}
