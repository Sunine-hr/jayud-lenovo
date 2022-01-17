package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerRisk;
import com.jayud.scm.mapper.CustomerRiskMapper;
import com.jayud.scm.service.ICustomerRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户风险表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerRiskServiceImpl extends ServiceImpl<CustomerRiskMapper, CustomerRisk> implements ICustomerRiskService {

}
