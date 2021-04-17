package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.AccountBalanceForm;
import com.jayud.mall.model.po.AccountBalance;
import com.jayud.mall.mapper.AccountBalanceMapper;
import com.jayud.mall.model.vo.AccountBalanceVO;
import com.jayud.mall.service.IAccountBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 账户余额表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Service
public class AccountBalanceServiceImpl extends ServiceImpl<AccountBalanceMapper, AccountBalance> implements IAccountBalanceService {

    @Autowired
    AccountBalanceMapper accountBalanceMapper;

    @Override
    public List<AccountBalanceVO> findCurrAccountBalance(AccountBalanceForm form) {
        return accountBalanceMapper.findCurrAccountBalance(form);
    }
}
