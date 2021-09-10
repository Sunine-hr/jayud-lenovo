package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddExportTaxInvoiceForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.ExportTaxInvoice;
import com.jayud.scm.mapper.ExportTaxInvoiceMapper;
import com.jayud.scm.model.po.ExportTaxInvoiceEntry;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.ExportTaxInvoiceVO;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.IExportTaxInvoiceEntryService;
import com.jayud.scm.service.IExportTaxInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 进项票主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Service
public class ExportTaxInvoiceServiceImpl extends ServiceImpl<ExportTaxInvoiceMapper, ExportTaxInvoice> implements IExportTaxInvoiceService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IExportTaxInvoiceEntryService exportTaxInvoiceEntryService;

    @Override
    public ExportTaxInvoiceVO getExportTaxInvoiceById(Integer id) {
        ExportTaxInvoice exportTaxInvoice = this.getById(id);
        ExportTaxInvoiceVO exportTaxInvoiceVO = ConvertUtil.convert(exportTaxInvoice, ExportTaxInvoiceVO.class);
        return exportTaxInvoiceVO;
    }

    @Override
    public boolean saveOrUpdateExportTaxInvoice(AddExportTaxInvoiceForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        ExportTaxInvoice exportTaxInvoice = ConvertUtil.convert(form, ExportTaxInvoice.class);
        if(form.getId() != null){
            exportTaxInvoice.setMdyBy(systemUser.getId().intValue());
            exportTaxInvoice.setMdyByDtm(LocalDateTime.now());
            exportTaxInvoice.setMdyByName(systemUser.getUserName());
        }else{
            exportTaxInvoice.setFBillNo(commodityService.getOrderNo(NoCodeEnum.EXPORT_TAX_INVOICE.getCode(),LocalDateTime.now()));
            exportTaxInvoice.setCrtBy(systemUser.getId().intValue());
            exportTaxInvoice.setCrtByDtm(LocalDateTime.now());
            exportTaxInvoice.setCrtByName(systemUser.getUserName());
        }
        boolean result = this.saveOrUpdate(exportTaxInvoice);
        if(result){
            log.warn("新增或修改进项票成功");
        }
        return result;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<ExportTaxInvoiceEntry> exportTaxInvoiceEntries = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            List<ExportTaxInvoiceEntry> exportTaxInvoiceEntries1 = exportTaxInvoiceEntryService.getExportTaxInvoiceEntryByExportTaxInvoiceId(id.intValue());
            exportTaxInvoiceEntries.addAll(exportTaxInvoiceEntries1);
        }
        for (ExportTaxInvoiceEntry exportTaxInvoiceEntry : exportTaxInvoiceEntries) {
            exportTaxInvoiceEntry.setVoided(0);
            exportTaxInvoiceEntry.setVoidedBy(deleteForm.getId().intValue());
            exportTaxInvoiceEntry.setVoidedByDtm(deleteForm.getDeleteTime());
            exportTaxInvoiceEntry.setVoidedByName(deleteForm.getName());
        }
        boolean result = this.exportTaxInvoiceEntryService.updateBatchById(exportTaxInvoiceEntries);
        if(result){
            log.warn("删除进项票明细成功");
        }
        return result;
    }
}
