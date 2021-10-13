package com.jayud.scm.model.vo;

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
import java.util.List;

/**
 * <p>
 * 出库单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HubShippingVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "出库单号")
    private String shippingNo;

    @ApiModelProperty(value = "业务类型")
    private Integer modelType;

    @ApiModelProperty(value = "业务类型")
    private String modelTypeName;

    @ApiModelProperty(value = "出库类型(0正常，1暂存)")
    @JsonProperty(value = "sModel")
    private Integer sModel;

    @ApiModelProperty(value = "出库次数")
    @JsonProperty(value = "sType")
    private Integer sType;

    @ApiModelProperty(value = "订单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "订单号")
    private String bookingNo;

    @ApiModelProperty(value = "要求交货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;

    @ApiModelProperty(value = "出库日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippingDate;

    @ApiModelProperty(value = "仓库名称")
    private String hubName;

    @ApiModelProperty(value = "入库ID")
    private Integer receivingId;

    @ApiModelProperty(value = "入库单号")
    private String receivingNo;

    @ApiModelProperty(value = "配送ID")
    private Integer deliverId;

    @ApiModelProperty(value = "配送编号")
    private String deliverNo;

    @ApiModelProperty(value = "发货类型（0自提，1发货，2配送，3直送发货，4转仓，5存仓发货,6.暂放仓库）")
    private String deliverType;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "委托客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "总数量")
    private BigDecimal totalQty;

    @ApiModelProperty(value = "总毛重")
    private BigDecimal totalGw;

    @ApiModelProperty(value = "总净重")
    @JsonProperty(value = "totaNw")
    private BigDecimal totaNw;

    @ApiModelProperty(value = "总体积")
    @JsonProperty(value = "totaCbm")
    private BigDecimal totaCbm;

    @ApiModelProperty(value = "总件数")
    @JsonProperty(value = "totaPackages")
    private Integer totaPackages;

    @ApiModelProperty(value = "总板数")
    @JsonProperty(value = "totaPallets")
    private Integer totaPallets;

    @ApiModelProperty(value = "总箱数")
    @JsonProperty(value = "totaCartons")
    private Integer totaCartons;

    @ApiModelProperty(value = "总散箱")
    private Integer totalCartons;

    @ApiModelProperty(value = "交货方式")
    private String whType;

    @ApiModelProperty(value = "交仓单号（入仓号）")
    private String whNo;

    @ApiModelProperty(value = "交货电话")
    private String whTel;

    @ApiModelProperty(value = "交货联系人")
    private String whName;

    @ApiModelProperty(value = "交货地址")
    private String whAddress;

    @ApiModelProperty(value = "收货时间")
    private String whDate;

    @ApiModelProperty(value = "客户联系人")
    private String customerContacts;

    @ApiModelProperty(value = "客户联系电话")
    private String customerTel;

    @ApiModelProperty(value = "检货人")
    private String checkName;

    @ApiModelProperty(value = "检货开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkBeginTime;

    @ApiModelProperty(value = "检货结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkEndTime;

    @ApiModelProperty(value = "检货说明")
    private String checkRemark;

    @ApiModelProperty(value = "复核人")
    private String reviewName;

    @ApiModelProperty(value = "复核完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    @ApiModelProperty(value = "复核说明")
    private String reviewRemark;

    @ApiModelProperty(value = "签收日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    @ApiModelProperty(value = "签收备注")
    private String signRemak;

    @ApiModelProperty(value = "签收状态（0:未签收，1已签收）")
    private Integer signState;

    @ApiModelProperty(value = "交货仓库")
    private String deliveryWarehouse;

    @ApiModelProperty(value = "收货公司")
    private String deliveryFactory;

    @ApiModelProperty(value = "交货备注")
    private String deliveryRemark;

    @ApiModelProperty(value = "港车ID")
    private Integer hgTruckId;

    @ApiModelProperty(value = "是否打印，0:未打印，1已打印")
    @JsonProperty(value = "isAutoPrint")
    private Integer isAutoPrint;

    @ApiModelProperty(value = "打印次数")
    private Integer printNum;

    @ApiModelProperty(value = "配送区域")
    private String region;

    @ApiModelProperty(value = "是否开票0不开票，1要开票")
    @JsonProperty(value = "isInv")
    private Integer isInv;

    @ApiModelProperty(value = "主体ID")
    private Integer companyId;

    @ApiModelProperty(value = "主体名称")
    private String companyName;

    @ApiModelProperty(value = "报关单号")
    private String customsNo;

    @ApiModelProperty(value = "报关日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime customsDate;

    @ApiModelProperty(value = "包装方式")
    private String packagesType;

    @ApiModelProperty(value = "是否贴标")
    private Integer isLabel;

    @ApiModelProperty(value = "标签要求")
    private String labelRemark;

    @ApiModelProperty(value = "箱单 要求")
    private String packagesRemark;

    @ApiModelProperty(value = "快递发货类别")
    @JsonProperty(value = "sfType")
    private String sfType;

    @ApiModelProperty(value = "快递付款方式")
    @JsonProperty(value = "sfPay")
    private String sfPay;

    @ApiModelProperty(value = "快递备注")
    @JsonProperty(value = "sfRemark")
    private String sfRemark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核人")
    @JsonProperty(value = "fMultiLevel0")
    private String fMultiLevel0;

    @ApiModelProperty(value = "审核 时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "fDateTime0")
    private LocalDateTime fDateTime0;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "出库状态（0未出库，1已安排，2已出库，3已完成）")
    private Integer stateFlag;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "出库订单明细集合")
    private List<HubShippingEntryVO> addHubShippingEntryFormList;

}
