package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.enums.ContractQuotationModeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * CrmContractQuotationDetails 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="合同报价详情对象", description="合同报价详情")
public class AddCrmContractQuotationDetailsForm extends SysBaseEntity {


    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "合同报价id")
    private Long contractQuotationId;

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

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "起始地id(多个逗号隔开)")
    private String startingPlaceId;

    @ApiModelProperty(value = "目的地id(多个逗号隔开)")
    private String destinationId;

    @ApiModelProperty(value = "起始地Id")
    private List<Long> startingPlaceIds;

    @ApiModelProperty(value = "目的地id")
    private List<Long> destinationIds;

    @ApiModelProperty(value = "重量计费/kg")
    private BigDecimal weightBilling;

    @ApiModelProperty(value = "件数计费/件")
    private BigDecimal numBilling;

    @ApiModelProperty(value = "板数计费/版")
    private BigDecimal plateNumBilling;

    @ApiModelProperty(value = "最低计费")
    private BigDecimal minBilling;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice==null) return;
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


    public void checkParam() {
        switch (ContractQuotationModeEnum.getEnum(subType)) {
            case ZGYS:
                if (type == null) {
                    throw new JayudBizException(400, "类型不能为空");
                } else if (type == 1) {
                    if (StringUtils.isEmpty(startingPlace)) throw new JayudBizException(400, "起始地不能为空");
                    if (StringUtils.isEmpty(destination)) throw new JayudBizException(400, "目的地不能为空");
                    if (StringUtils.isEmpty(vehicleSize)) throw new JayudBizException(400, "车型尺寸不能为空");
                }
                if (unitPrice == null) {
                    throw new JayudBizException(400, "单价不能为空");
                }
                break;
            case BG:
                if (unitPrice == null) {
                    throw new JayudBizException(400, "单价不能为空");
                }
            case HKPS:
                if (this.weightBilling==null){
                    throw new JayudBizException(400, "香港配送重量计费不能为空");
                }
                if (this.numBilling==null){
                    throw new JayudBizException(400, "香港配送件数计费不能为空");
                }
                if (this.plateNumBilling==null){
                    throw new JayudBizException(400, "香港配送板数计费不能为空");
                }
                if (this.minBilling==null){
                    throw new JayudBizException(400, "香港配送最低计费不能为空");
                }
                break;
        }

        if (StringUtils.isEmpty(costCode)) {
            throw new JayudBizException(400, "费用名称不能为空");
        }
        if (StringUtils.isEmpty(currencyCode)) {
            throw new JayudBizException(400, "币种代码不能为空");
        }
        if (costTypeId == null) {
            throw new JayudBizException(400, "作业环节不能为空");
        }

    }


}
