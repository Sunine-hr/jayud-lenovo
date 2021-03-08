package com.jayud.Inlandtransport.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendCarForm extends Model<SendCarForm> {

    @ApiModelProperty(value = "主键,修改时需要")
    private Long id;

    @ApiModelProperty(value = "子订单ID", required = true)
    @JsonIgnore
    private Long orderId;

    @ApiModelProperty(value = "运输订单号", required = true)
    private String transportNo;

    @ApiModelProperty(value = "运输对应子订单", required = true)
    @JsonIgnore
    private String orderNo;

    @ApiModelProperty(value = "车型(1吨车 2柜车)", required = true)
    @JsonIgnore
    private Integer vehicleType = 1;

    @ApiModelProperty(value = "车型(例:3T)", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "车牌号", required = true)
    private String licensePlate;

    @ApiModelProperty(value = "车型id", required = true)
    private Long vehicleId;

    @ApiModelProperty(value = "司机姓名", required = true)
    private String driverName;

    @ApiModelProperty(value = "司机电话", required = true)
    private String driverPhone;

    @ApiModelProperty(value = "供应商", required = true)
    private String supplierName;

    @ApiModelProperty(value = "供应商id", required = true)
    private String supplierId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审核意见")
    private String describes;

    public void checkSendCarOptParam() {
        String msg = "必填";
        if (StringUtils.isEmpty(this.vehicleSize)) {
            throw new JayudBizException("车型尺寸" + msg);
        }
        if (this.vehicleId == null) {
            throw new JayudBizException("请选择车牌");
        }
        if (StringUtils.isEmpty(this.driverName)) {
            throw new JayudBizException("司机姓名" + msg);
        }
        if (StringUtils.isEmpty(this.driverPhone)) {
            throw new JayudBizException("司机电话" + msg);
        }
        if (StringUtils.isEmpty(this.supplierName)) {
            throw new JayudBizException("车辆供应商" + msg);
        }
        if (StringUtils.isEmpty(this.supplierId)) {
            throw new JayudBizException("供应商id" + msg);
        }
    }
}
