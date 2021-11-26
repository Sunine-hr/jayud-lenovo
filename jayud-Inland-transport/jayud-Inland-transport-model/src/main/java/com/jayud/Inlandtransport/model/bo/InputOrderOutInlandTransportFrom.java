package com.jayud.Inlandtransport.model.bo;


import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.exception.JayudBizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 传入 外部调用部分 参数 表头  接受参数
 */
@Data
@Slf4j
public class InputOrderOutInlandTransportFrom {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键ID")
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单编号(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "内陆订单编号")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)")
    private String vehicleSize;

    @ApiModelProperty(value = "外部调用标识(1类型1  2类型2)")
    private Integer type;
//
//    @ApiModelProperty(value = "提货时间 pickUp（yyyyMMddHHmmss）")
//    private String pickUpDate;    //DateOfDelivery
//
//    @ApiModelProperty(value = "提货时间 pickUp（yyyyMMddHHmmss）")
//    private String pickUpDate;  //deliveryDate


    /**
     * 根据订车类型判断车型(1吨车 2柜车)
     * @return
     */
    public int getVehicleTypeVal() {
        if (!StringUtil.isNullOrEmpty(this.vehicleSize)) {
            if (this.vehicleSize.endsWith("T")) {
                return 1;
            } else if (this.vehicleSize.endsWith("GP") || this.vehicleSize.endsWith("HQ")) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 订单信息校验
     */
    public void checkOrderTransportParam() {
        StringBuffer sb = new StringBuffer();
        boolean isSuccess = true;
        if (StringUtil.isNullOrEmpty(this.getOrderNo())) {
            sb.append("第三方订单号").append("参数不能为空").append(",");
            isSuccess = false;
        }

        if (!isSuccess) {
            String msg = sb.substring(0, sb.length() - 1);
            log.warn(msg);
            throw new JayudBizException(msg);
        }
    }

    /**
     * 提货/送货消息校验
     * @param takeAdrFormsList
     * @return
     */
    private boolean checkOrderTakeAdrForm(List<InputOrderOutTakeAdrForm> takeAdrFormsList) {
        for (InputOrderOutTakeAdrForm orderTakeAdrForm : takeAdrFormsList) {
            if (StringUtils.isAnyBlank(orderTakeAdrForm.getProvinceName(),
                    orderTakeAdrForm.getAreaName(),
                    orderTakeAdrForm.getCityName(),
                    orderTakeAdrForm.getAddress(),
                    orderTakeAdrForm.getGoodsDesc(),
                    orderTakeAdrForm.getContacts(),
                    orderTakeAdrForm.getPhone())) {
                return true;
            }

            if (orderTakeAdrForm.getWeight() == null || orderTakeAdrForm.getPieceAmount() == null) {
                return true;
            }
        }
        return false;
    }

}
