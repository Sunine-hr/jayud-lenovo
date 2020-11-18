package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryEditBillForm;
import com.jayud.finance.bo.QueryFinanceAccountForm;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderPaymentBillDetail;
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
public interface OrderPaymentBillDetailMapper extends BaseMapper<OrderPaymentBillDetail> {

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(Page page, @Param("form") QueryPaymentBillDetailForm form);

    /**
     * 导出应付对账单分页查询
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(@Param("form") QueryPaymentBillDetailForm form);

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findEditBillByPage(Page page, @Param("form") QueryEditBillForm form);

    /**
     * 预览账单表头
     * @param billNo
     * @return
     */
    List<SheetHeadVO> findSheetHead(@Param("billNo") String billNo);

    /**
     * 预览账单分页查询
     * @param billNo
     * @return
     */
    List<ViewBilToOrderVO> viewBillDetail(@Param("billNo") String billNo);

    /**
     * 查询账单明细
     * @param billNo
     * @return
     */
    List<ViewBillToCostClassVO> findCostClass(@Param("billNo") String billNo);

    /**
     * 对账单详情的全局数据部分
     * @param billNo
     * @return
     */
    ViewBillVO getViewBill(@Param("billNo") String billNo);

    /**
     * 财务核算分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<FinanceAccountVO> findFinanceAccountByPage(Page page, @Param("form") QueryFinanceAccountForm form);

    /**
     * 导出财务核算分页查询
     * @param form
     * @return
     */
    List<FinanceAccountVO> findFinanceAccountByPage(@Param("form") QueryFinanceAccountForm form);

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findFBillAuditByPage(Page page,@Param("form") QueryEditBillForm form);

    /**
     * 开票审核列表
     * @param billNo
     * @return
     */
    List<FCostVO> findFCostList(@Param("billNo") String billNo);
}
