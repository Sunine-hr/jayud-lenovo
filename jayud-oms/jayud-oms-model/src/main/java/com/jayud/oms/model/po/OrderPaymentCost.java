package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单对应应付费用明细
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderPaymentCost对象", description="订单对应应付费用明细")
public class OrderPaymentCost extends Model<OrderPaymentCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "业务订单号")
    private String bizOrderNo;

    @ApiModelProperty(value = "实际产生业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "服务类型(product_biz code)")
    private String bizCode;

    @ApiModelProperty(value = "服务名(product_biz name)")
    private String bizName;

    @ApiModelProperty(value = "客户code(customer_info code)")
    private String customerCode;

    @ApiModelProperty(value = "客户名(customer_info name)")
    private String customerName;

    @ApiModelProperty(value = "费用类型ID")
    private Long costTypeId;

    @ApiModelProperty(value = "费用类型")
    private Long costGenreId;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "收费项目code")
    private String costCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量")
    private BigDecimal number;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态(0-草稿 1-审核通过 2-审核驳回)")
    private Integer status;

    @ApiModelProperty(value = "操作人")
    private String optName;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime optTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "附件路径,多个时用逗号分隔")
    private String files;

    @ApiModelProperty(value = "附件名称,多个时用逗号分隔")
    private String fileName;

    @ApiModelProperty(value = "是否出账 1-已出账 0-未出账")
    private String isBill;

    @ApiModelProperty(value = "是否汇总到主订单")
    private Boolean isSumToMain;

    @ApiModelProperty(value = "录用费用类型(zgys=中港运输)")
    private String subType;

    @ApiModelProperty(value = "暂存绑定账单号")
    private String tmpBillNo;

    @ApiModelProperty(value = "卸货地址")
    private String unloadingAddress;

    @ApiModelProperty(value = "子订单法人主体名字")
    private String legalName;

    @ApiModelProperty(value = "子订单法人主体id")
    private Long legalId;

    @ApiModelProperty(value = "应收费用id")
    private Long receivableId;

    @ApiModelProperty(value = "指定的供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "操作部门id/主订单所属部门id")
    private Long departmentId;

    @ApiModelProperty(value = "是否内部费用")
    private Boolean isInternal;

    @ApiModelProperty(value = "内部部门id")
    private Long internalDepartmentId;

    @ApiModelProperty(value = "订单结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "订单结算单位")
    private String unitName;

    @ApiModelProperty(value = "司机费用id")
    private Long driverCostId;

    @ApiModelProperty(value = "是否实报实销")
    private Boolean isReimbursement;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
