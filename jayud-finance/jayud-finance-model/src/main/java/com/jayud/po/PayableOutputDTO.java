package com.jayud.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于输出查询获得的应付单数据
 *
 * @author william
 * @description
 * @Date: 2020-05-28 16:35
 */
public class PayableOutputDTO implements InvoiceOutputBase {

//    private String supplierName;

    //private String currency;

    /**
     * 组织
     */
    private String settleOrgName;

//    private String remark;

    /**
     * 业务单号
     */
    private String businessNo;
    /**
     * 部门
     */
    private String purchaseDeptName;
    /**
     * 明细
     */
    private List<APARDetailDTO> entityDetail = new ArrayList<>();

    public String getSettleOrgName() {
        return settleOrgName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public String getDeptName() {
        return purchaseDeptName;
    }

    @Override
    public List getDetail() {
        return entityDetail;
    }

    public void setSettleOrgName(String settleOrgName) {
        this.settleOrgName = settleOrgName;
    }


    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public void setDeptName(String purchaseDeptName) {
        this.purchaseDeptName = purchaseDeptName;
    }

    @Override
    public void setDetail(List list) {
        this.entityDetail = list;
    }


}
