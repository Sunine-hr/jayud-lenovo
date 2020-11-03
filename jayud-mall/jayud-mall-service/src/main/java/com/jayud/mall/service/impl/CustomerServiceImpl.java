package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.Customer;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
