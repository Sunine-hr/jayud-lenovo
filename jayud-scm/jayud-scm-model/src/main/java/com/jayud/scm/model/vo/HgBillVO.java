package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 报关单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HgBillVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "报关合同号")
    private String billNo;

    @ApiModelProperty(value = "拼单日期")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "报关方式(0，为进口，1,出口)")
    private Integer modelType;

    @ApiModelProperty(value = "报关单号")
    private String customsNo;

    @ApiModelProperty(value = "报关日期")
    private LocalDateTime customsDate;

    @ApiModelProperty(value = "客户合同号")
    private String customerBillNo;

    @ApiModelProperty(value = "车次ID")
    private Integer hgTruckId;

    @ApiModelProperty(value = "车次编号")
    private String hgTruckNo;

    @ApiModelProperty(value = "总毛重")
    private BigDecimal totalGw;

    @ApiModelProperty(value = "总净重")
    private BigDecimal totalNw;

    @ApiModelProperty(value = "总材积")
    private BigDecimal totalCbm;

    @ApiModelProperty(value = "总件数")
    private Integer packages;

    @ApiModelProperty(value = "总板数")
    private Integer pallets;

    @ApiModelProperty(value = "总箱数")
    private Integer cartons;

    @ApiModelProperty(value = "总散箱数")
    @JsonProperty(value = "lCartons")
    private Integer lCartons;

    @ApiModelProperty(value = "0:未拼单，1：已拼单")
    private Integer isMerge;

    @ApiModelProperty(value = "FOB运费方式(按比例，按金额)")
    @JsonProperty(value = "fFeeType")
    private Integer fFeeType;

    @ApiModelProperty(value = "运费币别")
    @JsonProperty(value = "fFeeCurrency")
    private String fFeeCurrency;

    @ApiModelProperty(value = "运费金额")
    @JsonProperty(value = "fFeeMoney")
    private BigDecimal fFeeMoney;

    @ApiModelProperty(value = "运费比例")
    @JsonProperty(value = "fFeePer")
    private BigDecimal fFeePer;

    @ApiModelProperty(value = "FOB保费方式(按比例，按金额)")
    @JsonProperty(value = "bFeeType")
    private Integer bFeeType;

    @ApiModelProperty(value = "保费币别")
    @JsonProperty(value = "bFeeCurrency")
    private String bFeeCurrency;

    @ApiModelProperty(value = "保费金额")
    @JsonProperty(value = "bFeeMoney")
    private BigDecimal bFeeMoney;

    @ApiModelProperty(value = "保费比例")
    @JsonProperty(value = "bFeePer")
    private BigDecimal bFeePer;

    @ApiModelProperty(value = "FOB杂费方式(按比例，按金额)")
    @JsonProperty(value = "oFeeType")
    private Integer oFeeType;

    @ApiModelProperty(value = "杂费币别")
    @JsonProperty(value = "oFeeCurrency")
    private String oFeeCurrency;

    @ApiModelProperty(value = "杂费金额")
    @JsonProperty(value = "oFeeMoney")
    private BigDecimal oFeeMoney;

    @ApiModelProperty(value = "杂费比例")
    @JsonProperty(value = "oFeePer")
    private BigDecimal oFeePer;

    @ApiModelProperty(value = "香港公司ID")
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

    @ApiModelProperty(value = "提运单号")
    private String pickUpNo;

    @ApiModelProperty(value = "柜数")
    private String cabinetNum;

    @ApiModelProperty(value = "香港报关单号")
    private String hkBillNo;

    @ApiModelProperty(value = "报关单申报状态：0未提交，1已提交，2提交成功，3生成成功")
    private Integer declareState;

    @ApiModelProperty(value = "关联关检号")
    private String seqNo;

    @ApiModelProperty(value = "是否处理香港管制商品0:是1:否")
    private Integer isHandleControl;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "一审审核人")
    @JsonProperty(value = "fMultiLevel0")
    private String fMultiLevel0;

    @ApiModelProperty(value = "一审核 时间")
    @JsonProperty(value = "fDateTime0")
    private LocalDateTime fDateTime0;

    @ApiModelProperty(value = "终审人")
    @JsonProperty(value = "fMultiLevel1")
    private String fMultiLevel1;

    @ApiModelProperty(value = "终审时间")
    @JsonProperty(value = "fDateTime1")
    private LocalDateTime fDateTime1;

    @ApiModelProperty(value = "是否扫描")
    private Integer isScan;

    @ApiModelProperty(value = "扫描时间")
    private LocalDateTime scanDateTime;

    @ApiModelProperty(value = "打印次数")
    private Integer printCount;

    @ApiModelProperty(value = "是否提交财务")
    private String isFinancial;

    @ApiModelProperty(value = "清关公司ID")
    private Integer hgCompanyId;

    @ApiModelProperty(value = "清关公司名称")
    private String hgCompanyName;

    @ApiModelProperty(value = "清关公司英文名称")
    private String hgCompanyNameEn;

    private String userName;

    @ApiModelProperty(value = "客户数")
    private Integer customerSum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;


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

}
