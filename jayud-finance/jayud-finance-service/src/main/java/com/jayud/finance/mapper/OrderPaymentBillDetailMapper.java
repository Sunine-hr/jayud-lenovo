package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderPaymentBillDetailMapper extends BaseMapper<OrderPaymentBillDetail> {

    /**
     * 应付对账单分页查询
     *
     * @param page
     * @param form
     * @param data
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(Page page, @Param("form") QueryPaymentBillDetailForm form, @Param("data") List<String> data);

    /**
     * 导出应付对账单分页查询
     *
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(@Param("form") QueryPaymentBillDetailForm form, @Param("data") List<String> data);

    /**
     * 应付对账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditBillByPage(Page page, @Param("form") QueryEditBillForm form,
                                                   @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

    /**
     * 预览账单表头
     *
     * @param billNo
     * @return
     */
    List<SheetHeadVO> findSheetHead(@Param("billNo") String billNo);

    /**
     * 预览账单分页查询
     *
     * @param billNo
     * @return
     */
    List<ViewFBilToOrderVO> viewBillDetail(@Param("billNo") String billNo);

    /**
     * 查询账单明细
     *
     * @param billNo
     * @return
     */
    List<ViewBillToCostClassVO> findCostClass(@Param("billNo") String billNo);

    /**
     * 对账单详情的全局数据部分
     *
     * @param billNo
     * @return
     */
    ViewBillVO getViewBill(@Param("billNo") String billNo);

    /**
     * 财务核算分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<FinanceAccountVO> findFinanceAccountByPage(Page page, @Param("form") QueryFinanceAccountForm form);

    /**
     * 导出财务核算分页查询
     *
     * @param form
     * @return
     */
    List<FinanceAccountVO> findFinanceAccountByPage(@Param("form") QueryFinanceAccountForm form);

    /**
     * 应付对账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findFBillAuditByPage(Page page, @Param("form") QueryFBillAuditForm form);

    /**
     * 导出应付对账单分页查询
     *
     * @param form
     * @return
     */
    List<PaymentNotPaidBillVO> findFBillAuditByPage(@Param("form") QueryFBillAuditForm form);

    /**
     * 付款审核列表
     *
     * @param billNo
     * @return
     */
    List<FCostVO> findFCostList(@Param("billNo") String billNo);

    /**
     * 获取推送金蝶的应付数据
     *
     * @param billNo
     * @return
     */
    List<PayableHeaderForm> getPayableHeaderForm(@Param("billNo") String billNo);

    /**
     * 获取推送金蝶的应付详情数据
     *
     * @param billNo
     * @return
     */
    List<APARDetailForm> findPayableHeaderDetail(@Param("billNo") String billNo, @Param("orderNo") String orderNo);

    /**
     * 应付:开票和付款申请/开票和付款核销/核销界面展示的金额
     *
     * @param billNo
     * @return
     */
    CostAmountVO getFCostAmountView(@Param("billNo") String billNo);

    /**
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     *
     * @param legalName
     * @param supplierChName
     * @param subType
     * @param orderNo
     * @return
     */
    List<OrderPaymentBillDetail> getNowFOrderExist(@Param("legalName") String legalName, @Param("supplierChName") String supplierChName,
                                                   @Param("subType") String subType, @Param("orderNo") String orderNo);


    /**
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     * @return
     */
    List<OrderPaymentBillDetail> getNowFOrderExistByLegalId(@Param("legalEntityId") Long legalEntityId,
                                                            @Param("supplierCode")String supplierCode,
                                                            @Param("subType")String subType,
                                                            @Param("orderNo")String orderNo);


    List<ProfitStatementBillDetailsVO> statisticsBillByCostIds(@Param("payCostIds")List<String> payCostIds);

    List<Map<String, Object>> getBillingStatusNum(@Param("userName") String userName);
}
