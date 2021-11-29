package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 调度配送 新增form表单
 * </p>
 *
 * @author LLJ
 * @since 2021-11-12
 */
@Data
public class AddHubShippingDeliverForm {

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
    @NotNull(message = "仓库名称不为空")
    private String hubName;

    @ApiModelProperty(value = "配送人")
    @NotNull(message = "配送人不为空")
    private String deliverName;

    @ApiModelProperty(value = "调度出库日期")
    @NotNull(message = "配送日期不为空")
    private String deliverTime;

    @ApiModelProperty(value = "车牌")
    @NotNull(message = "车牌不为空")
    private String truckNo;

    @ApiModelProperty(value = "运输方式（空运，陆运)")
    @NotNull(message = "运输方式不为空")
    private String transType;

    @ApiModelProperty(value = "供应商ID")
    @NotNull(message = "供应商ID不为空")
    private Integer supplierId;

    @ApiModelProperty(value = "物流承运商")
    @NotNull(message = "物流承运商不为空")
    private String supplierName;

    @ApiModelProperty(value = "承运单号")
    @NotNull(message = "承运单号不为空")
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
    @NotNull(message = "司机不为空")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @NotNull(message = "司机电话不为空")
    private String driverTel;

    @ApiModelProperty(value = "证件号码")
    @NotNull(message = "证件号码不为空")
    private String idCode;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "车牌ID")
    private Integer truckNoId;

    @ApiModelProperty(value = "司机ID")
    private Integer diverId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "出库单号")
    private String shippingNo;

    @ApiModelProperty(value = "提验货单号")
    private String checkNo;


    public String checkParam(){
//        if(this.idCode == null){
//            return "司机证件号码不能为空";
//        }
        if(this.driverName == null){
            return "司机姓名不能为空";
        }
        if(this.driverTel == null){
            return "司机电话不能为空";
        }
        if(this.truckNo == null){
            return "车牌不能为空";
        }
        if(this.deliverTime == null){
            return "配送时间不能为空";
        }
        if(this.deliverName == null){
            return "配送人不能为空";
        }
        return null;
    }

}
