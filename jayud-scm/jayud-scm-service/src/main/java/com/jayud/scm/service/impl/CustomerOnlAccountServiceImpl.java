package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerOnlAccount;
import com.jayud.scm.mapper.CustomerOnlAccountMapper;
import com.jayud.scm.service.ICustomerOnlAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户在线账号表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerOnlAccountServiceImpl extends ServiceImpl<CustomerOnlAccountMapper, CustomerOnlAccount> implements ICustomerOnlAccountService {

}
