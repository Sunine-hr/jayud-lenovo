package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerTypeForm;
import com.jayud.scm.model.po.CustomerClass;
import com.jayud.scm.mapper.CustomerClassMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.service.ICustomerClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemActionService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户财务编号表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerClassServiceImpl extends ServiceImpl<CustomerClassMapper, CustomerClass> implements ICustomerClassService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<CustomerClass> getCustomerClassByCustomerId(Integer id) {
        QueryWrapper<CustomerClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerClass::getCustomerId,id);
        queryWrapper.lambda().eq(CustomerClass::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public CustomerClass getCustomerClassByCustomerIdAndClassName(Integer id, String s) {
        QueryWrapper<CustomerClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerClass::getCustomerId,id);
        queryWrapper.lambda().eq(CustomerClass::getClassName,s);
        queryWrapper.lambda().eq(CustomerClass::getVoided,0);
        return this.getOne(queryWrapper);

    }

    @Override
    public boolean updateCustomerClass(AddCustomerTypeForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<CustomerClass> customerClasses = ConvertUtil.convertList(form.getAddCustomerClassForms(), CustomerClass.class);
        for (CustomerClass customerClass : customerClasses) {
            customerClass.setCustomerId(form.getId());
            customerClass.setCrtBy(systemUser.getId().intValue());
            customerClass.setCrtByDtm(LocalDateTime.now());
            customerClass.setCrtByName(UserOperator.getToken());
        }
        boolean b = this.saveOrUpdateBatch(customerClasses);
        if(b){
            log.warn("客户类型设置成功");
        }
        return b;
    }

    @Override
    public boolean financialNumberSetting(AddCustomerTypeForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<CustomerClass> customerClasses = ConvertUtil.convertList(form.getAddCustomerClassForms(), CustomerClass.class);
        for (CustomerClass customerClass : customerClasses) {
            customerClass.setCustomerId(form.getId());
            customerClass.setMdyBy(systemUser.getId().intValue());
            customerClass.setMdyByDtm(LocalDateTime.now());
            customerClass.setMdyByName(UserOperator.getToken());
        }
        boolean b = this.saveOrUpdateBatch(customerClasses);
        if(b){
            log.warn("财务编号设置成功");
        }
        return b;
    }

}
