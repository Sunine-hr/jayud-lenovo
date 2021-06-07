package com.jayud.oms.model.bo;

import com.jayud.common.enums.UnitEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.NumberUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


@Data
public class InputPaymentCostForm {

    @ApiModelProperty(value = "应付主键ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "应付客户名称用户自填改为了下拉供应商", required = true)
    @NotEmpty(message = "customerCode is required")
    private String customerCode;

    @ApiModelProperty(value = "费用类别", required = true)
    @NotEmpty(message = "costTypeId is required")
    private Long costTypeId;

    @ApiModelProperty(value = "应付项目CODE(费用名称)", required = true)
    @NotEmpty(message = "costCode is required")
    private String costCode;

    @ApiModelProperty(value = "费用类型", required = true)
    private Long costGenreId;

    @ApiModelProperty(value = "单位", required = true)
    private String unit;

    @ApiModelProperty(value = "单价", required = true)
    @NotEmpty(message = "unitPrice is required")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量", required = true)
    @NotEmpty(message = "number is required")
    private BigDecimal number;

    @ApiModelProperty(value = "币种CODE", required = true)
    @NotEmpty(message = "currencyCode is required")
    private String currencyCode;

    @ApiModelProperty(value = "应付金额", required = true)
    @NotEmpty(message = "amount is required")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率", required = true)
    @NotEmpty(message = "exchangeRate is required")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额", required = true)
    @NotEmpty(message = "changeAmount is required")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "卸货地址")
    private String unloadingAddress;

    @ApiModelProperty(value = "指定的供应商id")
    private Long supplierId;


    public void checkParam() {
        //校验单位匹配数量是否整数
        if (UnitEnum.getCode(this.unit) == null) {
            throw new JayudBizException("不存在该单位");
        }
        if (UnitEnum.CTNS.name().equals(this.unit)
                || UnitEnum.PCS.name().equals(this.unit)
                || UnitEnum.BILL.name().equals(this.unit)) {
            if (this.number != null && !NumberUtils.isIntegerValue(this.number)) {
                throw new JayudBizException("数量请输入整数（单位:CTNS/PCS/BILL)");
            }
        }
    }


}
