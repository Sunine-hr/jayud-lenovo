package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.ApplyInvoiceForm;
import com.jayud.finance.bo.ListForm;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;

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

}
