package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.scm.model.bo.AddAddressForm;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 港车运输主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HgTruckApiVO {

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "车次编号")
    private String orderNo;

    @ApiModelProperty(value = "预订车次类型")
    private String preTruckStyle;

    @ApiModelProperty(value = "起运地")
    private String origin;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "进/出口岸")
    private String portName;

    @ApiModelProperty(value = "中转仓")
    private String warehouseInfo;

    @ApiModelProperty(value = "是否过磅")
    private Boolean isVehicleWeigh;

    @ApiModelProperty(value = "车次日期")
    private String truckDate;

    @ApiModelProperty(value = "提货地址")
    private List<AddAddressForm> takeAdrForms1;

    @ApiModelProperty(value = "送货地址")
    private List<AddAddressForm> takeAdrForms2;

}
