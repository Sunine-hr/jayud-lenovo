package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm2;
import com.jayud.scm.model.po.AccountBankBillEntry;
import com.jayud.scm.mapper.AccountBankBillEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.service.IAccountBankBillEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 水单明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Service
public class AccountBankBillEntryServiceImpl extends ServiceImpl<AccountBankBillEntryMapper, AccountBankBillEntry> implements IAccountBankBillEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<AccountBankBillEntry> getListByAccountBankBillId(Long id) {
        QueryWrapper<AccountBankBillEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AccountBankBillEntry::getBankBillId,id.intValue());
        queryWrapper.lambda().eq(AccountBankBillEntry::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public boolean addAccountBankBillEntry(AddAccountBankBillEntryForm2 form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        List<AccountBankBillEntry> accountBankBillEntries = ConvertUtil.convertList(form.getAddAccountBankBillEntryForms(), AccountBankBillEntry.class);

        for (AccountBankBillEntry accountBankBillEntry : accountBankBillEntries) {
            accountBankBillEntry.setBankBillId(form.getBankBillId());
            accountBankBillEntry.setCrtBy(systemUser.getId().intValue());
            accountBankBillEntry.setCrtByDtm(LocalDateTime.now());
            accountBankBillEntry.setCrtByName(systemUser.getUserName());
        }

        boolean result = this.saveBatch(accountBankBillEntries);
        return result;
    }
}
