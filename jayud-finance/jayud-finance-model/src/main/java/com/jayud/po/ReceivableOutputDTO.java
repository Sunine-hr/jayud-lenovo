package com.jayud.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于输出应收单的数据
 *
 * @author william
 * @description
 * @Date: 2020-05-28 16:31
 */
public class ReceivableOutputDTO implements InvoiceOutputBase {
//    /**
//     * 客户名称
//     */
//    private String customerName;
    /**
     * 币别
     */
    //private String currency;
    /**
     * 结算组织
     */
    private String settleOrgName;
    /**
     * 销售部门
     */
    private String saleDeptName;
    /**
     * 业务单号（报关单号）
     */
    private String businessNo;
//    /**
//     * 备注
//     */
//    private String remark;
//    /**
//     * 账单号
//     */
//    private String billNo;
    /**
     * 明细
     */
    private List<APARDetailDTO> entityDetail = new ArrayList<>();

//    public String getCompanyName() {
//        return customerName;
//    }

//    public String getCurrency() {
//        return currency;
//    }

    public String getSettleOrgName() {
        return settleOrgName;
    }

    public String getDeptName() {
        return saleDeptName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

//    public String getRemark() {
//        return remark;
//    }
//
//    public String getBillNo() {
//        return billNo;
//    }

    public List<APARDetailDTO> getDetail() {
        return entityDetail;
    }

//    public void setCompanyName(String customerName) {
//        this.customerName = customerName;
//    }

//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }

    public void setSettleOrgName(String settleOrgName) {
        this.settleOrgName = settleOrgName;
    }

    public void setDeptName(String saleDeptName) {
        this.saleDeptName = saleDeptName;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public void setBillNo(String billNo) {
//        this.billNo = billNo;
//    }

    public void setDetail(List entityDetail) {
        this.entityDetail = entityDetail;
    }
}
