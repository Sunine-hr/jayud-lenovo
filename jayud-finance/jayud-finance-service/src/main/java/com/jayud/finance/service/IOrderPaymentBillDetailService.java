package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.bo.ApplyPaymentForm;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;

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
}
