package com.jayud.tms.model.bo;

import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SendCarForm {

    @ApiModelProperty(value = "主键,修改时需要")
    private Long id;

    @ApiModelProperty(value = "子订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "主订单ID", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "运输订单号", required = true)
    private String transportNo;

    @ApiModelProperty(value = "运输对应子订单", required = true)
    private String orderNo;

    @ApiModelProperty(value = "车型(1吨车 2柜车)", required = true)
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T)", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "车辆ID", required = true)
    private Long vehicleId;

    @ApiModelProperty(value = "大陆司机名", required = true)
    private Long driverInfoId;

    @ApiModelProperty(value = "要求")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态,审核时传")
    private String status;

    @ApiModelProperty(value = "审核意见，审核时传")
    private String auditComment;

    @ApiModelProperty(value = "操作指令，区分派车sendCar/审核auditCar/驳回editCar", required = true)
    private String cmd;

    @ApiModelProperty(value = "骑师id")
    private Long jockeyId;

    public void checkSendCar() {
        //参数校验(旧的)
        if ((CommonConstant.EDIT_CAR.equals(this.getCmd()) && this.getId() == null) ||
                this.getOrderId() == null || this.getMainOrderId() == null ||
                StringUtil.isNullOrEmpty(this.getTransportNo()) || StringUtil.isNullOrEmpty(this.getOrderNo()) ||
                this.getVehicleSize() == null || this.getVehicleType() == null ||
                this.getVehicleId() == null || this.getDriverInfoId() == null) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
        //TODO 新加校验柜号 车型(1吨车 2柜车)
        if (this.vehicleType.equals(2) && StringUtils.isEmpty(this.cntrNo)) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }

    }
}
