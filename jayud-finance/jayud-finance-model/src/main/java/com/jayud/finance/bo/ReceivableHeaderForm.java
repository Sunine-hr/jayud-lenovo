package com.jayud.finance.bo;

import com.jayud.common.vaildator.DateTime;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 应收单需要传入的表头信息
 *
 * @author william
 * @description
 * @Date: 2020-04-22 14:40
 */
@Data
public class ReceivableHeaderForm {
    //TODO 模板为暂存的单据状态，可能需要改变
    /**
     * FCUSTOMERID 客户
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "客户名称不允许为空")
    private String customerName;

    /**
     * FCUSTOMERID 币别
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "币别代码不允许为空")
    private String currency;

    /**
     * FSETTLEORGID 结算组织
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "组织名称不允许为空")
    private String settleOrgName;

    /**
     * FSALEDEPTID 销售部门
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    @NotEmpty(message = "销售部门名称不允许为空")
    private String saleDeptName;

    /**
     * FAR_Remark 业务单号
     */
    @NotEmpty(message = "业务单号不允许为空")
    private String businessNo;

    /**
     * F_JYD_Remarks 备注
     */
    private String remark;

    /**
     * F_PCQE_Text  账单编号
     */
    private String billNo;

    /**
     * 明细数据列表
     */
    @NotEmpty(message = "明细项目不可为空")
    private List<APARDetailForm> entityDetail = new ArrayList<>();

    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "业务日期结束格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String businessDate;
    /**
     * 本位币
     */
    private String baseCurrency;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * FDATE 业务日期（当前日期，不带时间）
     */
    //private Date Date;
    /**
     * FENDDATE_H 到期日期
     */
    //private Date endDateH;

    /**
     * FSALEORGID 销售组织
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    //private String saleOrgId;

    /**
     * FPAYORGID 收款组织
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    //private String payOrgIdPacked;



    /**
     * FsubHeadSuppiler 表头客户
     * <br>默认三个均为 CUST0001
     * <br>注意此处要修改三个字段
     * <br>FORDERID,FTRANSFERID,FChargeId,均为packed数据
     * <li>使用packed标记时数据填写至节点下的FNumber节点中
     * <br>示例 : "字段名": {"FNumber": "字段值"}
     */
    //TODO 此属性对应收货方，付款方，订货方三者ID，目前均一样，是否要在后续进行区分
    //private String subHeadSupplierPacked;




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


    /**
     * FENDDATE 计划到期时间
     */
    //private Date endDate;


    //FBillTypeID 单据类型默认为 YSD01_SYS;
    //FBillNo（保存时会自动生成）
    //FISINIT 是否期初单据默认为 false
    //FISTAX 是否按含税单价录入默认为 true
    //FCancelStatus 作废状态默认为 A
    //FBUSINESSTYPE 业务类型默认为 BZ
    //FSetAccountType 立账类型默认为 1
    //FISHookMatch 参与暂估应收核销默认为 false
    //FISINVOICEARLIER 先到票后出库默认为 false
    //FWBOPENQTY 反写开票数量默认为 false
    //FPAYRATE 应收比例默认为100.0
    //FPAYAMOUNTFOR 应收金额通过计算获取


}
