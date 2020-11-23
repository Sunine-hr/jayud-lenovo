package com.jayud.finance.vo;

import com.jayud.finance.enums.BillEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账单数列表(应付应收一致)
 */
@Data
public class OrderPaymentBillNumVO {

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "开始核算期 年月日")
    private String beginAccountTermStr;

    @ApiModelProperty(value = "结束核算期 年月日")
    private String endAccountTermStr;

    @ApiModelProperty(value = "账单编号维度下的订单数")
    private Integer billNum;

    @ApiModelProperty(value = "人民币")
    private BigDecimal rmb;

    @ApiModelProperty(value = "美元")
    private BigDecimal dollar;

    @ApiModelProperty(value = "欧元")
    private BigDecimal euro;

    @ApiModelProperty(value = "港币")
    private BigDecimal hKDollar;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "已收金额，即财务已核销金额")
    private BigDecimal heXiaoAmount;

    @ApiModelProperty(value = "账单状态")
    private String billStatus;

    @ApiModelProperty(value = "账单状态")
    private String billStatusDesc;

    @ApiModelProperty(value = "制单人")
    private String makeUser;

    @ApiModelProperty(value = "制单时间")
    private String makeTimeStr;

    public String getBillStatusDesc() {
        if(!StringUtil.isNullOrEmpty(this.billStatus)){
            billStatusDesc = BillEnum.getDesc(this.billStatus);
        }
        return "";
    }


}
