package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CheckOrder;
import com.jayud.scm.model.po.CheckOrderFollow;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.po.TaxInvoice;
import com.jayud.scm.mapper.TaxInvoiceMapper;
import com.jayud.scm.service.ISystemUserService;
import com.jayud.scm.service.ITaxInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 销项票主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Service
public class TaxInvoiceServiceImpl extends ServiceImpl<TaxInvoiceMapper, TaxInvoice> implements ITaxInvoiceService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public boolean dispatch(List<Integer> taxInvoiceIds, Integer id, String deliverNo) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<TaxInvoice> taxInvoices = new ArrayList<>();
        for (Integer taxInvoicedId : taxInvoiceIds) {
            TaxInvoice taxInvoice = new TaxInvoice();
            taxInvoice.setId(taxInvoicedId);
            taxInvoice.setDeliverId(id);
            taxInvoice.setDeliverNo(deliverNo);
            taxInvoice.setMdyBy(systemUser.getId().intValue());
            taxInvoice.setMdyByDtm(LocalDateTime.now());
            taxInvoice.setMdyByName(systemUser.getUserName());
            taxInvoices.add(taxInvoice);
        }
        boolean result = this.updateBatchById(taxInvoices);
        if(result){
            log.warn("提验货单绑定配送车辆成功");
        }
        return result;
    }

    @Override
    public boolean deleteDispatch(List<Integer> taxInvoiceIds) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<TaxInvoice> taxInvoices = new ArrayList<>();
        for (Integer taxInvoicedId : taxInvoiceIds) {
            TaxInvoice taxInvoice = new TaxInvoice();
            taxInvoice.setId(taxInvoicedId);
            taxInvoice.setDeliverId(null);
            taxInvoice.setDeliverNo(null);
            taxInvoice.setMdyBy(systemUser.getId().intValue());
            taxInvoice.setMdyByDtm(LocalDateTime.now());
            taxInvoice.setMdyByName(systemUser.getUserName());
            taxInvoices.add(taxInvoice);
        }
        boolean result = this.updateBatchById(taxInvoices);
        if(result){
            log.warn("提验货单绑定配送车辆成功");
        }
        return result;
    }
}
