package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.AccountPayable;
import com.jayud.mall.mapper.AccountPayableMapper;
import com.jayud.mall.service.IAccountPayableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应付对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class AccountPayableServiceImpl extends ServiceImpl<AccountPayableMapper, AccountPayable> implements IAccountPayableService {

}
