package com.jayud.finance.bo;


import com.jayud.common.exception.JayudBizException;
import com.jayud.finance.vo.InitComboxStrVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 暂存/提交对账单
 */
@Data
public class CreatePaymentBillForm {

    @ApiModelProperty(value = "应付出账单界面部分,生成账单时必传")
    private OrderPaymentBillForm paymentBillForm;

    @Valid
    @ApiModelProperty(value = "应付出账单详情界面部分", required = true)
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;

    @ApiModelProperty(value = "账单编号,生成账单时必传")
    private String billNo;

    @ApiModelProperty(value = "核算期,生成账单时必传")
    private String accountTermStr;

    @ApiModelProperty(value = "结算币种,生成账单时必传")
    private String settlementCurrency;

    @ApiModelProperty(value = "账单类别,生成账单时必传,只允许填写main or zgys or bg or ky")
    private String subType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单", required = true)
    @Pattern(regexp = "(pre_create|create)", message = "只允许填写pre_create or create")
    private String cmd;

    @ApiModelProperty(value = "登录用户", required = true)
    private String loginUserName;

    @ApiModelProperty(value = "是否自定义汇率", required = true)
    private Boolean isCustomExchangeRate = false;

    @ApiModelProperty(value = "自定义汇率")
    private List<InitComboxStrVO> customExchangeRate;

    /**
     * 校验出账单参数
     */
    public void checkCreateReceiveBill() {

        //校验自定义汇率
        if (isCustomExchangeRate) {
            if (CollectionUtils.isEmpty(customExchangeRate)) {
                throw new JayudBizException(400, "请配置自定义汇率");
            }
        }
    }
}
