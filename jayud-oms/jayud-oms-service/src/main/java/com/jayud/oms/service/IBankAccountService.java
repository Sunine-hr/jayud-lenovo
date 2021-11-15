package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.CustomerAddress;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.CustomerAddrVO;

/**
 * 银行账户信息
 *
 * @author wh
 * @since 2021-11-10
 */
public interface IBankAccountService extends IService<BankAccount> {

    String bankAccountNum();

    Long saveOrUpdateAddr(BankAccount bankAccount);

    IPage<BankAccountVO> findBankAccountByPage(BankAccountFrom form);
}
