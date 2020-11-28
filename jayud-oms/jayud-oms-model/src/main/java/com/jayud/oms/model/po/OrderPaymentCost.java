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
    private Integer number;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
