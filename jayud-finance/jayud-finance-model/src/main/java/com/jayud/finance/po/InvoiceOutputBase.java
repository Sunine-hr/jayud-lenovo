package com.jayud.finance.po;

import java.util.List;

/**
 * 用于输出查询出的账单数据
 *
 * @author william
 * @description
 * @Date: 2020-05-29 20:05
 */

 public interface InvoiceOutputBase {

    String getSettleOrgName();

    String getBusinessNo();

    String getDeptName();

    List getDetail();

   void setSettleOrgName(String s);

   void setBusinessNo(String s);

   void setDeptName(String s);

   void setDetail(List list);
}
