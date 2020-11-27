package com.jayud.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.MakeInvoiceListForm;
import com.jayud.finance.po.MakeInvoice;
import com.jayud.finance.vo.MakeInvoiceVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IMakeInvoiceService extends IService<MakeInvoice> {

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
    CommonResult makeInvoice(MakeInvoiceListForm form);

    /**
     * 开票作废
     * @param invoiceId
     * @return
     */
    Boolean makeInvoiceDel(Long invoiceId);
}
