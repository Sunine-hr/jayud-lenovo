package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.Customer;
import com.jayud.scm.mapper.CustomerMapper;
import com.jayud.scm.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
