package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerFormVO;
import com.jayud.scm.model.vo.CustomerVO;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerService extends IService<Customer> {

    IPage<CustomerFormVO> findByPage(QueryCustomerForm form);

    Customer getCustomer(String customerName);

    boolean addCustomer(AddCustomerNameForm form);

    CustomerVO getCustomerById(Integer id);

    boolean updateCustomer(AddCustomerForm form);

    boolean delete(DeleteForm deleteForm);

    boolean updateCustomerName(Customer customer);

    boolean AddCustomerFollow(AddCustomerFollowForm followForm);
}
