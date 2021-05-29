package com.jayud.oms.model.bo;

import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;


@Data
public class InputCostForm {

    @ApiModelProperty(value = "主订单ID", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "子订单法人主体")
    private String subLegalName;

    @ApiModelProperty(value = "子订单结算单位CODE")
    private String subUnitCode;

    @ApiModelProperty(value = "应付费用", required = true)
    private List<InputPaymentCostForm> paymentCostList;

    @ApiModelProperty(value = "应收费用", required = true)
    private List<InputReceivableCostForm> receivableCostList;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit_main or submit_main or preSubmit_sub or submit_sub", required = true)
    private String cmd;

    @ApiModelProperty(value = "区分费用类型，主订单=main 中港订单=zgys 报关=bg" +
            "空运=ky 海运=hy 拖车=tc", required = true)
    private String subType;


    /**
     * 检查供应商录用费用
     */
    public void checkSupplierEntryFee() {
        if (this.mainOrderId == null) {
            throw new JayudBizException("参数不合法");
        }
        if (CollectionUtils.isEmpty(this.paymentCostList)) {
            throw new JayudBizException("请提交费用");
        }
        if (StringUtils.isEmpty(this.orderNo)
                || StringUtils.isEmpty(this.subLegalName)
                || StringUtils.isEmpty(this.subUnitCode)) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }

//        if ("preSubmit_main".equals(this.cmd) || "preSubmit_sub".equals(this.cmd)) {
//            for (InputPaymentCostForm paymentCost : this.paymentCostList) {
//                paymentCost.checkParam();
//            }
////            for (InputReceivableCostForm receivableCost : this.receivableCostList) {
////                receivableCost.checkUnitParam();
////            }
//        }

        if ("submit_main".equals(this.cmd) || "submit_sub".equals(this.cmd)) {
            for (InputPaymentCostForm paymentCost : this.paymentCostList) {
                if (StringUtils.isEmpty(paymentCost.getCostCode())) {
                    throw new JayudBizException("请填写费用名称");
                }
                if (paymentCost.getUnitPrice() == null) {
                    throw new JayudBizException("单价");
                }
                if (paymentCost.getNumber() == null) {
                    throw new JayudBizException("请填写数量");
                }
                if (StringUtils.isEmpty(paymentCost.getCurrencyCode())) {
                    throw new JayudBizException("请填写币种");
                }
                if (paymentCost.getAmount() == null) {
                    throw new JayudBizException("请填写应付金额");
                }
//                if (StringUtil.isNullOrEmpty(paymentCost.getCustomerCode())
//                        || StringUtil.isNullOrEmpty(paymentCost.getCostCode())
//                        || paymentCost.getCostTypeId() == null || paymentCost.getCostGenreId() == null
//                        || StringUtil.isNullOrEmpty(paymentCost.getUnit())
//                        || paymentCost.getExchangeRate() == null
//                        || paymentCost.getChangeAmount() == null) {
//                    return CommonResult.error(400, "参数不合法");
//                }
                paymentCost.checkParam();
            }

        }
    }
}
