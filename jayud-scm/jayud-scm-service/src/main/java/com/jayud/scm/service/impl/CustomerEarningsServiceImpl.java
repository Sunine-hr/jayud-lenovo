package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerEarnings;
import com.jayud.scm.mapper.CustomerEarningsMapper;
import com.jayud.scm.service.ICustomerEarningsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户收入表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerEarningsServiceImpl extends ServiceImpl<CustomerEarningsMapper, CustomerEarnings> implements ICustomerEarningsService {

}
