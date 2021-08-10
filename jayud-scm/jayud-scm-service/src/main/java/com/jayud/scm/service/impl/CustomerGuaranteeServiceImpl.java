package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerGuaranteeForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.model.po.CustomerGuarantee;
import com.jayud.scm.mapper.CustomerGuaranteeMapper;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ICustomerGuaranteeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 担保合同 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class CustomerGuaranteeServiceImpl extends ServiceImpl<CustomerGuaranteeMapper, CustomerGuarantee> implements ICustomerGuaranteeService {

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public IPage<CustomerGuaranteeVO> findByPage(QueryCommonForm form) {
        Page<CustomerGuaranteeVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerGuarantee(AddCustomerGuaranteeForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        CustomerGuarantee customerGuarantee = ConvertUtil.convert(form, CustomerGuarantee.class);
        if(form != null){
            customerGuarantee.setMdyBy(systemUser.getId().intValue());
            customerGuarantee.setMdyByDtm(LocalDateTime.now());
            customerGuarantee.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(systemUser.getUserName()+"修改担保合同");
        }else {
            customerGuarantee.setCrtBy(systemUser.getId().intValue());
            customerGuarantee.setCrtByDtm(LocalDateTime.now());
            customerGuarantee.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(systemUser.getUserName()+"增加担保合同");
        }
        boolean update = this.saveOrUpdate(customerGuarantee);
        if(update){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加担保合同，客户操作日志增加成功");
            }
        }
        return update;
    }

    @Override
    public CustomerGuaranteeVO getCustomerGuaranteeById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerGuaranteeVO.class);
    }
}
