package com.jayud.finance.bo;


import com.jayud.common.vaildator.DateTime;
import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 应付单表头信息
 *
 * @author william
 * @description
 * @Date: 2020-04-25 11:26
 */
@Data
public class PayableHeaderForm {
    /**
     * F_JYD_Text 业务单号（必填项，用于关联作业号）
     */
    @NotEmpty(message = "业务单号不允许为空")
    private String businessNo;

    /**
     * FSUPPLIERID 供应商（必填项）
     * <br>需要封装FNumber
     */
    @NotEmpty(message = "供应商名称不允许为空")
    private String supplierName;

    /**
     * FCURRENCYID 币别（必填项）
     * <br>需要封装FNumber
     */
    @NotEmpty(message = "币别代码不允许为空")
    private String currency;

    /**
     * FSETTLEORGID 结算组织（必填项）
     * <br>需要封装FNumber
     */
    @NotEmpty(message = "组织名称不允许为空")
    private String settleOrgName;

    /**
     * 备注
     */
    private String remark;

    /**
     * FPURCHASEDEPTID 采购部门
     * <br>需要封装FNumber
     */
    @NotEmpty(message = "采购部门名称不允许为空")
    private String purchaseDeptName;

    /**
     * F_PCQE_Text  账单编号
     */
    private String billNo;

    /**
     * 费用明细
     */
    @NotEmpty(message = "明细项目不可为空")
    private List<APARDetailForm> entityDetail;

    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "业务日期结束格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String businessDate;
    /**
     * 本位币
     */
    private String baseCurrency;

    /**
     * 汇率
     */
    @NotNull(message = "汇率不能为空")
    private BigDecimal exchangeRate;

    /**
     * FBillTypeID 单据类型（必填项）默认
     * <br>需要封装FNumber
     */
    //private String billTypeIdPacked;
    /**
     * FISINIT 是否期初单据
     */
    //private boolean isInit;
    /**
     * FDATE 业务时间（必填项）
     */
    //private Date date;
    /**
     * FENDDATE_H 到期日期（必填项）
     */
    //private Date endDateH;
    /**
     * FDOCUMENTSTATUS 单据状态 (必填项)
     */
    //private String documentStatus;
    /**
     * FISPRICEEXCLUDETAX 价外税
     */
    //private boolean isPriceExcludeTax;
    /**
     * FBUSINESSTYPE 业务类型（必填项）
     */
    //private String FBUSINESSTYPE;
    /**
     * FISTAX 按含税单价录入
     */
    //private boolean isTax;

    /**
     * FPAYORGID 付款组织（必填项）
     * <br>需要封装FNumber
     */
    //private String payOrgId;
    /**
     * FSetAccountType 立账类型
     */
    //private String setAccountType;
    /**
     * FISTAXINCOST  税额计入成本
     */
    //private boolean isTaxInCost;

    /**
     * FISHookMatch 参与暂估应付核销
     */
    //private boolean isHookMatch;
    /**
     * FCancelStatus 作废状态（必填项）
     */
    //private String cancelStatus;
    /**
     * FISBYIV 是否发票审核自动生成
     */
    //private boolean FISBYIV;
    /**
     * FISGENHSADJ 是否需要成本调整
     */
    //private boolean FISGENHSADJ;
    /**
     * FISINVOICEARLIER 先到票后入库
     */
    //private boolean FISINVOICEARLIER;
    /**
     * FWBOPENQTY 反写开票数量
     */
    //private boolean FWBOPENQTY;

    /**
     * FsubHeadSuppiler 表头供应商
     * FORDERID 订货方
     * <br>需要封装FNumber
     */
    //private String orderIdPacked;
    /**
     * FsubHeadSuppiler 表头供应商
     * FTRANSFERID 供货方
     * <br>需要封装FNumber
     */
    //private String transferIdPacked;
    /**
     * FsubHeadSuppiler 表头供应商
     * FChargeId 收款方
     * <br>需要封装FNumber
     */
    //private String chargeIdPacked;

    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FACCNTTIMEJUDGETIME 到期日计算日期
     * <br>要填入FsubHeadFinc字段下</>
     */
    //private Date accntTimeJudgeTime;
    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FMAINBOOKSTDCURRID 本位币
     * <br>要填入FsubHeadFinc字段下
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    //private String mainBookStdCurridPacked;
    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FEXCHANGETYPE 汇率类型
     * <br>要填入FsubHeadFinc字段下
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    //private String exchangeType = "HLTX01_SYS";
    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FExchangeRate 汇率
     * <br>要填入FsubHeadFinc字段下
     */
    //private BigDecimal exchangeRate;
    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FTaxAmountFor 税额
     * <br>要填入FsubHeadFinc字段下
     */
    //private BigDecimal taxAmountFor;
    /**
     * 隶属于 FsubHeadFinc 表头财务字段的 FNoTaxAmountFor 不含税金额
     * <br>要填入FsubHeadFinc字段下
     */
    //private BigDecimal noTaxAmountFor;


    @Override
    public String toString() {
        return "PayableHeaderForm{" +
                "businessNo='" + businessNo + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", currency='" + currency + '\'' +
                ", settleOrgName='" + settleOrgName + '\'' +
                ", remark='" + remark + '\'' +
                ", purchaseDeptName='" + purchaseDeptName + '\'' +
                ", billNo='" + billNo + '\'' +
                ", entityDetail=" + entityDetail +
                ", businessDate='" + businessDate + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
