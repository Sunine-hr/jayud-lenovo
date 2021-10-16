package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAcctPayEntryForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.AcctPay;
import com.jayud.scm.model.po.AcctPayEntry;
import com.jayud.scm.mapper.AcctPayEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.service.IAcctPayEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 应付款表（付款单明细表） 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class AcctPayEntryServiceImpl extends ServiceImpl<AcctPayEntryMapper, AcctPayEntry> implements IAcctPayEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Override
    public List<AcctPayEntry> getListByAcctPayId(Long id) {
        QueryWrapper<AcctPayEntry> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AcctPayEntry::getPayId,id);
        queryWrapper.lambda().eq(AcctPayEntry::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateAcctPay(AddAcctPayEntryForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        AcctPayEntry acctPayEntry = ConvertUtil.convert(form, AcctPayEntry.class);
        if(form.getId() != null){
            acctPayEntry.setMdyBy(systemUser.getId().intValue());
            acctPayEntry.setMdyByDtm(LocalDateTime.now());
            acctPayEntry.setMdyByName(systemUser.getUserName());
        }else{
            acctPayEntry.setFBillNo(commodityService.getOrderNo(NoCodeEnum.ACCT_ENTRY_PAY.getCode(),LocalDateTime.now()));
            acctPayEntry.setCrtBy(systemUser.getId().intValue());
            acctPayEntry.setCrtByDtm(LocalDateTime.now());
            acctPayEntry.setCrtByName(systemUser.getUserName());
        }
        boolean result = this.saveOrUpdate(acctPayEntry);
        if(result){
            log.warn("应付款单删除失败");
        }
        return result;
    }

    @Override
    public boolean deleteAcctPayEntry(DeleteForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        AcctPayEntry acctPayEntry = new AcctPayEntry();
        acctPayEntry.setId(form.getId().intValue());
        acctPayEntry.setVoided(1);
        acctPayEntry.setVoidedBy(systemUser.getId().intValue());
        acctPayEntry.setVoidedByDtm(LocalDateTime.now());
        acctPayEntry.setVoidedByName(systemUser.getUserName());
        boolean result = this.updateById(acctPayEntry);

        return result;
    }

    @Override
    public List<AcctPayEntry> getAcctPayEntryByOrderId(Integer id) {
        QueryWrapper<AcctPayEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AcctPayEntry::getOrderId,id);
        queryWrapper.lambda().eq(AcctPayEntry::getVoided,0);
        return this.list(queryWrapper);
    }
}
