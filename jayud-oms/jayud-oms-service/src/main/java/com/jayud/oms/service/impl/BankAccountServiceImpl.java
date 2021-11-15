package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.BankAccountMapper;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.service.IBankAccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 银行信息
 * </p>
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {


    @Override
    public String bankAccountNum() {
        StringBuilder orderNo = new StringBuilder();
        int count = this.count();
        orderNo.append("BK").append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
                .append(StringUtils.zeroComplement(4, count + 1));
        return orderNo.toString();
    }

    @Override
    public Long saveOrUpdateAddr(BankAccount bankAccount) {
        if (Objects.isNull(bankAccount.getId())) {
            bankAccount.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken())
                    .setStatus(StatusEnum.ENABLE.getCode());
            this.save(bankAccount);
        } else {
            bankAccount.setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            this.updateById(bankAccount);
        }
        return bankAccount.getId();
    }

    @Override
    public IPage<BankAccountVO> findBankAccountByPage(BankAccountFrom form) {
        Page<BankAccountVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findBankAccountByPage(page, form);
    }
}
