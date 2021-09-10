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
import java.util.List;

/**
 * <p>
 * 提验货主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CheckOrder对象", description="提验货主表")
public class AddCheckOrderForm extends Model<AddCheckOrderForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @ApiModelProperty(value = "验货单号")
    private String checkNo;

    @ApiModelProperty(value = "验货类型下厂验货，仓库验货")
    private String checkType;

    @ApiModelProperty(value = "业务类型（进口，出口）")
    private Integer modelType;

    @ApiModelProperty(value = "工作单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "工作单号")
    private String bookingNo;

    @ApiModelProperty(value = "单据类型 0提货，1验货，2提货+验货")
    private Integer orderType;

    @ApiModelProperty(value = "运输方式")
    private String truckModel;

    @ApiModelProperty(value = "状态(0:未提交1:已提交2:已取消3:提货完成4:验货异常5:验货完成6:已入库7:异常已处理)")
    private String checkState;

    @ApiModelProperty(value = "提货时间")
    private String checkDateTime;

    @ApiModelProperty(value = "验货人")
    private String checkUser;

    @ApiModelProperty(value = "验货人电话")
    private String checkTel;

    @ApiModelProperty(value = "验货人证件号码")
    private String cardNumber;

    @ApiModelProperty(value = "提货车牌")
    private String truckNo;

    @ApiModelProperty(value = "总毛重")
    private BigDecimal totalGw;

    @ApiModelProperty(value = "总体积")
    private BigDecimal totalCbm;

    @ApiModelProperty(value = "总件数")
    private BigDecimal totalPackages;

    @ApiModelProperty(value = "是否总板货")
    private Integer isPallet;

    @ApiModelProperty(value = "总板数")
    private Integer totalPallet;

    @ApiModelProperty(value = "包装方式")
    private String packagesType;

    @ApiModelProperty(value = "客户联系人")
    private String customerUser;

    @ApiModelProperty(value = "客户联系电话")
    private String customerUserTel;

    @ApiModelProperty(value = "提货单号")
    private String pickNo;

    @ApiModelProperty(value = "提货联系人")
    private String pickUpUser;

    @ApiModelProperty(value = "提货联系电话")
    private String pickUpTel;

    @ApiModelProperty(value = "提货地址")
    private String pickAddress;

    @ApiModelProperty(value = "提验货说明")
    private String checkRemark;

    @ApiModelProperty(value = "仓库Id")
    private BigDecimal warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "配送单Id")
    private Integer shippingDeliverId;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplerName;

    @ApiModelProperty(value = "验货出发时间")
    private String startDate;

    @ApiModelProperty(value = "到达时间")
    private String arriveDate;

    @ApiModelProperty(value = "离厂时间")
    private String leaveDate;

    @ApiModelProperty(value = "到仓时间")
    private String toWarehouseTime;

    @ApiModelProperty(value = "验货完成时间")
    private String checkEndTime;

    @ApiModelProperty(value = "优先级别abc")
    private String receiptPriority;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "提验货详情集合")
    private List<AddCheckOrderEntryForm> bookingOrderEntryList;

}
