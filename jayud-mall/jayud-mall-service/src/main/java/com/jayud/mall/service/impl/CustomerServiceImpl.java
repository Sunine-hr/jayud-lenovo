package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.model.bo.QueryCustomerForm;
import com.jayud.mall.model.po.Customer;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public IPage<CustomerVO> findCustomerByPage(QueryCustomerForm form) {
        //定义分页参数
        Page<CustomerVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomerVO> pageInfo = customerMapper.findCustomerByPage(page, form);
        return pageInfo;

    }
}
