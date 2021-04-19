package com.jayud.mall.service;

import com.jayud.mall.model.bo.AccountBalanceForm;
import com.jayud.mall.model.po.AccountBalance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.AccountBalanceVO;

import java.util.List;

/**
 * <p>
 * 账户余额表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
public interface IAccountBalanceService extends IService<AccountBalance> {

    /**
     * 当前登录账户余额
     * @param form
     * @return
     */
    List<AccountBalanceVO> findCurrAccountBalance(AccountBalanceForm form);
}
