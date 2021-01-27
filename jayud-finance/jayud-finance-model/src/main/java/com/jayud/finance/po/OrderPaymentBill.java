package com.jayud.finance.po;

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
 * 应付出账单
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderPaymentBill对象", description="")
public class OrderPaymentBill extends Model<OrderPaymentBill> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应付出账单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "供应商")
    private String supplierChName;

    @ApiModelProperty(value = "已出账金额(人民币)")
    private BigDecimal alreadyPaidAmount;

    @ApiModelProperty(value = "已出账订单数")
    private Integer billOrderNum;

    @ApiModelProperty(value = "账单数")
    private Integer billNum;

    @ApiModelProperty(value = "如果是已子订单维度出账的,则记录具体的子订单类型")
    private String subType;

    @ApiModelProperty(value = "是否以主订单维度出账的")
    private Boolean isMain;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "法人主体id")
    private Long legalEntityId;

    @ApiModelProperty(value = "供应商code")
    private String supplierCode;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
