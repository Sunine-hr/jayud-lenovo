package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddExportTaxInvoiceForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.ExportTaxInvoice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.ExportTaxInvoiceVO;

/**
 * <p>
 * 进项票主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
public interface IExportTaxInvoiceService extends IService<ExportTaxInvoice> {

    ExportTaxInvoiceVO getExportTaxInvoiceById(Integer id);

    boolean saveOrUpdateExportTaxInvoice(AddExportTaxInvoiceForm form);

    boolean delete(DeleteForm deleteForm);
}
