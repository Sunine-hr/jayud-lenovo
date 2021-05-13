package com.jayud.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.MakeInvoiceListForm;
import com.jayud.finance.po.MakeInvoice;
import com.jayud.finance.vo.MakeInvoiceVO;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;

import java.math.BigDecimal;
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
public interface IMakeInvoiceService extends IService<MakeInvoice> {

    /**
     * 开票核销列表
     *
     * @param billNo
     * @return
     */
    List<MakeInvoiceVO> findInvoiceList(String billNo);

    /**
     * 开票核销
     *
     * @param form
     * @return
     */
    CommonResult makeInvoice(MakeInvoiceListForm form);

    /**
     * 开票作废
     *
     * @param invoiceId
     * @return
     */
    Boolean makeInvoiceDel(Long invoiceId);

    Map<String, BigDecimal> calculationAccounting(List<String> billNos, int oprType);
}
