package com.jayud.finance.bo;


import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.vo.InitComboxStrVO;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


@Data
public class EditSBillForm {

    @ApiModelProperty(value = "对账单编号", required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "被删除的费用集合", required = true)
    @NotNull(message = "delCosts is required")
    private List<OrderReceiveBillDetailForm> delCosts = new ArrayList<>();

    @ApiModelProperty(value = "新增的费用集合", required = true)
    @NotEmpty(message = "receiveBillDetailForms is required")
    private List<OrderReceiveBillDetailForm> receiveBillDetailForms = new ArrayList<>();

    @ApiModelProperty(value = "操作指令cmd=save保存 submit提交 cw_save财务暂存", required = true)
    @Pattern(regexp = "(save|submit|cw_save)", message = "只允许填写save or submit")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户", required = true)
    private String loginUserName;

    @ApiModelProperty(value = "核算期,生成账单时必传")
    private String accountTermStr;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "是否自定义汇率", required = true)
    private Boolean isCustomExchangeRate;

    @ApiModelProperty(value = "自定义汇率")
    private List<InitComboxStrVO> customExchangeRate;

    /**
     * 校验出账单参数
     */
    public void checkCreateReceiveBill() {
        //校验自定义汇率
        if (isCustomExchangeRate != null && isCustomExchangeRate) {
            if (CollectionUtils.isEmpty(customExchangeRate)) {
                throw new JayudBizException(400, "请配置自定义汇率");
            }
            if (customExchangeRate.stream().anyMatch(e -> StringUtils.isEmpty(e.getNote()))) {
                throw new JayudBizException(400, "请配置汇率");
            }
        }
    }

    /**
     * 校验编辑对账单
     */
    public void checkEditSBill() {
        //参数校验
        if (StringUtil.isNullOrEmpty(this.getBillNo()) || StringUtil.isNullOrEmpty(this.getCmd())
                || StringUtil.isNullOrEmpty(this.getLoginUserName())) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
    }
}
