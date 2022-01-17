package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddCustomerTypeForm;
import com.jayud.scm.model.po.CustomerClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerVO;

import java.util.List;

/**
 * <p>
 * 客户财务编号表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerClassService extends IService<CustomerClass> {

    List<CustomerClass> getCustomerClassByCustomerId(Integer id);

    CustomerClass getCustomerClassByCustomerIdAndClassName(Integer id, String s);

    boolean updateCustomerClass(AddCustomerTypeForm form);

    boolean financialNumberSetting(AddCustomerTypeForm form);

}
