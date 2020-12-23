package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderReceivableBillDetailMapper extends BaseMapper<OrderReceivableBillDetail> {

    /**
     * 应收对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(Page page, @Param("form") QueryPaymentBillDetailForm form);

    /**
     * 导出应收对账单分页查询
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(@Param("form") QueryPaymentBillDetailForm form);

    /**
     * 应收对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditSBillByPage(Page page, @Param("form") QueryEditBillForm form);

    /**
     * 预览账单表头
     * @param billNo
     * @return
     */
    List<SheetHeadVO> findSSheetHead(@Param("billNo") String billNo);

    /**
     * 预览账单分页查询
     * @param billNo
     * @return
     */
    List<ViewBilToOrderVO> viewSBillDetail(@Param("billNo") String billNo);

    /**
     * 查询账单明细
     * @param billNo
     * @return
     */
    List<ViewBillToCostClassVO> findSCostClass(@Param("billNo") String billNo);

    /**
     * 对账单详情的全局数据部分
     * @param billNo
     * @return
     */
    ViewBillVO getViewSBill(@Param("billNo") String billNo);

    /**
     * 应收对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findSBillAuditByPage(Page page,@Param("form") QueryFBillAuditForm form);

    /**
     * 导出应收对账单分页查询
     * @param form
     * @return
     */
    List<PaymentNotPaidBillVO> findSBillAuditByPage(@Param("form") QueryFBillAuditForm form);

    /**
     * 开票审核列表
     * @param billNo
     * @return
     */
    List<FCostVO> findSCostList(@Param("billNo") String billNo);

    /**
     * 获取推送金蝶的应收数据
     * @param billNo
     * @return
     */
    ReceivableHeaderForm getReceivableHeaderForm(@Param("billNo") String billNo);

    /**
     * 获取推送金蝶的应收详情数据
     * @param billNo
     * @return
     */
    List<APARDetailForm> findReceivableHeaderDetail(@Param("billNo") String billNo);

    /**
     * 应收:开票和付款申请/开票和付款核销/核销界面展示的金额
     * @param billNo
     * @return
     */
    CostAmountVO getSCostAmountView(@Param("billNo") String billNo);

    /**
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     * @param legalName
     * @param unitAccount
     * @param subType
     * @param orderNo
     * @return
     */
    List<OrderReceivableBillDetail> getNowSOrderExist(@Param("legalName") String legalName, @Param("unitAccount") String unitAccount,
                                                     @Param("subType") String subType,@Param("orderNo") String orderNo);
}
