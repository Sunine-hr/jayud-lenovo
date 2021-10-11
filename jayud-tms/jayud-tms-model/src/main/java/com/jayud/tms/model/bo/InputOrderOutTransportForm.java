package com.jayud.tms.model.bo;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.ApiResult;
import com.jayud.common.exception.JayudBizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
@Slf4j
public class InputOrderOutTransportForm {

    @ApiModelProperty(value = "第三方订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "业务类型", required = true)
    private String bizType;

    @ApiModelProperty(value = "通关口岸名称", required = true)
    private String portName;

    @ApiModelProperty(value = "订车类型(3T,5T,20GP,40GP等)", required = true)
    private String preTruckStyle;

    @ApiModelProperty(value = "中转仓库")
    private String warehouseInfo;

    @ApiModelProperty(value = "车次时间（yyyyMMddHHmmss）")
    private String truckDate;

    @ApiModelProperty(value = "是否车辆过磅")
    private Boolean isVehicleWeigh;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms2 = new ArrayList<>();

    /**
     * 根据业务类型判断货物流向，不匹配时报错
     * @return
     */
    public Integer getGoodsTypeVal() {

        if (!StringUtil.isNullOrEmpty(this.bizType)) {
            if ("进口".equals(this.bizType)) {
                return 1;
            } else if ("出口".equals(this.bizType)) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 根据订车类型判断车型(1吨车 2柜车)
     * @return
     */
    public int getVehicleTypeVal() {
        if (!StringUtil.isNullOrEmpty(this.preTruckStyle)) {
            if (this.preTruckStyle.endsWith("T")) {
                return 1;
            } else if (this.preTruckStyle.endsWith("GP") || this.preTruckStyle.endsWith("HQ")) {
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
        if (StringUtil.isNullOrEmpty(this.getBizType())) {
            sb.append("业务类型").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (!"进口".equals(this.getBizType()) && !"出口".equals(this.getBizType())) {
            sb.append("业务类型").append("不匹配").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getPortName())) {
            sb.append("通关口岸名称").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getPreTruckStyle())) {
            sb.append("订车类型").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getTruckDate())) {
            sb.append("车次时间").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (!Pattern.compile("^[0-9]{14}$").matcher(this.getTruckDate()).matches()) {
            sb.append("车次时间").append("格式不正确").append(",");
            isSuccess = false;
        }

        // 检查提货/送货地址
        if (CollectionUtil.isNotEmpty(this.getTakeAdrForms1()) ) {
            boolean isFail = checkOrderTakeAdrForm(this.getTakeAdrForms1());
            if (isFail) {
                isSuccess = false;
                sb.append("提货地址").append("存在遗漏参数").append(",");
            }
        }

        if (CollectionUtil.isNotEmpty(this.getTakeAdrForms2())) {
            boolean isFail = checkOrderTakeAdrForm(this.getTakeAdrForms2());
            if (isFail) {
                isSuccess = false;
                sb.append("送货地址").append("存在遗漏参数").append(",");
            }
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
