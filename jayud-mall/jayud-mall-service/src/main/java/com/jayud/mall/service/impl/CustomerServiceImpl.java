package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.model.bo.CustomerEditForm;
import com.jayud.mall.model.bo.CustomerForm;
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
        page.addOrder(OrderItem.asc("t.id"));
        IPage<CustomerVO> pageInfo = customerMapper.findCustomerByPage(page, form);
        return pageInfo;

    }

    @Override
    public CommonResult saveCustomer(CustomerEditForm form) {
        Customer customer = ConvertUtil.convert(form, Customer.class);
        Integer id = form.getId();
        String company = form.getCompany();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", company);
        queryWrapper.ne("id", id);
        int companyCount = this.count(queryWrapper);
        if(companyCount > 0){
            return CommonResult.error(-1, "客户(公司)名称,已存在");
        }
        String userName = form.getUserName();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        queryWrapper.ne("id", id);
        int userNameCount = this.count(queryWrapper);
        if(userNameCount > 0){
            return CommonResult.error(-1, "客户(公司)名称,已存在");
        }
        String phone = form.getPhone();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.ne("id", id);
        int phoneCount = this.count(queryWrapper);
        if(phoneCount > 0){
            return CommonResult.error(-1, "手机号,已存在");
        }
        this.saveOrUpdate(customer);
        return CommonResult.success("保存客户，成功！");
    }

    @Override
    public CommonResult<CustomerVO> auditCustomer(CustomerForm form) {
        Customer customer = ConvertUtil.convert(form, Customer.class);
        this.saveOrUpdate(customer);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        return CommonResult.success(customerVO);
    }


}
