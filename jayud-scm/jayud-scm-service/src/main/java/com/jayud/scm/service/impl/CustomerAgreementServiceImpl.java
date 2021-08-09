package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerAgreement;
import com.jayud.scm.mapper.CustomerAgreementMapper;
import com.jayud.scm.service.ICustomerAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户协议表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerAgreementServiceImpl extends ServiceImpl<CustomerAgreementMapper, CustomerAgreement> implements ICustomerAgreementService {

}
