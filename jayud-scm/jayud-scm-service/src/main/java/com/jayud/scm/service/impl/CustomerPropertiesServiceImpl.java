package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerProperties;
import com.jayud.scm.mapper.CustomerPropertiesMapper;
import com.jayud.scm.service.ICustomerPropertiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户资产表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerPropertiesServiceImpl extends ServiceImpl<CustomerPropertiesMapper, CustomerProperties> implements ICustomerPropertiesService {

}
