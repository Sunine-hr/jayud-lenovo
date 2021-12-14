package com.jayud.scm.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 云报关数据
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
public class YunBaoGuanData {

    @ApiModelProperty(value = "报关ID")
    private Integer billId;

    @ApiModelProperty(value = "")
    private Integer pid;

    @ApiModelProperty(value = "")
    private String entryType;

    @ApiModelProperty(value = "")
    private Integer declareId;

    @ApiModelProperty(value = "")
    private Integer declareGroupId;

    @ApiModelProperty(value = "")
    private String indateDt;

    @ApiModelProperty(value = "")
    private String portNo2;

    @ApiModelProperty(value = "")
    private String soNo;

    @ApiModelProperty(value = "")
    private String recordsNo;

    @ApiModelProperty(value = "")
    private String transmodeNo;

    @ApiModelProperty(value = "")
    private String tradeNo;

    @ApiModelProperty(value = "")
    private String tradeCountryNo;

    @ApiModelProperty(value = "")
    private String bargainmodeNo;

    @ApiModelProperty(value = "")
    private String imposemodeNo;

    @ApiModelProperty(value = "")
    private String imposeNo;

    @ApiModelProperty(value = "")
    private String licenceNo;

    @ApiModelProperty(value = "")
    private String customerCustomsCode;

    @ApiModelProperty(value = "")
    private String customerName;

    @ApiModelProperty(value = "")
    private String startCountryNo;

    @ApiModelProperty(value = "")
    private String endCountryNo;

    @ApiModelProperty(value = "")
    private String loadportNo;

    @ApiModelProperty(value = "")
    private String portNo;

    @ApiModelProperty(value = "")
    private String bookNo;

