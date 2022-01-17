package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerEntry;
import com.jayud.scm.mapper.CustomerEntryMapper;
import com.jayud.scm.service.ICustomerEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerEntryServiceImpl extends ServiceImpl<CustomerEntryMapper, CustomerEntry> implements ICustomerEntryService {

}
