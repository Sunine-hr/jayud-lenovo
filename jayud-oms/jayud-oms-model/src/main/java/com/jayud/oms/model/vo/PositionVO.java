package com.jayud.oms.model.vo;

import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * GPS实时定位车辆返回对象
 */
@Data
public class PositionVO {

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "中港订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "货物流向描述")
    private String goodsTypeDesc;

    @ApiModelProperty("通关口岸CODE")
    private String portCode;

    @ApiModelProperty("结算单位CODE")
    private String unitCode;

    @ApiModelProperty("结算单位名称")
    private String unitName;

    @ApiModelProperty("接单法人")
    private String legalName;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "车型/柜号")
    private String modelAndCntrNo;

    @ApiModelProperty(value = "运输公司")
    private String supplierChName;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "司机香港电话")
    private String driverHkPhone;

    @ApiModelProperty(value = "总件数")
    private Integer totalAmount;

    @ApiModelProperty(value = "总重量")
    private Integer totalWeight;


    @ApiModelProperty(value = "对接GPS所需要的key值")
    private String appKey;

    @ApiModelProperty(value = "对接GPS公用路径前缀")
    private String gpsAddress;

    @ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    @ApiModelProperty(value = "车辆点火方向")
    private Integer accState;

    @ApiModelProperty(value = "行驶方向")
    private Integer direction;

    @ApiModelProperty(value = "经度")
    private Double latitude;

    @ApiModelProperty(value = "纬度")
    private Double longitude;

    @ApiModelProperty(value = "终端上报位置的时间")
    private String reportTime;

    @ApiModelProperty(value = "速度")
    private Double speed;

    @ApiModelProperty(value = "终端记录的总里程")
    private Double starkMileage;

    @ApiModelProperty(value = "提货地址货物信息")
    private String goodInfo;

    @ApiModelProperty(value = "地理位置")
    private String addr;

    @ApiModelProperty(value = "车辆状态")
    private String vehicleStatus;

    @ApiModelProperty(value = "行驶里程")
    private Double mile;

}
