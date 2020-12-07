package com.jayud.airfreight.model.bo;

import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 空运节点操作流程
 */
@Data
@Slf4j
public class AirProcessOptForm {


    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "空运订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "订舱信息")
    private AddAirBookingForm airBooking = new AddAirBookingForm();

    @ApiModelProperty(value = "代理公司id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "代理服务类型（0:清关,1:配送")
    private List<String> proxyServiceType;

//    @ApiModelProperty(value = "操作指令,cmd = 1-空运接单,2-空运订舱,3-订单入仓,4-确认提单, " +
//            "5-确认离港，6-确认到港,7-海外代理,8-确认签收", required = true)
//    private Integer cmd;

    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),ZGYS(2,中港运输) 前端不用管")
    private Integer businessType;

    @ApiModelProperty(value = "状态码,前台不用传")
    private String status;

    @ApiModelProperty(value = "状态名称,前台不用传")
    private String statusName;

    @ApiModelProperty(value = "附件,前台不用传")
    private String statusPic;

    @ApiModelProperty(value = "附件名称,前台不用传")
    private String statusPicName;

    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    public void checkProcessOpt(OrderStatusEnum statusEnum) {
        boolean pass = true;
        switch (statusEnum) {
            case AIR_A_1: //空运接单
            case AIR_A_3: //订单入仓
            case AIR_A_8: //签收
                pass = checkOptInfo();
                break;
            case AIR_A_2: //订舱
                pass = this.airBooking.checkBookingSpaceOptParam();
                break;
            case AIR_A_4:
                if (CollectionUtils.isEmpty(this.fileViewList)) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                pass = this.airBooking.checkConfirmLadingBillOptParam();
                break;
            case AIR_A_5:
                pass = this.airBooking.checkConfirmATDOptParam();
                break;
            case AIR_A_6:
                pass = this.airBooking.checkConfirmATAOptParam();
                break;
            case AIR_A_7:
                pass = this.checkOverseasAgencyOpt();
                break;
        }
        if (!pass) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
    }


    public boolean checkOverseasAgencyOpt() {
        String title = "海外代理";
        if (this.agentSupplierId == null) {
            log.warn(title + " 海外代理id必填");
            return false;
        }
        if (this.proxyServiceType == null) {
            log.warn(title + " 代理服务必填");
            return false;
        }
        return true;
    }

    public boolean checkOptInfo() {
        if (StringUtils.isEmpty(this.operatorUser)) {
            log.warn("操作人必填");
            return false;
        }
        if (StringUtils.isEmpty(this.operatorTime)) {
            log.warn("操作时间必填");
            return false;
        }
        return true;
    }
}
