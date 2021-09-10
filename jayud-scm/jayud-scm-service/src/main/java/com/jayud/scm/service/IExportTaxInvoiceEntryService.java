package com.jayud.scm.service;

import com.jayud.scm.model.po.ExportTaxInvoiceEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 进项票明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
public interface IExportTaxInvoiceEntryService extends IService<ExportTaxInvoiceEntry> {

    List<ExportTaxInvoiceEntry> getExportTaxInvoiceEntryByExportTaxInvoiceId(int intValue);
}
