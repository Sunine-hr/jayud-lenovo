package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerAgreementForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CustomerAgreement;
import com.jayud.scm.mapper.CustomerAgreementMapper;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerAgreementVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ISystemUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户协议表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerAgreementServiceImpl extends ServiceImpl<CustomerAgreementMapper, CustomerAgreement> implements ICustomerAgreementService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Override
    public IPage<CustomerAgreementVO> findByPage(QueryCommonForm form) {
        Page<CustomerAgreementVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerAgreement(AddCustomerAgreementForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        CustomerAgreement customerAgreement = ConvertUtil.convert(form, CustomerAgreement.class);
        if(form != null){
            customerAgreement.setMdyBy(systemUser.getId().intValue());
            customerAgreement.setMdyDtm(LocalDateTime.now());
            customerAgreement.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(UserOperator.getToken()+"修改客户协议");
        }else {
            customerAgreement.setCrtBy(systemUser.getId().intValue());
            customerAgreement.setCrtDtm(LocalDateTime.now());
            customerAgreement.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(systemUser.getUserName()+"增加客户协议");
        }
        boolean update = this.saveOrUpdate(customerAgreement);
        if(update){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加客户协议，客户操作日志增加成功");
            }
        }
        return update;
    }

    @Override
    public CustomerAgreementVO getCustomerAgreementById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerAgreementVO.class);
    }
}
