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
    private Integer modelType;

    @ApiModelProperty(value = "")
    private String entryType;

    @ApiModelProperty(value = "")
    private String customerNo;

    @ApiModelProperty(value = "")
    private String customerCustomsCode;

    @ApiModelProperty(value = "")
    private String customerName;

    @ApiModelProperty(value = "")
    private String startCountryNo;

    @ApiModelProperty(value = "")
    private String endCountryNo;

    @ApiModelProperty(value = "")
    private String portNo;

    @ApiModelProperty(value = "")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "")
    private String transname;

    @ApiModelProperty(value = "")
    private String incotermsNo;

    @ApiModelProperty(value = "")
    private String currencyNo;

    @ApiModelProperty(value = "")
    private String freightNo;

    @ApiModelProperty(value = "")
    private String freight;

    @ApiModelProperty(value = "")
    private String freightCurrency;

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
    private String packNO;

    @ApiModelProperty(value = "")
    private BigDecimal gw;

    @ApiModelProperty(value = "")
    private BigDecimal nw;

    @ApiModelProperty(value = "")
    private String entryNw;

    @ApiModelProperty(value = "")
    private String entryGw;

    @ApiModelProperty(value = "")
    private String cnTruckNo;

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
    private String lockNum;

    @ApiModelProperty(value = "")
    private String itemName;

    @ApiModelProperty(value = "")
    private String itemModel;

    @ApiModelProperty(value = "")
    private String itemBrand;

    @ApiModelProperty(value = "")
    private String elements;

    @ApiModelProperty(value = "")
    private String unitNo;

    @ApiModelProperty(value = "")
    private String unitNo2;

    @ApiModelProperty(value = "")
    private String unitNo3;

    @ApiModelProperty(value = "")
    private BigDecimal qty;

    @ApiModelProperty(value = "")
    private BigDecimal qty2;

    @ApiModelProperty(value = "")
    private BigDecimal qty3;

    @ApiModelProperty(value = "")
    private BigDecimal cipPrice;

    @ApiModelProperty(value = "")
    private BigDecimal totalCipPrice;

    @ApiModelProperty(value = "")
    private String CountryNo;

    @ApiModelProperty(value = "")
    private String OtherCountryNo;

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
    private String itemNo;

    @ApiModelProperty(value = "")
    private String despport;

    @ApiModelProperty(value = "")
    private String goodsplace;

    @ApiModelProperty(value = "")
    private String markno;

    @ApiModelProperty(value = "")
    private String distinateport;

    @ApiModelProperty(value = "")
    private String buyerNo;

    @ApiModelProperty(value = "")
    private String buyerName;

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
