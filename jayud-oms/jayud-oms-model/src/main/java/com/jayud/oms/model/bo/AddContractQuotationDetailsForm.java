package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.aop.annotations.FieldLabel;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
public class AddContractQuotationDetailsForm extends Model<AddContractQuotationDetailsForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "合同报价id")
    private Long contractQuotationId;

    @ApiModelProperty(value = "类型(1:整车 2:其他)")
    @FieldLabel(name = "类型", mappingString = "1:整车,2:其他")
    private Integer type;

    @ApiModelProperty(value = "起始地")
    @FieldLabel(name = "起始地")
    private String startingPlace;

    @ApiModelProperty(value = "目的地")
    @FieldLabel(name = "目的地")
    private String destination;

    @ApiModelProperty(value = "起始地Id")
    private String startingPlaceId;

    @ApiModelProperty(value = "目的地id")
    private String destinationId;

    @ApiModelProperty(value = "起始地Id")
    private List<Long> startingPlaceIds;

    @ApiModelProperty(value = "目的地id")
    private List<Long> destinationIds;

    @ApiModelProperty(value = "车型(3T 5T 8T 10T 12T 20GP 40GP 45GP)")
    @FieldLabel(name = "车型尺寸")
    private String vehicleSize;

    @ApiModelProperty(value = "费用名称code")
    @FieldLabel(name = "费用名称")
    private String costCode;

    @ApiModelProperty(value = "单价")
    @FieldLabel(name = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单位")
    @FieldLabel(name = "单位")
    private String unit;

    @ApiModelProperty(value = "币种代码")
    @FieldLabel(name = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "费用类别(作业环节)")
    @FieldLabel(name = "费用类别")
    private Long costTypeId;

    public static void main(String[] args) {
        System.out.println(Utilities.printCheckCode(AddContractQuotationDetailsForm.class));
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice.setScale(4);
    }

    public void setStartingPlaceIds(List<Long> startingPlaceIds) {
        this.startingPlaceIds = startingPlaceIds;
        if (!CollectionUtils.isEmpty(startingPlaceIds)) {
            StringBuilder sb = new StringBuilder();
            for (Long startPlaceId : startingPlaceIds) {
                sb.append(startPlaceId).append(",");
            }
            startingPlaceId = sb.toString();
        }
    }

    public void setDestinationIds(List<Long> destinationIds) {
        this.destinationIds = destinationIds;
        if (!CollectionUtils.isEmpty(destinationIds)) {
            StringBuilder sb = new StringBuilder();
            for (Long destinationId : destinationIds) {
                sb.append(destinationId).append(",");
            }
            destinationId = sb.toString();
        }
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void checkParam() {
        switch (SubOrderSignEnum.getEnum(subType)) {
            case ZGYS:
                if (type == null) {
                    throw new JayudBizException(400, "类型不能为空");
                } else if (type == 1) {
                    if (StringUtils.isEmpty(startingPlace)) throw new JayudBizException(400, "起始地不能为空");
                    if (StringUtils.isEmpty(destination)) throw new JayudBizException(400, "目的地不能为空");
                    if (StringUtils.isEmpty(vehicleSize)) throw new JayudBizException(400, "车型尺寸不能为空");
                }
                break;
            case BG:

                break;
        }

        if (StringUtils.isEmpty(costCode)) {
            throw new JayudBizException(400, "费用名称不能为空");
        }
        if (unitPrice == null) {
            throw new JayudBizException(400, "单价不能为空");
        }
        if (StringUtils.isEmpty(currencyCode)) {
            throw new JayudBizException(400, "币种代码不能为空");
        }
        if (costTypeId == null) {
            throw new JayudBizException(400, "作业环节不能为空");
        }

    }
}
