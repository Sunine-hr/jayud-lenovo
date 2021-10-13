package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 委托订单明细表
 */
@Data
public class BookingOrderEntryVO {

    @ApiModelProperty(value = "自动id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "自动id")
    private Integer bookingEntryId;

    @ApiModelProperty(value = "委托单id")
    private Integer bookingId;

    @ApiModelProperty(value = "提验货单id")
    private Integer checkId;

    @ApiModelProperty(value = "序号")
    private Integer itemNo;

    @ApiModelProperty(value = "产品id")
    private Integer itemId;

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

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "报关单价")
    private BigDecimal hgPrice;

    @ApiModelProperty(value = "运保费单价")
    private BigDecimal cipPrice;

    @ApiModelProperty(value = "开票汇率")
    private BigDecimal invoicePrice;

    @ApiModelProperty(value = "人民币价")
    private BigDecimal rmbPrice;

    @ApiModelProperty(value = "出口固定人民币采购价")
    private BigDecimal exRmbPrice;

    @ApiModelProperty(value = "总价格(=qty*price)")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "报关总价(=hg_price*qty)")
    private BigDecimal totalHgMoney;

    @ApiModelProperty(value = "目的国")
    private String destination;

    @ApiModelProperty(value = "海关编码")
    private String customsCode;

    @ApiModelProperty(value = "申报要素")
    private String elements;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "包装方式")
    private String packingType;

    @ApiModelProperty(value = "采购订货单号")
    private String po;

    @ApiModelProperty(value = "料号")
    private String pn;

    @ApiModelProperty(value = "批号")
    private String bn;

    @ApiModelProperty(value = "箱号")
    private String ctnsNo;

    @ApiModelProperty(value = "件数")
    private BigDecimal packages;

    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;

    @ApiModelProperty(value = "净重")
    private BigDecimal nw;

    @ApiModelProperty(value = "体积")
    private BigDecimal cbm;

    @ApiModelProperty(value = "尺寸")
    private BigDecimal itemSize;

    @ApiModelProperty(value = "唛头")
    private String shippingM;

    @ApiModelProperty(value = "订单号")
    private String demandNo;

    @ApiModelProperty(value = "入仓单号")
    private String inStoreNum;

    @ApiModelProperty(value = "功能")
    private String isFunction;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "外箱型号")
    private String packmodel;

    @ApiModelProperty(value = "提货单号")
    private String pickUpNo;

    @ApiModelProperty(value = "提货人联系人")
    private String pickUpUser;

    @ApiModelProperty(value = "提人联系方式")
    private String pickUpTel;

    @ApiModelProperty(value = "提货地址")
    private String pickUpAddress;

    @ApiModelProperty(value = "提货说明")
    private String pickUpRemark;

    @ApiModelProperty(value = "交货联系人")
    private String deliveryName;

    @ApiModelProperty(value = "交货联系电话")
    private String deliveryTel;

    @ApiModelProperty(value = "交货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "交货备注")
    private String deliveryRemark;

    @ApiModelProperty(value = "交仓时间")
    private String deliveryDate;

    @ApiModelProperty(value = "配件")
    private String accessories;

    @ApiModelProperty(value = "入库id")
    private Integer receivingId;

    @ApiModelProperty(value = "入库明细id")
    private Integer receivingEntryId;

    @ApiModelProperty(value = "监管条件")
    private String controlMa;

    @ApiModelProperty(value = "香港管制")
    private String hkControl;

    @ApiModelProperty(value = "增值税率")
    private BigDecimal tax;

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

    @ApiModelProperty(value = "客户合同号")
    private String customerNum;

    @ApiModelProperty(value = "是否需要商检 是 否")
    private String isCiq;

    @ApiModelProperty(value = "商检类型 ml, l")
    private String ciqType;

    @ApiModelProperty(value = "木卡板数")
    private Integer boardNum;

    @ApiModelProperty(value = "木卡国家")
    private String originCountry;

    @ApiModelProperty(value = "采购合同明细(针对出口)")
    private Integer contractEntryId;

    @ApiModelProperty(value = "预计到货时间")
    private LocalDate arrivalDate;

    @ApiModelProperty(value = "客户序号")
    private String customerSerno;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "原产地区代码【原产国内的生产区域，如州、省等】")
    private String origPlaceCode;

    @ApiModelProperty(value = "境内目的地/境内货源地，进口指境内目的地，出口指境内货源地")
    private String districtCode;

    @ApiModelProperty(value = "境外生产企业名称")
    private String engManEntCnm;

    @ApiModelProperty(value = "用途/生产厂家")
    private String useTo;

    @ApiModelProperty(value = "品牌类型")
    private String itemBrandType;

    @ApiModelProperty(value = "出口享惠情况")
    private String exportDiscounts;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedByDtm;


}