    @ApiModelProperty(value = "")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "")
    private String transname;

    @ApiModelProperty(value = "")
    private String incotermsNo;

    @ApiModelProperty(value = "")
    private String remitmodeNo;

    @ApiModelProperty(value = "")
    private String currencyNo;

    @ApiModelProperty(value = "")
    private String freightNo;

    @ApiModelProperty(value = "")
    private String freightmodeNo;

    @ApiModelProperty(value = "")
    private BigDecimal freight;

    @ApiModelProperty(value = "")
    private String freightCurrencyNo;

    @ApiModelProperty(value = "")
    private String subscribemodeNo;

    @ApiModelProperty(value = "")
    private BigDecimal subscribe;

    @ApiModelProperty(value = "")
    private String subscribeCurrencyNo;

    @ApiModelProperty(value = "")
    private String incidentalmodeNo;

    @ApiModelProperty(value = "")
    private String incidentalCurrencyNo;

    @ApiModelProperty(value = "")
    private BigDecimal incidental;

    @ApiModelProperty(value = "")
    private String premiumNo;

    @ApiModelProperty(value = "")
    private String premium;

    @ApiModelProperty(value = "")
    private String premiumCurrency;

    @ApiModelProperty(value = "")
    private String otherCost;

    @ApiModelProperty(value = "")
    private String otherCostNo;

    @ApiModelProperty(value = "")
    private String otherCostCurrency;

    @ApiModelProperty(value = "")
    private Integer packages;

    @ApiModelProperty(value = "")
    private String entryPackages;

    @ApiModelProperty(value = "")
    private String packNo;

    @ApiModelProperty(value = "")
    private BigDecimal grossweight;

    @ApiModelProperty(value = "")
    private BigDecimal netweight;

    @ApiModelProperty(value = "")
    private BigDecimal gw;

    @ApiModelProperty(value = "")
    private BigDecimal nw;

    @ApiModelProperty(value = "")
    private BigDecimal entryNw;

    @ApiModelProperty(value = "")
    private BigDecimal entryGw;

    @ApiModelProperty(value = "")
    private String cnTruckNo;

    @ApiModelProperty(value = "")
    private String asorigincode;

    @ApiModelProperty(value = "")
    private String truckNo;

    @ApiModelProperty(value = "")
    private String cnTruckNo1;

    @ApiModelProperty(value = "")
    private String pickUpNo;

    @ApiModelProperty(value = "")
    private String billNo;

    @ApiModelProperty(value = "")
    private String remark;

    @ApiModelProperty(value = "")
    private String preTruckStyle;

    @ApiModelProperty(value = "")
    private String cabinetNo;

    @ApiModelProperty(value = "")
    private String cabinNo;

    @ApiModelProperty(value = "")
    private String numNo;

    @ApiModelProperty(value = "")
    private String lockNum;

    @ApiModelProperty(value = "")
    private String drawbackNo;

    @ApiModelProperty(value = "")
    private String vessel;

    @ApiModelProperty(value = "")
    private String sellerTel;



    @ApiModelProperty(value = "")
    private String tsgxqr;

    @ApiModelProperty(value = "")
    private String jgyxqr;

    @ApiModelProperty(value = "")
    private String zftxqsyfqr;

    @ApiModelProperty(value = "")
    private String note;




    @ApiModelProperty(value = "")
    private String itemNo;

    @ApiModelProperty(value = "")
    private String goodsName;

    @ApiModelProperty(value = "")
    private String goodsNo;

    @ApiModelProperty(value = "")
    private String goodsSpec;

    @ApiModelProperty(value = "")
    private String elements;

    @ApiModelProperty(value = "")
    private BigDecimal amount;

    @ApiModelProperty(value = "")
    private BigDecimal sum;

    @ApiModelProperty(value = "")
    private String unitNo;

    @ApiModelProperty(value = "")
    private String unit02No;

    @ApiModelProperty(value = "")
    private String unit03No;

    @ApiModelProperty(value = "")
    private BigDecimal amount02;

    @ApiModelProperty(value = "")
    private BigDecimal amount03;

    @ApiModelProperty(value = "")
    private BigDecimal price;

    @ApiModelProperty(value = "")
    private BigDecimal cipPrice;

    @ApiModelProperty(value = "")
    private BigDecimal totalCipPrice;

    @ApiModelProperty(value = "")
    private String countryNo;

    @ApiModelProperty(value = "")
    private String otherCountryNo;

    @ApiModelProperty(value = "")
    private String sysCustomerName;

    @ApiModelProperty(value = "")
    private String sysTaxno;

    @ApiModelProperty(value = "")
    private String sysCustomsCode;

    @ApiModelProperty(value = "")
    private String agentName;

    @ApiModelProperty(value = "")
    private String agentNo;

    @ApiModelProperty(value = "")
    private String agentCode;

    @ApiModelProperty(value = "")
    private String hgCurrencyNo;

    @ApiModelProperty(value = "")
    private String voyage;

    @ApiModelProperty(value = "")
    private String orderEntryId;

    @ApiModelProperty(value = "")
    private String orderEntryNo;

    @ApiModelProperty(value = "")
    private BigDecimal allMoney;

    @ApiModelProperty(value = "")
    private String truckTime;

    @ApiModelProperty(value = "")
    private String itemNotes;

    @ApiModelProperty(value = "")
    private String billEntryId;

    @ApiModelProperty(value = "")
    private String despPort;

    @ApiModelProperty(value = "")
    private String goodsplace;

    @ApiModelProperty(value = "")
    private String markno;

    @ApiModelProperty(value = "")
    private String distinateport;

    @ApiModelProperty(value = "")
    private String buyerNo;

    @ApiModelProperty(value = "")
    private String buyersAddress;

    @ApiModelProperty(value = "")
    private String buyersTel;

    @ApiModelProperty(value = "")
    private String buyerName;

    @ApiModelProperty(value = "")
    private String busNo;

    @ApiModelProperty(value = "")
    private String exgNo;

    @ApiModelProperty(value = "")
    private Integer gif;

    @ApiModelProperty(value = "")
    private String sjgoodsNo;

    @ApiModelProperty(value = "")
    private String ciqName;

    @ApiModelProperty(value = "")
    private String districtcode;

    @ApiModelProperty(value = "")
    private String origincode;

    @ApiModelProperty(value = "")
    private String consigneeNo;

    @ApiModelProperty(value = "")
    private String contractNo;

    @ApiModelProperty(value = "")
    private String contractDt;

    @ApiModelProperty(value = "")
    private String invoiceDt;

    @ApiModelProperty(value = "")
    private String invoiceNo;

    @ApiModelProperty(value = "")
    private String signedAt;

    @ApiModelProperty(value = "")
    private String piece;

    @ApiModelProperty(value = "")
    private String consigneeName;

    @ApiModelProperty(value = "")
    private String consigneeCode;

    @ApiModelProperty(value = "")
    private String shipperName;

    @ApiModelProperty(value = "")
    private String shipperCode;

    @ApiModelProperty(value = "")
    private String shipperNo;

    @ApiModelProperty(value = "")
    private String gsdjqr;

    @ApiModelProperty(value = "")
    private String zdjgqr;

    @ApiModelProperty(value = "")
    private String sjcg;

}
