package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddAccountBankBillEntryForm;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm2;
import com.jayud.scm.model.po.AccountBankBillEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 水单明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
public interface IAccountBankBillEntryService extends IService<AccountBankBillEntry> {

    List<AccountBankBillEntry> getListByAccountBankBillId(Long id);

    boolean addAccountBankBillEntry(AddAccountBankBillEntryForm2 form);
}
