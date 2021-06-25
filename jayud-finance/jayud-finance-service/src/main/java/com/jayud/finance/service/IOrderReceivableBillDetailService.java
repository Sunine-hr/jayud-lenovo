package com.jayud.finance.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.vo.*;
import com.jayud.finance.vo.template.order.Template;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderReceivableBillDetailService extends IService<OrderReceivableBillDetail> {

    /**
     * 应收对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(QueryPaymentBillDetailForm form);

    /**
     * 导出使用
     *
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findReceiveBillDetail(QueryPaymentBillDetailForm form);

    /**
     * 提交财务
     *
     * @param form
     * @return
     */
    CommonResult submitSCw(ListForm form);

    /**
     * 开票申请
     *
     * @param form
     * @return
     */
    Boolean applyInvoice(ApplyInvoiceForm form);

    /**
     * 开票申请作废
     *
     * @param billNo
     * @return
     */
    Boolean applyInvoiceCancel(String billNo);

    /**
     * 编辑对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditSBillByPage(QueryEditBillForm form);

    /**
     * 编辑对账单保存
     *
     * @param form
     * @return
     */
    CommonResult editSBill(EditSBillForm form);

    /**
     * 编辑对账单提交
     *
     * @param billNo
     * @param loginUserName
     * @return
     */
    Boolean editSBillSubmit(String billNo, String loginUserName);

    /**
     * 对账单详情
     *
     * @param billNo
     * @return
     */
    List<ViewBilToOrderVO> viewSBillDetail(String billNo);

    /**
     * 对账单详情 TODO 升级版
     *
     * @param billNo
     * @return
     */
    public JSONArray viewSBillDetailInfo(String billNo, String cmd, String templateCmd);


    /**
     * 对账单详情表头
     *
     * @return
     */
    List<SheetHeadVO> findSSheetHead(String billNo, Map<String, Object> callbackArg);


    List<SheetHeadVO> findSSheetHeadInfo(String billNo, Map<String, Object> callbackArg,
                                         String cmd, String templateCmd);

    /**
     * 对账单详情的全局数据部分
     *
     * @return
     */
    ViewBillVO getViewSBill(String billNo);

    /**
     * 应收对账单审核
     *
     * @param form
     * @return
     */
    CommonResult billSAudit(BillAuditForm form);

    /**
     * 应收反审核
     *
     * @param form
     * @return
     */
    CommonResult contrarySAudit(ListForm form);

    //财务核算模块

    /**
     * 应收对账单分页查询
     *
     * @param form
     * @return
     */
//    IPage<PaymentNotPaidBillVO> findSBillAuditByPage(QueryFBillAuditForm form);

    /**
     * 应收对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<LinkedHashMap> findSBillAuditByPage(QueryFBillAuditForm form);

    /**
     * 导出 对账单明细
     *
     * @param form
     * @return
     */
    List<PaymentNotPaidBillVO> findSBillAudit(QueryFBillAuditForm form);

    /**
     * 开票审核列表
     *
     * @param billNo
     * @return
     */
    List<FCostVO> findSCostList(String billNo);

    /**
     * 开票审核
     *
     * @param form
     * @return
     */
    CommonResult auditSInvoice(BillAuditForm form);

    /**
     * 编辑保存确定
     *
     * @param costIds
     * @param form
     * @return
     */
    Boolean editSSaveConfirm(List<Long> costIds, EditSBillForm form);

    /**
     * 编辑删除
     *
     * @param costIds
     * @return
     */
    Boolean editSDel(List<Long> costIds);

    /**
     * 获取推送金蝶的应收数据
     *
     * @param billNo
     * @return
     */
    List<ReceivableHeaderForm> getReceivableHeaderForm(String billNo);

    /**
     * 获取推送金蝶的应收详细数据
     *
     * @param billNo
     * @param orderNo
     * @return
     */
    List<APARDetailForm> findReceivableHeaderDetail(String billNo, String orderNo);

    /**
     * 开票和付款申请/开票和付款核销/核销界面展示的金额
     *
     * @param billNo
     * @return
     */
    CostAmountVO getSCostAmountView(String billNo);

    /**
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     *
     * @param legalName
     * @param unitAccount
     * @param subType
     * @param orderNo
     * @return
     */
    List<OrderReceivableBillDetail> getNowSOrderExist(String legalName, String unitAccount, String subType, String orderNo);


    public List<OrderReceivableBillDetail> getNowSOrderExistByLegalId(Long legalId, String unitCode, String subType, String orderNo);

    /**
     * 获取编辑账单数
     *
     * @param billNo
     */
    int getEditBillNum(String billNo);

    /**
     * 根据账单编号查询应收账单详情
     */
    public List<OrderReceivableBillDetail> getByBillNo(String billNo);


    /**
     * 根据条件查询账单详情信息
     */
    public List<OrderReceivableBillDetail> getByCondition(OrderReceivableBillDetail orderReceivableBillDetail);

    /**
     * 统计账单数据
     *
     * @param billNo
     */
    public void statisticsBill(String billNo);
}
