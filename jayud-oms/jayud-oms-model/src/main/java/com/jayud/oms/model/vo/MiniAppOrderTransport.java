package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 小程序中港运输订单
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class MiniAppOrderTransport  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中港订单id")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "中港订单编号")
    private String orderNo;

    @ApiModelProperty(value = "口岸")
    private String portCode;

    @ApiModelProperty(value = "状态")
    private String status;

//    @ApiModelProperty(value = "1-装货 0-不需要装货")
//    private String isLoadGoods;
//
//    @ApiModelProperty(value = "1-装货 0-不需要装货")
//    private String isUnloadGoods;

    @ApiModelProperty(value = "结算单位")
    private String unitCode;


    @ApiModelProperty(value = "香港清关结算单位,选择了香港清关必填")
    private String hkUnitCode;

    @ApiModelProperty(value = "香港清关接单法人,选择了香港清关必填")
    private String hkLegalName;

    @ApiModelProperty(value = "是否香港清关 1-是 0-否,选择了香港清关必填")
    private String isHkClear;

    @ApiModelProperty(value = "车辆供应商ID")
    private Long hkSupplierId;

    @ApiModelProperty(value = "香港清关司机名称")
    private String hkDriverName;

    @ApiModelProperty(value = "香港清关司机电话")
    private String hkDriverPhone;

    @ApiModelProperty(value = "香港清关大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "香港清关香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "香港清关")
    private String clearCustomsNo;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime jiedanTime;

    @ApiModelProperty(value = "过磅数")
    private Double carWeighNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建用户")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "创建用户")
    private String updatedUser;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "通关时间")
    private LocalDateTime goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private LocalDateTime preGoCustomsTime;


}
