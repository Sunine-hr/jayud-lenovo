package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddInvoiceEntryForm;
import com.jayud.scm.model.po.InvoiceEntry;
import com.jayud.scm.mapper.InvoiceEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.InvoiceEntryVO;
import com.jayud.scm.service.IInvoiceEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 结算明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class InvoiceEntryServiceImpl extends ServiceImpl<InvoiceEntryMapper, InvoiceEntry> implements IInvoiceEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<InvoiceEntry> getListByInvoiceId(Long id) {
        QueryWrapper<InvoiceEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InvoiceEntry::getInvoiceId,id);
        queryWrapper.lambda().eq(InvoiceEntry::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public List<InvoiceEntryVO> findInvoiceEntryByInvoiceIdAndOrderEntryId(Integer invoiceId, Integer orderEntryId) {
        QueryWrapper<InvoiceEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InvoiceEntry::getInvoiceId,invoiceId);
        queryWrapper.lambda().eq(InvoiceEntry::getOrderEntryId,orderEntryId);
        queryWrapper.lambda().eq(InvoiceEntry::getVoided,0);
        List<InvoiceEntry> list = this.list(queryWrapper);
        List<InvoiceEntryVO> invoiceEntryVOS = ConvertUtil.convertList(list, InvoiceEntryVO.class);
        return invoiceEntryVOS;
    }

    @Override
    public boolean updateInvoiceEntry(List<AddInvoiceEntryForm> invoiceEntryVOList) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<InvoiceEntry> invoiceEntries = ConvertUtil.convertList(invoiceEntryVOList, InvoiceEntry.class);
        for (InvoiceEntry invoiceEntry : invoiceEntries) {
            invoiceEntry.setMdyBy(systemUser.getId().intValue());
            invoiceEntry.setMdyByDtm(LocalDateTime.now());
            invoiceEntry.setMdyByName(systemUser.getUserName());
        }
        boolean result = this.updateBatchById(invoiceEntries);
        if(result){
            log.warn("修改应收款明细成功");
        }
        return result;
    }
}
