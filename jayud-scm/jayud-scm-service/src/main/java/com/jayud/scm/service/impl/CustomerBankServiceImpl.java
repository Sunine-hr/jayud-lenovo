package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerBankForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CustomerBank;
import com.jayud.scm.mapper.CustomerBankMapper;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerBankVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户银行表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerBankServiceImpl extends ServiceImpl<CustomerBankMapper, CustomerBank> implements ICustomerBankService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    /**
     * 分页查询
     * @param form
     * @return
     */
    @Override
    public IPage<CustomerBankVO> findByPage(QueryCommonForm form) {
        Page<CustomerBankVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    /**
     * 修改或保存银行资料
     * @param form
     * @return
     */
    @Override
    public boolean saveOrUpdateCustomerBank(AddCustomerBankForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        CustomerBank customerBank = ConvertUtil.convert(form, CustomerBank.class);
        if(form.getId() != null){
            customerBank.setMdyBy(systemUser.getId().intValue());
            customerBank.setMdyByDtm(LocalDateTime.now());
            customerBank.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"修改银行资料");
        }else {
            customerBank.setCrtBy(systemUser.getId().intValue());
            customerBank.setCrtByDtm(LocalDateTime.now());
            customerBank.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"增加银行资料");
        }
        boolean update = this.saveOrUpdate(customerBank);
        if(update){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("银行资料增加，客户操作日志成功");
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
    public boolean modifyDefaultValues(AddCustomerBankForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取原来为默认值的值
        QueryWrapper<CustomerBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerBank::getCustomerId,form.getCustomerId());
        queryWrapper.lambda().eq(CustomerBank::getIsDefault,1);
        CustomerBank one = this.getOne(queryWrapper);

        CustomerBank customerBank = ConvertUtil.convert(form, CustomerBank.class);
        customerBank.setMdyBy(systemUser.getId().intValue());
        customerBank.setMdyByDtm(LocalDateTime.now());
        customerBank.setMdyByName(systemUser.getUserName());

        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCustomerId(form.getCustomerId());
        customerFollow.setFollowContext(UserOperator.getToken()+"修改银行资料的默认值为"+customerBank.getBankName());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(systemUser.getUserName());
        boolean update = this.updateById(customerBank);
        if(update){
            log.warn("修改默认值成功");
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("客户修改联系人默认值，日志添加成功");
            }
            //修改成功
            if(one != null){
                one.setIsDefault(0);
                this.updateById(one);
            }
        }
        return update;
    }

    /**
     * 根据id获取银行信息
     * @param id
     * @return
     */
    @Override
    public CustomerBankVO getCustomerBankById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerBankVO.class);
    }
}
