package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerMaintenanceSetupForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.CustomerMaintenanceSetupMapper;
import com.jayud.scm.model.vo.CustomerMaintenanceSetupVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ICustomerMaintenanceSetupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户维护人表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-07
 */
@Service
public class CustomerMaintenanceSetupServiceImpl extends ServiceImpl<CustomerMaintenanceSetupMapper, CustomerMaintenanceSetup> implements ICustomerMaintenanceSetupService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public IPage<CustomerMaintenanceSetupVO> findByPage(QueryCommonForm form) {
        Page<CustomerMaintenanceSetupVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerMaintenanceSetup(AddCustomerMaintenanceSetupForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        CustomerMaintenanceSetup customerMaintenanceSetup = ConvertUtil.convert(form, CustomerMaintenanceSetup.class);
        if(form.getId() != null){
            customerMaintenanceSetup.setMdyBy(systemUser.getId().intValue());
            customerMaintenanceSetup.setMdyByDtm(LocalDateTime.now());
            customerMaintenanceSetup.setMdyByName(UserOperator.getToken());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"修改维护人信息");
        }else {
            customerMaintenanceSetup.setCrtBy(systemUser.getId().intValue());
            customerMaintenanceSetup.setCrtByDtm(LocalDateTime.now());
            customerMaintenanceSetup.setCrtByName(UserOperator.getToken());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"增加维护人信息");
        }
        boolean update = this.saveOrUpdate(customerMaintenanceSetup);

        if(update){


            if(form.getRoleName().equals("业务")){
                Customer customer = new Customer();
                customer.setId(customerMaintenanceSetup.getCustomerId());
                customer.setFsalesId(customerMaintenanceSetup.getWhUserId());
                customer.setFsalesMan(customerMaintenanceSetup.getWhUserName());
                this.customerService.updateById(customer);
                log.warn("客户跟单业务员修改成功");
            }
            if(form.getRoleName().equals("商务")){
                Customer customer = new Customer();
                customer.setId(customerMaintenanceSetup.getCustomerId());
                customer.setFollowerId(customerMaintenanceSetup.getWhUserId());
                customer.setFollowerName(customerMaintenanceSetup.getWhUserName());
                this.customerService.updateById(customer);
                log.warn("客户跟单商务修改成功");
            }

            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(UserOperator.getToken());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加或修改维护人信息，增加客户操作日志成功");
            }
        }
        return update;
    }

    @Override
    public CustomerMaintenanceSetupVO getCustomerMaintenanceSetupById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerMaintenanceSetupVO.class);
    }
}
