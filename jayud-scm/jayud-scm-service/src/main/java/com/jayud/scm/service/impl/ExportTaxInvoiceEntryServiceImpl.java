package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.ExportTaxInvoiceEntry;
import com.jayud.scm.mapper.ExportTaxInvoiceEntryMapper;
import com.jayud.scm.service.IExportTaxInvoiceEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 进项票明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Service
public class ExportTaxInvoiceEntryServiceImpl extends ServiceImpl<ExportTaxInvoiceEntryMapper, ExportTaxInvoiceEntry> implements IExportTaxInvoiceEntryService {

    @Override
    public List<ExportTaxInvoiceEntry> getExportTaxInvoiceEntryByExportTaxInvoiceId(int intValue) {
        QueryWrapper<ExportTaxInvoiceEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExportTaxInvoiceEntry::getExportInvId,intValue);
        queryWrapper.lambda().eq(ExportTaxInvoiceEntry::getVoided,0);
        return this.list(queryWrapper);
    }
}
