package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.InvoiceMapper;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.model.vo.InvoiceDetailVO;
import com.jayud.scm.service.IInvoiceEntryService;
import com.jayud.scm.service.IInvoiceFollowService;
import com.jayud.scm.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 结算单（应收款）主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {

    @Autowired
    private IInvoiceEntryService iInvoiceEntryService;

    @Autowired
    private IInvoiceFollowService iInvoiceFollowService;

    @Override
    public IPage<InvoiceDetailVO> findByPage(QueryForm form) {
        Page<InvoiceDetailVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<InvoiceEntry> invoiceEntries = new ArrayList<>();
        List<InvoiceFollow> invoiceFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<InvoiceEntry> invoiceEntries1 = iInvoiceEntryService.getListByInvoiceId(id);
            for (InvoiceEntry invoiceEntry : invoiceEntries1) {
                invoiceEntry.setVoidedBy(deleteForm.getId().intValue());
                invoiceEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                invoiceEntry.setVoidedByName(deleteForm.getName());
                invoiceEntry.setVoided(1);
                invoiceEntries.add(invoiceEntry);
            }

            InvoiceFollow invoiceFollow = new InvoiceFollow();
            invoiceFollow.setInvoiceId(id.intValue());
            invoiceFollow.setSType(OperationEnum.DELETE.getCode());
            invoiceFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            invoiceFollow.setCrtBy(deleteForm.getId().intValue());
            invoiceFollow.setCrtByDtm(deleteForm.getDeleteTime());
            invoiceFollow.setCrtByName(deleteForm.getName());
            invoiceFollows.add(invoiceFollow);
        }
        //删除应收款单明细
        boolean update = this.iInvoiceEntryService.updateBatchById(invoiceEntries);
        if(!update){
            log.warn("删除水单详情失败");
        }

        boolean b1 = this.iInvoiceFollowService.saveBatch(invoiceFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+invoiceFollows);
        }
        return b1;
    }

    @Override
    public List<Invoice> getByOrderId(Integer id) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Invoice::getOrderId,id);
        queryWrapper.lambda().eq(Invoice::getVoided,0);
        return this.list(queryWrapper);
    }
}
