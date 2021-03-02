package com.jayud.oms.model.bo;

import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderForm {

    @ApiModelProperty(value = "主订单", required = true)
    private InputMainOrderForm orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsForm orderCustomsForm;

    @ApiModelProperty(value = "中港")
    private InputOrderTransportForm orderTransportForm;

    @ApiModelProperty(value = "空运")
    private InputAirOrderForm airOrderForm;

    @ApiModelProperty(value = "服务单")
    private InputOrderServiceForm orderServiceForm;

    @ApiModelProperty(value = "海运")
    private InputSeaOrderForm seaOrderForm;

    @ApiModelProperty(value = "拖车")
    private  InputTrailerOrderFrom trailerOrderFrom;

    @ApiModelProperty(value = "登录人")
    private String loginUserName;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;


    /**
     * 报关单号校验
     */
    public void checkCustomsParam() {
        String title = "报关:";
        if (this.orderCustomsForm == null) {
            throw new JayudBizException(title + "信息不能为空");
        }
        this.orderCustomsForm.checkCustomsInfoParam();
//        InputOrderCustomsForm inputOrderCustomsForm = this.orderCustomsForm;
//        if (inputOrderCustomsForm == null ||
//                StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortCode()) ||
//                StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortName()) ||
//                inputOrderCustomsForm.getGoodsType() == null ||
//                StringUtil.isNullOrEmpty(inputOrderCustomsForm.getBizModel()) ||
//                StringUtil.isNullOrEmpty(inputOrderCustomsForm.getLegalName()) ||
//                inputOrderCustomsForm.getLegalEntityId() == null ||
//                StringUtil.isNullOrEmpty(inputOrderCustomsForm.getEncode()) ||//六联单号
//                inputOrderCustomsForm.getSubOrders() == null) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//        }
    }
}
