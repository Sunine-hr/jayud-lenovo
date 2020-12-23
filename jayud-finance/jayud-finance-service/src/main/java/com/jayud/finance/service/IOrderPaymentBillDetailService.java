package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.vo.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderPaymentBillDetailService extends IService<OrderPaymentBillDetail> {

    /**
     * 应付对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(QueryPaymentBillDetailForm form);

    /**
     * 导出使用
     *
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findPaymentBillDetail(QueryPaymentBillDetailForm form);

    /**
     * 提交财务
     *
     * @param form
     * @return
     */
    CommonResult submitFCw(ListForm form);

    /**
     * 付款申请
     *
     * @param form
     * @return
     */
    Boolean applyPayment(ApplyPaymentForm form);

    /**
     * 付款申请作废
     *
     * @param billNo
     * @return
     */
    Boolean applyPaymentCancel(String billNo);

    /**
     * 编辑对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditBillByPage(QueryEditBillForm form);

    /**
     * 编辑对账单保存
     *
     * @param form
     * @return
     */
    CommonResult editBill(EditBillForm form);

    /**
     * 编辑对账单提交
     *
     * @param billNo
     * @param loginUserName
     * @return
     */
    Boolean editBillSubmit(String billNo,String loginUserName);

    /**
     * 对账单详情
     *
     * @param billNo
     * @return
     */
    List<ViewFBilToOrderVO> viewBillDetail(String billNo);

    /**
     * 对账单详情表头
     *
     * @return
     */
    List<SheetHeadVO> findSheetHead(String billNo);

    /**
     * 对账单详情的全局数据部分
     *
     * @return
     */
    ViewBillVO getViewBill(String billNo);

    /**
     * 应付对账单审核
     *
     * @param form
     * @return
     */
    CommonResult billAudit(BillAuditForm form);

    /**
     * 反审核
     *
     * @param form
     * @return
     */
    CommonResult contraryAudit(ListForm form);


    //财务核算模块

    /**
     * 财务核算分页查询
     *
     * @param form
     * @return
     */
    IPage<FinanceAccountVO> findFinanceAccountByPage(QueryFinanceAccountForm form);

    /**
     * 导出财务核算分页查询
     *
     * @param form
     * @return
     */
    List<FinanceAccountVO> findFinanceAccount(QueryFinanceAccountForm form);

    /**
     * 应付对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findFBillAuditByPage(QueryFBillAuditForm form);

    /**
     * 导出 对账单明细
     *
     * @param form
     * @return
     */
    List<PaymentNotPaidBillVO> findFBillAudit(QueryFBillAuditForm form);

    /**
     * 付款审核列表
     *
     * @param billNo
     * @return
     */
    List<FCostVO> findFCostList(String billNo);

    /**
     * 付款审核
     *
     * @param form
     * @return
     */
    CommonResult auditFInvoice(BillAuditForm form);

    /**
     *编辑保存确定
     * @param costIds
     * @return
     */
    Boolean editFSaveConfirm(List<Long> costIds);

    /**
     * 编辑删除
     * @param costIds
     * @return
     */
    Boolean editFDel(List<Long> costIds);

    /**
     * 获取推送金蝶的应付数据
     * @param billNo
     * @return
     */
    PayableHeaderForm getPayableHeaderForm(String billNo);

    /**
     * 获取推送金蝶的应付详细数据
     * @param billNo
     * @return
     */
    List<APARDetailForm> findPayableHeaderDetail(String billNo);

    /**
     * 开票和付款申请/开票和付款核销/核销界面展示的金额
     * @param billNo
     * @return
     */
    CostAmountVO getFCostAmountView(String billNo);

    /**
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     * @param legalName
     * @param supplierChName
     * @param subType
     * @param orderNo
     * @return
     */
    List<OrderPaymentBillDetail> getNowFOrderExist(String legalName, String supplierChName, String subType, String orderNo);

}
