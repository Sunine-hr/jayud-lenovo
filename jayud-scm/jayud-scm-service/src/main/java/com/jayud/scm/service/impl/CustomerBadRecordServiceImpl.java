package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerBadRecord;
import com.jayud.scm.mapper.CustomerBadRecordMapper;
import com.jayud.scm.service.ICustomerBadRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户不良记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerBadRecordServiceImpl extends ServiceImpl<CustomerBadRecordMapper, CustomerBadRecord> implements ICustomerBadRecordService {

}
