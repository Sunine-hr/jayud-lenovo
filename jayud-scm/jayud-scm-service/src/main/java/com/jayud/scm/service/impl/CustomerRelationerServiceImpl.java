package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.mapper.CustomerRelationerMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.SystemRoleActionVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ICustomerRelationerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户联系人表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerRelationerServiceImpl extends ServiceImpl<CustomerRelationerMapper, CustomerRelationer> implements ICustomerRelationerService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Override
    public IPage<CustomerRelationerVO> findByPage(QueryCommonForm form) {
        Page<CustomerRelationerVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerRelationer(AddCustomerRelationerForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        CustomerRelationer customerRelationer = ConvertUtil.convert(form, CustomerRelationer.class);
        if(form.getId() != null){
            if(customerRelationer.getIsDefault().equals(1)){
                QueryWrapper<CustomerRelationer> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(CustomerRelationer::getCustomerId,form.getCustomerId());
                queryWrapper.lambda().eq(CustomerRelationer::getSType,form.getSType());
                queryWrapper.lambda().eq(CustomerRelationer::getIsDefault,1);
                CustomerRelationer one = this.getOne(queryWrapper);
                if(one != null){
                    customerRelationer.setIsDefault(0);
                }
            }
            customerRelationer.setMdyBy(systemUser.getId().intValue());
            customerRelationer.setMdyByDtm(LocalDateTime.now());
            customerRelationer.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(systemUser.getUserName()+"修改联系人");
        }else {
            customerRelationer.setCrtBy(systemUser.getId().intValue());
            customerRelationer.setCrtByDtm(LocalDateTime.now());
            customerRelationer.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(systemUser.getUserName()+"增加联系人");
        }
        boolean update = this.saveOrUpdate(customerRelationer);
        if(update){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加客户操作日志成功");
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
    public boolean modifyDefaultValues(AddCustomerRelationerForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取原来为默认值的值
        QueryWrapper<CustomerRelationer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerRelationer::getCustomerId,form.getCustomerId());
        queryWrapper.lambda().eq(CustomerRelationer::getSType,form.getSType());
        queryWrapper.lambda().eq(CustomerRelationer::getIsDefault,1);
        CustomerRelationer one = this.getOne(queryWrapper);

        CustomerRelationer customerRelationer = ConvertUtil.convert(form, CustomerRelationer.class);
        customerRelationer.setMdyBy(systemUser.getId().intValue());
        customerRelationer.setMdyByDtm(LocalDateTime.now());
        customerRelationer.setMdyByName(UserOperator.getToken());

        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCustomerId(form.getCustomerId());
        customerFollow.setFollowContext(UserOperator.getToken()+"修改联系人的默认值为"+customerRelationer.getCName());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(UserOperator.getToken());
        boolean update = this.updateById(customerRelationer);
        if(update){
            log.warn("修改默认值成功");
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加客户修改联系人默认值日志成功");
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
    public CustomerRelationerVO getCustomerRelationerById(Integer id) {
        return ConvertUtil.convert(this.getById(id),CustomerRelationerVO.class);
    }

    @Override
    public List<CustomerRelationer> getCustomerRelationerByCustomerIdAndType(Integer id, String s) {
        QueryWrapper<CustomerRelationer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerRelationer::getCustomerId,id);
        queryWrapper.lambda().eq(CustomerRelationer::getSType,s);
        queryWrapper.lambda().eq(CustomerRelationer::getVoided,0);
        return this.list(queryWrapper);
    }


}
