package com.jayud.oms.model.bo;

import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


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

    @ApiModelProperty(value = "内陆")
    private InputOrderInlandTransportForm orderInlandTransportForm;

    @ApiModelProperty(value = "拖车")
    private List<InputTrailerOrderFrom> trailerOrderFrom;

    @ApiModelProperty(value = "入库")
    private  InputStorageInputOrderForm storageInputOrderForm;

    @ApiModelProperty(value = "出库")
    private  InputStorageOutOrderForm storageOutOrderForm;

    @ApiModelProperty(value = "快进快出")
    private  InputStorageFastOrderForm storageFastOrderForm;

    @ApiModelProperty(value = "登录人")
    private String loginUserName;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;

    @ApiModelProperty(value = "(0:本系统,2:scm)")
    private Integer createUserType;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "外部调用标识(1类型1  2类型2)")
    private Integer type;
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

    /**
     * 报关单号校验
     */
    public void checkCreateParam() {
        InputMainOrderForm orderForm = this.orderForm;

        //内陆
        if (OrderStatusEnum.NLYS.getCode().equals(orderForm.getClassCode())
                || orderForm.getSelectedServer().contains(OrderStatusEnum.NLDD.getCode())) {
            this.orderInlandTransportForm.checkCreateOrder();
        }
    }

}
