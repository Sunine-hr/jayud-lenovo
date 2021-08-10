package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerAddressForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CustomerAddress;
import com.jayud.scm.mapper.CustomerAddressMapper;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerAddressVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ISystemUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户常用地址表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Override
    public IPage<CustomerAddressVO> findByPage(QueryCommonForm form) {
        Page<CustomerAddressVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerAddress(AddCustomerAddressForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CustomerFollow customerFollow = new CustomerFollow();

        CustomerAddress customerAddress = ConvertUtil.convert(form, CustomerAddress.class);
        if(form != null){
            customerAddress.setMdyBy(systemUser.getId().intValue());
            customerAddress.setMdyByDtm(LocalDateTime.now());
            customerAddress.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"修改地址类型");
        }else {
            customerAddress.setCrtBy(systemUser.getId().intValue());
            customerAddress.setCrtByDtm(LocalDateTime.now());
            customerAddress.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"增加地址类型");
        }
        boolean update = this.saveOrUpdate(customerAddress);
        if(update){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加地址类型，客户操作日志成功");
            }
        }
        return update;
    }

    /**
     * 修改默认值
     * @param form
     * @return
     */
    @Override
    public boolean modifyDefaultValues(AddCustomerAddressForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取原来为默认值的值
        QueryWrapper<CustomerAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerAddress::getCustomerId,form.getCustomerId());
        queryWrapper.lambda().eq(CustomerAddress::getSType,form.getSType());
        queryWrapper.lambda().eq(CustomerAddress::getIsDefault,1);
        CustomerAddress one = this.getOne(queryWrapper);

        CustomerAddress customerAddress = ConvertUtil.convert(form, CustomerAddress.class);
        customerAddress.setMdyBy(systemUser.getId().intValue());
        customerAddress.setMdyByDtm(LocalDateTime.now());
        customerAddress.setMdyByName(systemUser.getUserName());

        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCustomerId(form.getCustomerId());
        customerFollow.setFollowContext(systemUser.getUserName()+"修改地址的默认值为"+customerAddress.getCName()+customerAddress.getAddress());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(systemUser.getUserName());
        boolean update = this.updateById(customerAddress);
        if(update){
            log.warn("修改地址类型默认值成功");
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("客户修改地址类型默认值日志添加成功");
            }
            //修改成功
            if(one != null){
                one.setIsDefault(0);
                this.updateById(one);
            }
        }

        return update;
    }

    @Override
    public CustomerAddressVO getCustomerAddressById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerAddressVO.class);
    }
}
