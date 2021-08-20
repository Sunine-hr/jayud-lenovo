package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 委托订单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BookingOrder对象", description = "委托订单主表")
public class BookingOrder extends Model<BookingOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "委托单号")
    private String bookingNo;

    @ApiModelProperty(value = "委托单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDate;

    @ApiModelProperty(value = "预计到货日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivedDate;

    @ApiModelProperty(value = "订单状态-1验货异常，-2异常已处理，-3提货异常， 0未确认,1已确认待交货,2已收货,3验货已完成,4入库,5已过关,6已过货,7配送中,8已签收,9部分出库，10已出库，11已报关")
    private Integer stateFlag;

    @ApiModelProperty(value = "客户名称、利润中心id")
    private Integer customerId;

    @ApiModelProperty(value = "客户编号")
    private String customerNo;

    @ApiModelProperty(value = "客户名称、利润中心")
    private String customerName;

    @ApiModelProperty(value = "客户类型")
    private String customerType;

    @ApiModelProperty(value = "采购人(客户下单人)")
    private String buyer;

    @ApiModelProperty(value = "客户邮箱")
    private String customerEmail;

    @ApiModelProperty(value = "供应商id")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商名称（可以使用，实际供应商在明细表）")
    private String supplierName;

    @ApiModelProperty(value = "供应商编号")
    private String supplierNo;

    @ApiModelProperty(value = "供应商类型")
    private String supplierType;

    @ApiModelProperty(value = "业务类型/工作单类型 1进口  2出口 3国内 4香港  5采购  6销售")
    private Integer modelType;

    @ApiModelProperty(value = "起运地（默认深圳、香港）")
    private String origin;

    @ApiModelProperty(value = "目的地")
    private String destination;

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

    @ApiModelProperty(value = "报关方式(自主报关，委托报关，客户自行报关)")
    private String hgType;

    @ApiModelProperty(value = "客户合同号")
    private String contractNo;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "是否报关(需要报关，等通知报关)")
    private String isHg;

    @ApiModelProperty(value = "订车id")
    private Integer hgTruckBookId;

    @ApiModelProperty(value = "报关车次id")
    private Integer hgTruckId;

    @ApiModelProperty(value = "报关车次编号")
    private String hgTruckNo;

    @ApiModelProperty(value = "审核人")
    @JsonProperty("fMultiLevei0")
    private String fMultiLevei0;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fDatetime")
    private LocalDateTime fDatetime;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    @JsonProperty("fLevel")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    @JsonProperty("fStep")
    private Integer fStep;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "境外客户id")
    private Integer overseasCustomersId;

    @ApiModelProperty(value = "境外客户名称")
    private String overseasCustomers;

    @ApiModelProperty(value = "入库单id")
    private Integer receivingId;

    @ApiModelProperty(value = "入库单单号")
    private String receivingNo;

    @ApiModelProperty(value = "客户联系人id")
    private Integer customerRelationId;

    @ApiModelProperty(value = "供应商联系人id")
    private Integer supplierRelationId;

    @ApiModelProperty(value = "结算id")
    private Integer arfeeId;

    @ApiModelProperty(value = "代理方式/协议类型")
    private String deputyStyle;

    @ApiModelProperty(value = "交易条件")
    private String incoterms;

    @ApiModelProperty(value = "付款方式")
    private String payFeeStyle;

    @ApiModelProperty(value = "付款帐期天数")
    private Integer payFeeDay;

    @ApiModelProperty(value = "收款方式")
    private String reFeeStyle;

    @ApiModelProperty(value = "帐期帐期天数")
    private Integer reFeeDay;

    @ApiModelProperty(value = "结算方式")
    private String accountType;

    @ApiModelProperty(value = "利息率")
    private Integer interest;

    @ApiModelProperty(value = "计息帐期天数")
    private Integer balanceDay;

    @ApiModelProperty(value = "保险")
    private String insurance;

    @ApiModelProperty(value = "下单汇率")
    @JsonProperty("fRate")
    private BigDecimal fRate;

    @ApiModelProperty(value = "报关当天汇率")
    private BigDecimal bgRate;

    @ApiModelProperty(value = "海关汇率")
    private BigDecimal hgRate;

    @ApiModelProperty(value = "到货汇率")
    private BigDecimal arrivedRate;

    @ApiModelProperty(value = "指定结算汇率")
    private BigDecimal invoiceRate;

    @ApiModelProperty(value = "收汇汇率")
    private BigDecimal receRate;

    @ApiModelProperty(value = "结算汇率")
    private BigDecimal accountRate;

    @ApiModelProperty(value = "指定货款结算汇率")
    private BigDecimal goodsRate;

    @ApiModelProperty(value = "折算汇率")
    private BigDecimal convertRate;

    @ApiModelProperty(value = "指定海关汇率")
    private BigDecimal appointHgRate;

    @ApiModelProperty(value = "指定综合费率")
    private BigDecimal compositeFee;

    @ApiModelProperty(value = "综合费率")
    private BigDecimal proxyFee;

    @ApiModelProperty(value = "固定代理费")
    private BigDecimal compositeProxyFee;

    @ApiModelProperty(value = "业务员id")
    private Integer fsalesId;

    @ApiModelProperty(value = "业务员")
    private String fsalesName;

    @ApiModelProperty(value = "商务id")
    private Integer followerId;

    @ApiModelProperty(value = "商务跟单")
    private String followerName;

    @ApiModelProperty(value = "运费比例")
    private BigDecimal yfee;

    @ApiModelProperty(value = "保费比例")
    private BigDecimal bfee;

    @ApiModelProperty(value = "保证金比例")
    private Integer earnestPer;

    @ApiModelProperty(value = "开票备注")
    private String invRemark;

    @ApiModelProperty(value = "取消最低收费，0默认，1取消最低收费")
    private Integer isLowestPrice;

    @ApiModelProperty(value = "开票客户id")
    private Integer invCustomerId;

    @ApiModelProperty(value = "开票付款客户名称(用于做购销协议及付款)")
    private String invCustomerName;

    @ApiModelProperty(value = "产品类别")
    private String productClass;

    @ApiModelProperty(value = "报关单id")
    private Integer billId;

    @ApiModelProperty(value = "报关抬头方式（单抬头、双抬头）")
    private String billStyle;

    @ApiModelProperty(value = "海关认证状态。0：不需制证，1需制证，2已制证")
    private Integer isHsStatus;

    @ApiModelProperty(value = "制证人id")
    private Integer makeBy;

    @ApiModelProperty(value = "制证人姓名")
    private String makeByName;

    @ApiModelProperty(value = "制证时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime makeDtm;

    @ApiModelProperty(value = "采购合同号")
    private String purchaseContractNo;

    @ApiModelProperty(value = "销售合同号")
    private String salesContractNo;

    @ApiModelProperty(value = "出口境内货源地")
    private String originArea;

    @ApiModelProperty(value = "香港公司id")
    private Integer hkCoId;

    @ApiModelProperty(value = "香港公司中文名称")
    private String hkCoName;

    @ApiModelProperty(value = "香港公司英文名称")
    private String hkCoNameEn;

    @ApiModelProperty(value = "香港公司中文地址")
    private String hkCoAddress;

    @ApiModelProperty(value = "香港公司英文地址")
    private String hkCoAddressEn;

    @ApiModelProperty(value = "香港公司联系人")
    private String hkCoRelationer;

    @ApiModelProperty(value = "香港公司电话")
    private String hkCoTel;

    @ApiModelProperty(value = "香港公司邮箱")
    private String hkCoEmail;

    @ApiModelProperty(value = "香港公司传真")
    private String hkCoFax;

    @ApiModelProperty(value = "报关币别")
    private String hgCurrencyName;

    @ApiModelProperty(value = "是否加急（0:不加急，1:加急）")
    private Integer isUrgent;

    @ApiModelProperty(value = "最低消费编号(用于合并计算最低收费)")
    private String minFeesNo;

    @ApiModelProperty(value = "0未同步1已同步")
    private Integer isSync;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "打印次数")
    private Integer printNum;

    @ApiModelProperty(value = "0未加急，1加急")
    private Integer isJiaji;

    @ApiModelProperty(value = "采购方id")
    private Integer purchaseid;

    @ApiModelProperty(value = "采购方名称")
    private String purchasename;

    @ApiModelProperty(value = "销售方id")
    private Integer saleid;

    @ApiModelProperty(value = "销售方名称")
    private String salename;

    @ApiModelProperty(value = "入仓单号、报关提运单号")
    private String hubNo;

    @ApiModelProperty(value = "主管海关（申报地海关）")
    private String customMaster;

    @ApiModelProperty(value = "经停港/指运港")
    private String distinatePort;

    @ApiModelProperty(value = "港口(出入境口岸)")
    private String port;

    @ApiModelProperty(value = "启运国/运抵国")
    private String tradeCountry;

    @ApiModelProperty(value = "贸易国别")
    private String tradeAreaCode;

    @ApiModelProperty(value = "启运港")
    private String despPortCode;

    @ApiModelProperty(value = "货物存放地")
    private String goodsPlace;

    @ApiModelProperty(value = "是否自报自缴")
    private Integer isUsPayment;

    @ApiModelProperty(value = "特殊关系确认/价格影响确认/支付特许权使用费确认")
    private String isConfirm;

    @ApiModelProperty(value = "so号")
    private String soNum;

    @ApiModelProperty(value = "收货人代码")
    private String consigneeCode;

    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "收货人电话")
    private String consigneeTel;

    @ApiModelProperty(value = "柜型")
    private String containerType;

    @ApiModelProperty(value = "收货人名称")
    private String consigneeName;

    @ApiModelProperty(value = "运输方式")
    private String transportationType;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
