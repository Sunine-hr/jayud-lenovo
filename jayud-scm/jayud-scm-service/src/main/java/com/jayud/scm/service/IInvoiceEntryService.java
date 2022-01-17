package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddInvoiceEntryForm;
import com.jayud.scm.model.po.InvoiceEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.InvoiceEntryVO;

import java.util.List;

/**
 * <p>
 * 结算明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IInvoiceEntryService extends IService<InvoiceEntry> {

    List<InvoiceEntry> getListByInvoiceId(Long id);

    List<InvoiceEntryVO> findInvoiceEntryByInvoiceIdAndOrderEntryId(Integer invoiceId, Integer orderEntryId);

    boolean updateInvoiceEntry(List<AddInvoiceEntryForm> invoiceEntryVOList);
}
