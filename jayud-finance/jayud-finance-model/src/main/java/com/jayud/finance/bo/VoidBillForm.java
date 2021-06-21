package com.jayud.finance.bo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.CancelAfterVerification;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 作废账单
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VoidBillForm {

    @ApiModelProperty(value = "账单编号")
    @NotEmpty(message = "请输入账单编号")
    private String billNo;

    @ApiModelProperty(value = "法人主体")
    @NotEmpty(message = "请输入法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户,应收时取值")
    private String unitAccount;

    @ApiModelProperty(value = "供应商,应付时取值")
    private String supplierChName;

    @ApiModelProperty(value = "核算期 年月")
    private String accountTermStr;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

//    @ApiModelProperty(value = "审核状态描述")
//    private String auditStatusDesc;

    @ApiModelProperty(value = "付款申请")
    private String applyStatus;

    @ApiModelProperty(value = "生成账单人")
    private String makeUser;

    @ApiModelProperty(value = "生成账单时间")
    private String makeTimeStr;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private String auditTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "(应付:付款金额,应收:开票金额)")
    private BigDecimal paymentAmount = new BigDecimal(0);

    @ApiModelProperty(value = "费用金额")
    private String amountStr;

    @ApiModelProperty(value = "费用类型(0-应收, 1-应付)")
    private Integer costType;


    public void checkVoidBill() {
        if (BillTypeEnum.getEnum(costType) == null) {
            throw new JayudBizException("没有这个费用类型");
        }
        if (!this.auditStatus.equals(BillEnum.B_1.getCode())
                && !this.auditStatus.equals(BillEnum.B_2_1.getCode())) {
            throw new JayudBizException("只有待经理审核和审核不通过才能作废账单");
        }
    }
}
