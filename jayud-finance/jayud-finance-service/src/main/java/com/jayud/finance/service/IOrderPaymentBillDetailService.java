package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBillDetail;
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
public interface IOrderPaymentBillDetailService extends IService<OrderPaymentBillDetail> {

    /**
     * 应付对账单分页查询
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(QueryPaymentBillDetailForm form);

    /**
     * 付款申请
     * @param form
     * @return
     */
    Boolean applyPayment(ApplyPaymentForm form);

    /**
     * 付款申请作废
     * @param billDetailId
     * @return
     */
    Boolean applyPaymentCancel(Long billDetailId);

    /**
     * 编辑对账单分页查询
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditBillByPage(QueryEditBillForm form);

    /**
     * 编辑对账单保存
     * @param form
     * @return
     */
    Boolean editBill(EditBillForm form);

    /**
     * 编辑对账单提交
     * @param billDetailId
     * @return
     */
    Boolean editBillSubmit(Long billDetailId);

    /**
     * 对账单详情
     * @param billNo
     * @return
     */
    List<ViewBilToOrderVO> viewBillDetail(String billNo);

    /**
     * 对账单详情表头
     * @return
     */
    List<SheetHeadVO> findSheetHead(String billNo);

    /**
     * 对账单详情的全局数据部分
     * @return
     */
    ViewBillVO getViewBill(String billNo);

    /**
     * 应付对账单审核
     * @param form
     * @return
     */
    Boolean billAudit(BillAuditForm form);

    /**
     * 反审核
     * @param form
     * @return
     */
    Boolean contraryAudit(ListForm form);


    /*****************************************************财务核算模块****************************************************/
    /**
     * 财务核算分页查询
     * @param form
     * @return
     */
    IPage<FinanceAccountVO> findFinanceAccountByPage(QueryFinanceAccountForm form);

    /**
     * 应付对账单分页查询
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findFBillAuditByPage(QueryEditBillForm form);

    /**
     * 核销列表
     * @param billNo
     * @return
     */
    List<HeXiaoListVO> heXiaoList(String billNo);

    /**
     * 核销确认
     * @param form
     * @return
     */
    Boolean heXiaoConfirm(List<HeXiaoConfirmForm> form);

    /**
     * 开票审核列表
     * @param billNo
     * @return
     */
    List<FCostVO> findFCostList(String billNo);

    /**
     * 开票审核
     * @param form
     * @return
     */
    Boolean auditInvoice(BillAuditForm form);

    /**
     * 开票核销列表
     * @param billNo
     * @return
     */
    List<MakeInvoiceVO> findInvoiceList(String billNo);

    /**
     * 开票核销
     * @param form
     * @return
     */
    Boolean makeInvoice(MakeInvoiceForm form);

    /**
     * 开票作废
     * @param invoiceId
     * @return
     */
    Boolean makeInvoiceDel(Long invoiceId);
}
