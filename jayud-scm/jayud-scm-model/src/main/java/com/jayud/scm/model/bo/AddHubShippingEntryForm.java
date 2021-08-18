package com.jayud.scm.model.bo;

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
 * 出库单明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddHubShippingEntryForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "出库ID")
    private Integer shippingId;

    @ApiModelProperty(value = "订单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "订单号")
    private String bookingNo;

    @ApiModelProperty(value = "订单明细ID")
    private Integer bookingEntryId;

    @ApiModelProperty(value = "入库ID")
    private Integer receivingId;

    @ApiModelProperty(value = "入库单号")
    private String receivingNo;

    @ApiModelProperty(value = "入库明细ID")
    private Integer receivingEntryId;

    @ApiModelProperty(value = "报关单ID")
    private Integer billId;

    @ApiModelProperty(value = "报关合同号")
    private String billNo;

    @ApiModelProperty(value = "商品Id")
    private Integer itemId;

    @ApiModelProperty(value = "序号")
    private Integer itemNo;

    @ApiModelProperty(value = "型号")
    private String itemModel;

    @ApiModelProperty(value = "名称")
    private String itemName;

    @ApiModelProperty(value = "品牌")
    private String itemBrand;

    @ApiModelProperty(value = "产地")
    private String itemOrigin;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "单价")
    private BigDecimal hgPrice;

    @ApiModelProperty(value = "件数")
    private Integer packages;

    @ApiModelProperty(value = "板数")
    private Integer pallets;

    @ApiModelProperty(value = "箱数")
    private Integer cartons;

    @ApiModelProperty(value = "散箱")
    private Integer iCartons;

    @ApiModelProperty(value = "箱号")
    private String ctnsNo;

    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;

    @ApiModelProperty(value = "净重")
    private BigDecimal nw;

    @ApiModelProperty(value = "材积")
    private BigDecimal cbm;

    @ApiModelProperty(value = "备用1")
    private String temp01;

    @ApiModelProperty(value = "备用2")
    private String temp02;

    @ApiModelProperty(value = "备用3")
    private String temp03;

    @ApiModelProperty(value = "0:未检货，1：正在检货，2：检货完成，3：复核完成")
    private Integer stateFlag;

    @ApiModelProperty(value = "检货开始时间")
    private LocalDateTime checkBeginTime;

    @ApiModelProperty(value = "检货完成时间")
    private LocalDateTime checkEndTime;

    @ApiModelProperty(value = "检货说明")
    private String checkRemark;

    @ApiModelProperty(value = "料号")
    private String pn;

    @ApiModelProperty(value = "是否同步在线")
    private Integer isOnl;

    @ApiModelProperty(value = "在线ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线单编号")
    private String onlNo;

    @ApiModelProperty(value = "要求")
    private String requirement;

    @ApiModelProperty(value = "拣货")
    private String pickRequested;

    @ApiModelProperty(value = "配货")
    private String allocatedRequested;

    @ApiModelProperty(value = "包装")
    private String packRequested;

    @ApiModelProperty(value = "QC")
    private String qcRequested;

    @ApiModelProperty(value = "帖标")
    private String labelRequested;

    @ApiModelProperty(value = "是否拆箱")
    private Integer isUnpacking;

    @ApiModelProperty(value = "明细入仓号")
    private String whNo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
