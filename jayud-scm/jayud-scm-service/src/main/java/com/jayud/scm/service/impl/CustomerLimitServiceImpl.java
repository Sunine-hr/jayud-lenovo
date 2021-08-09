package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerLimit;
import com.jayud.scm.mapper.CustomerLimitMapper;
import com.jayud.scm.service.ICustomerLimitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户限制表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerLimitServiceImpl extends ServiceImpl<CustomerLimitMapper, CustomerLimit> implements ICustomerLimitService {

}
