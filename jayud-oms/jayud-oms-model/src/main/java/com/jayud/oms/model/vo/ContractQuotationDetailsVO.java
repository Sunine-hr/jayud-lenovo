package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
@Accessors(chain = true)
@ApiModel(value = "ContractQuotationDetails对象", description = "合同报价详情")
public class ContractQuotationDetailsVO extends Model<ContractQuotationDetailsVO> {

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

    @ApiModelProperty(value = "起始地Id")
    private String startingPlaceId;

    @ApiModelProperty(value = "目的地id")
    private String destinationId;

    @ApiModelProperty(value = "起始地Id")
    private List<Long> startingPlaceIds;

    @ApiModelProperty(value = "目的地id")
    private List<Long> destinationIds;

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

    @ApiModelProperty(value = "币种名称")
    private String currency;

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

    @ApiModelProperty(value = "合同报价id")
    private Long contractQuotationId;

    @ApiModelProperty(value = "作业环节")
    private List<InitComboxVO> categorys;

    @ApiModelProperty(value = "标识某些字段不能不修改")
    private Boolean disable;

    @ApiModelProperty(value = "录用费用数量")
    private Integer number;

    @ApiModelProperty(value = "费用类型id")
    private Long costGenreId;

    @ApiModelProperty(value = "创建类型(1:其他,2:合同)")
    private Integer createType = 2;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void setType(Integer type) {
        this.type = type;
        if (type == null) return;
        switch (type) {
            case 1:
                disable = false;
                break;
            case 2:
                disable = true;
                break;
        }
    }

    public void setStartingPlaceId(String startingPlaceId) {
        this.startingPlaceId = startingPlaceId;
        if (!StringUtils.isEmpty(startingPlaceId)) {
            String[] split = startingPlaceId.split(",");
            this.startingPlaceIds = new ArrayList<>();
            for (String s : split) {
                this.startingPlaceIds.add(Long.valueOf(s));
            }
        }
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
        if (!StringUtils.isEmpty(destinationId)) {
            String[] split = destinationId.split(",");
            this.destinationIds = new ArrayList<>();
            for (String s : split) {
                this.destinationIds.add(Long.valueOf(s));
            }
        }
    }


    public boolean pairingTmsRule(List<String> startAddrs, List<String> endAddrs, String vehicleSize) {
        if (this.type == 2) {
            return true;
        }
        boolean isTrue = false;
        if (!this.vehicleSize.equals(vehicleSize)) {
            return isTrue;
        }
        String[] startingPlaceStrs = this.startingPlace.split(",");

        for (String startAddr : startAddrs) {
            for (String startingPlaceStr : startingPlaceStrs) {
                if (startAddr.contains(startingPlaceStr)) {
                    isTrue = true;
                }
            }
            if (isTrue) {
                break;
            }
        }
        if (!isTrue) {
            return false;
        } else {
            isTrue = false;
        }

        String[] destinationStrs = this.destination.split(",");
        for (String endAddr : endAddrs) {
            for (String destinationStr : destinationStrs) {
                if (endAddr.contains(destinationStr)) {
                    isTrue = true;
                }
            }
            if (isTrue) {
                break;
            }
        }

        return isTrue;
    }

}
