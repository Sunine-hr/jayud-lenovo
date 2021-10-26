package com.jayud.oms.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同报价详情
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ContractQuotationDetails对象", description="合同报价详情")
public class ContractQuotationDetails extends Model<ContractQuotationDetails> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      private Long id;

    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "类型(1:整车 2:其他)")
    private Integer type;

    @ApiModelProperty(value = "起始地")
    private String startingPlace;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "车型(3T 5T 8T 10T 12T 20GP 40GP 45GP)")
    private String vehicleSize;

    @ApiModelProperty(value = "费用名称code")
    private String costCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "费用类别(作业环节)")
    private Long costTypeId;

    @ApiModelProperty(value = "状态（0禁用 1启用 2删除）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
