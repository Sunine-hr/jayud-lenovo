package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerTaxForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.CustomerTaxMapper;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ICustomerTaxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户开票资料 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerTaxServiceImpl extends ServiceImpl<CustomerTaxMapper, CustomerTax> implements ICustomerTaxService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public CustomerTax getCustomerTaxByCustomerId(Integer id) {
        QueryWrapper<CustomerTax> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerTax::getCustomerId,id);
        queryWrapper.lambda().eq(CustomerTax::getVoided,0);
        queryWrapper.lambda().eq(CustomerTax::getIsDefaultValue,1);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<CustomerTaxVO> findByPage(QueryCommonForm form) {
        Page<CustomerTaxVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerTax(AddCustomerTaxForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();
//        Customer customer = new Customer();

        CustomerTax customerTax = ConvertUtil.convert(form, CustomerTax.class);
        if(form.getId() != null){
            customerTax.setMdyBy(systemUser.getId().intValue());
            customerTax.setMdyByDtm(LocalDateTime.now());
            customerTax.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"修改开票资料"+customerTax.getTaxName());
        }else {
            customerTax.setCrtBy(systemUser.getId().intValue());
            customerTax.setCrtByDtm(LocalDateTime.now());
            customerTax.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"增加开票资料"+customerTax.getTaxName());
        }
        boolean update = this.saveOrUpdate(customerTax);
        if(update){

//            customer.setId(form.getCustomerId());
//            customer.setTaxNo(form.getTaxNo());
//            boolean update1 = customerService.updateById(customer);
//            if(update1){
//                log.warn("修改客户税号为"+form.getTaxNo());
//            }

            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加或修改开票资料，客户操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean modifyDefaultValues(AddCustomerTaxForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取原来为默认值的值
        QueryWrapper<CustomerTax> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerTax::getCustomerId,form.getCustomerId());
        queryWrapper.lambda().eq(CustomerTax::getIsDefaultValue,1);
        CustomerTax one = this.getOne(queryWrapper);

        CustomerTax customerTax = ConvertUtil.convert(form, CustomerTax.class);
        customerTax.setMdyBy(systemUser.getId().intValue());
        customerTax.setMdyByDtm(LocalDateTime.now());
        customerTax.setMdyByName(systemUser.getUserName());

        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCustomerId(form.getCustomerId());
        customerFollow.setFollowContext(systemUser.getUserName()+"修改联系人的默认值为"+customerTax.getTaxName());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(systemUser.getUserName());
        boolean update = this.updateById(customerTax);
        if(update){
            log.warn("修改默认值成功");
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("修改开票资料默认值，客户操作日志添加成功");
            }
            //修改成功
            if(one != null){
                one.setIsDefaultValue(0);
                this.updateById(one);
            }
        }

        return update;
    }

    @Override
    public CustomerTaxVO getCustomerTaxById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerTaxVO.class);
    }
}
