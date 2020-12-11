package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.vo.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderReceivableBillDetailService extends IService<OrderReceivableBillDetail> {

    /**
     * 应收对账单分页查询
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(QueryPaymentBillDetailForm form);

    /**
     * 导出使用
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findReceiveBillDetail(QueryPaymentBillDetailForm form);

    /**
     * 提交财务
     * @param form
     * @return
     */
    CommonResult submitSCw(ListForm form);

    /**
     * 开票申请
     * @param form
     * @return
     */
    Boolean applyInvoice(ApplyInvoiceForm form);

    /**
     * 开票申请作废
     * @param billNo
     * @return
     */
    Boolean applyInvoiceCancel(String billNo);

    /**
     * 编辑对账单分页查询
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditSBillByPage(QueryEditBillForm form);

    /**
     * 编辑对账单保存
     * @param form
     * @return
     */
    CommonResult editSBill(EditSBillForm form);

    /**
     * 编辑对账单提交
     * @param billNo
     * @param loginUserName
     * @return
     */
    Boolean editSBillSubmit(String billNo,String loginUserName);

    /**
     * 对账单详情
     * @param billNo
     * @return
     */
    List<ViewBilToOrderVO> viewSBillDetail(String billNo);

    /**
     * 对账单详情表头
     * @return
     */
    List<SheetHeadVO> findSSheetHead(String billNo);

    /**
     * 对账单详情的全局数据部分
     * @return
     */
    ViewBillVO getViewSBill(String billNo);

    /**
     * 应收对账单审核
     * @param form
     * @return
     */
    CommonResult billSAudit(BillAuditForm form);

    /**
     * 应收反审核
     * @param form
     * @return
     */
    CommonResult contrarySAudit(ListForm form);

   //财务核算模块
    /**
     * 应收对账单分页查询
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findSBillAuditByPage(QueryFBillAuditForm form);

    /**
     * 导出 对账单明细
     * @param form
     * @return
     */
    List<PaymentNotPaidBillVO> findSBillAudit(QueryFBillAuditForm form);

    /**
     * 开票审核列表
     * @param billNo
     * @return
     */
    List<FCostVO> findSCostList(String billNo);

    /**
     * 开票审核
     * @param form
     * @return
     */
    CommonResult auditSInvoice(BillAuditForm form);

    /**
     *编辑保存确定
     * @param costIds
     * @return
     */
    Boolean editSSaveConfirm(List<Long> costIds);

    /**
     * 编辑删除
     * @param costIds
     * @return
     */
    Boolean editSDel(List<Long> costIds);

    /**
     * 获取推送金蝶的应收数据
     * @param billNo
     * @return
     */
    ReceivableHeaderForm getReceivableHeaderForm(String billNo);

    /**
     * 获取推送金蝶的应收详细数据
     * @param billNo
     * @return
     */
    List<APARDetailForm> findReceivableHeaderDetail(String billNo);

    /**
     * 开票和付款申请/开票和付款核销/核销界面展示的金额
     * @param billNo
     * @return
     */
    CostAmountVO getSCostAmountView(String billNo);

}
