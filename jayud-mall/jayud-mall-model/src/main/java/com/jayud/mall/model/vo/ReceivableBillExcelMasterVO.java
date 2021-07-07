package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ReceivableBillExcelMasterVO {

    //客户-结算法人主体
    @ApiModelProperty(value = "客户-结算法人主体")
    private String legalName;

    //客户-结算法人主体英文名
    @ApiModelProperty(value = "客户-结算法人主体英文名")
    private String legalEnName;

    //账单编号
    @ApiModelProperty(value = "账单编号(多个)")
    private String billCodes;

    //账单日期

    //到期日期

    //客户名称
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    //费用明细
    @ApiModelProperty(value = "费用明细")
    private List<ReceivableBillDetailExcelVO> receivableBillDetailList;

    //人民币总计
    @ApiModelProperty(value = "金额汇总(要分币种)")
    private List<BillAmountTotalVO> billAmountTotalList;

    //户名
    @ApiModelProperty(value = "户名")
    private String bankAccountName;

    //银行账号
    @ApiModelProperty(value = "银行账号")
    private String accountOpen;

    //开户银行
    @ApiModelProperty(value = "开户银行")
    private String bank;


}
