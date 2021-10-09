package com.jayud.scm.model.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 入库主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HubReceiving对象", description="入库主表")
public class HubReceiving extends Model<HubReceiving> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @ApiModelProperty(value = "入库单号")
    private String receivingNo;

    @ApiModelProperty(value = "业务类型（进口，出口等）")
    private String modelType;

    @ApiModelProperty(value = "入库类型（1.第一次，2.第二次）")
    private Integer rType;

    @ApiModelProperty(value = "入库模式0正常入库，1暂存入库")
    private Integer rModel;

    @ApiModelProperty(value = "订单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "订单编号")
    private String bookingNo;

    @ApiModelProperty(value = "提验货ID")
    private Integer checkId;

    @ApiModelProperty(value = "提验货单号")
    private String checkNo;

    @ApiModelProperty(value = "入仓号")
    private String pickupNo;

    @ApiModelProperty(value = "入库人")
    private String receivingUser;

    @ApiModelProperty(value = "入库日期")
    private LocalDateTime receivingDate;

    @ApiModelProperty(value = "入库模式")
    private String receivingMode;

    @ApiModelProperty(value = "仓库名称")
    private String hubName;

    @ApiModelProperty(value = "主体ID")
    private Integer companyId;

    @ApiModelProperty(value = "主体名称")
    private String companyName;

    @ApiModelProperty(value = "供应商Id")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务模式（代理进口等）")
    private String deputyStyle;

    @ApiModelProperty(value = "交易条件")
    private String incoterms;

    @ApiModelProperty(value = "运输方式")
    private String transModel;

    @ApiModelProperty(value = "总件数")
    private Integer totalPackages;

    @ApiModelProperty(value = "总毛重")
    private BigDecimal totalGw;

    @ApiModelProperty(value = "入库标签打印次数")
    private Integer labelPrint;

    @ApiModelProperty(value = "验货人")
    private String checkName;

    @ApiModelProperty(value = "验货开始时间")
    private LocalDateTime checkBeginTime;

    @ApiModelProperty(value = "验货结束时间")
    private LocalDateTime checkEndTime;

    @ApiModelProperty(value = "验货说明")
    private String checkRemark;

    @ApiModelProperty(value = "入库单状态（0：待验货，1：已验货，2：验货异常，3：已复核，4：已入库")
    private BigDecimal stateFlag;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "是否自动同步到在线")
    private BigDecimal isOnl;

    @ApiModelProperty(value = "客户订单号")
    private String contractNo;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "存仓周期（一个月，三个月）")
    private String hubDate;

    @ApiModelProperty(value = "储位")
    private String storage;

    @ApiModelProperty(value = "标记（0：否   1：是）")
    private Integer sign;

    @ApiModelProperty(value = "到货方式")
    private String deliveryMode;

    @ApiModelProperty(value = "预计出库日期")
    private LocalDateTime exDeTime;

    @ApiModelProperty(value = "登记费编号")
    private String registerNo;

    @ApiModelProperty(value = "停车费")
    private BigDecimal parkingAmt;

    @ApiModelProperty(value = "入仓费")
    private BigDecimal warehouseAmt;

    @ApiModelProperty(value = "理货费")
    private BigDecimal tallyingAmt;

    @ApiModelProperty(value = "登记费")
    private BigDecimal registerAmt;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否全部出库")
    private BigDecimal shippingAll;

    @ApiModelProperty(value = "审核人")
    private String fMultiLevel0;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime fDateTime0;

    private String fMultiLevel1;

    private LocalDateTime fDateTime1;

    private String fMultiLevel2;

    private LocalDateTime fDateTime2;

    private String fMultiLevel3;

    private LocalDateTime fDateTime3;

    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "备用字段1")
    private String temp01;

    @ApiModelProperty(value = "备用字段2")
    private String temp02;

    @ApiModelProperty(value = "备用字段3")
    private String temp03;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
