package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@ApiModel(value = "ContractQuotationDetails对象", description = "合同报价详情")
public class AddContractQuotationDetailsForm extends Model<AddContractQuotationDetailsForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "子订单类型")
    private String subType = SubOrderSignEnum.ZGYS.getSignOne();

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

    public static void main(String[] args) {
        System.out.println(Utilities.printCheckCode(AddContractQuotationDetailsForm.class));
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void checkParam() {
        if (type == null) {
            throw new JayudBizException(400, "类型不能为空");
        }
        if (StringUtils.isEmpty(startingPlace)) {
            throw new JayudBizException(400, "起始地不能为空");
        }
        if (StringUtils.isEmpty(destination)) {
            throw new JayudBizException(400, "目的地不能为空");
        }
        if (StringUtils.isEmpty(vehicleSize)) {
            throw new JayudBizException(400, "车型不能为空");
        }
        if (StringUtils.isEmpty(costCode)) {
            throw new JayudBizException(400, "费用名称不能为空");
        }
        if (unitPrice == null) {
            throw new JayudBizException(400, "单价不能为空");
        }
//        if (StringUtils.isEmpty(unit)) {
//            throw new JayudBizException(400, "单位不能为空");
//        }
//        if (StringUtils.isEmpty(currencyCode)) {
//            throw new JayudBizException(400, "币种代码不能为空");
//        }
        if (costTypeId == null) {
            throw new JayudBizException(400, "作业环节不能为空");
        }

    }
}
