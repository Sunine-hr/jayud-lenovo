package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 调度配送表
 * </p>
 *
 * @author LLJ
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HubShippingDeliver对象", description="调度配送表")
public class HubShippingDeliver extends Model<HubShippingDeliver> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "单号（配送，发货，自提系统单号）")
    private String deliverNo;

    @ApiModelProperty(value = "单据日期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "业务类型")
    private Integer modelType;

    @ApiModelProperty(value = "发货类型（0自提，1发货，2配送，3直送发货，4转仓，5存仓发货,6.暂放仓库）")
    private Integer deliverType;

    @ApiModelProperty(value = "仓库名称")
    private String hubName;

    @ApiModelProperty(value = "配送人")
    private String deliverName;

    @ApiModelProperty(value = "调度出库日期")
    private String deliverTime;

    @ApiModelProperty(value = "车牌")
    private String truckNo;

    @ApiModelProperty(value = "运输方式（空运，陆运)")
    private String transType;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "物流承运商")
    private String supplierName;

    @ApiModelProperty(value = "承运单号")
    private String supplierNo;

    @ApiModelProperty(value = "快递发货类别")
    private String sfType;

    @ApiModelProperty(value = "快递付款方式")
    private String sfPay;

    @ApiModelProperty(value = "快递备注")
    private String sfRemark;

    @ApiModelProperty(value = "自提人")
    private String takeUser;

    @ApiModelProperty(value = "自提人证件号码")
    private String takeUserCid;

    @ApiModelProperty(value = "总件数")
    private Integer totalPackages;

    @ApiModelProperty(value = "总重量")
    private BigDecimal totalGw;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核步长")
    private Integer fStep;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "发货状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    @ApiModelProperty(value = "司机")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverTel;

    @ApiModelProperty(value = "证件号码")
    private String idCode;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "车牌ID")
    private Integer truckNoId;

    @ApiModelProperty(value = "司机ID")
    private Integer diverId;

    @ApiModelProperty(value = "司机ID")
    private Integer pushOms;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
