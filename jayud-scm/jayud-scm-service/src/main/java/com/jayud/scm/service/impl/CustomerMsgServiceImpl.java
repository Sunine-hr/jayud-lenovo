package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.CustomerMsg;
import com.jayud.scm.mapper.CustomerMsgMapper;
import com.jayud.scm.service.ICustomerMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户消息通知表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerMsgServiceImpl extends ServiceImpl<CustomerMsgMapper, CustomerMsg> implements ICustomerMsgService {

}
