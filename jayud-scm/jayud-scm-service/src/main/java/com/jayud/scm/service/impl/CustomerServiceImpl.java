package com.jayud.scm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.CustomerMaintenanceSetupMapper;
import com.jayud.scm.mapper.CustomerMapper;
import com.jayud.scm.mapper.CustomerRelationerMapper;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerClassService customerClassService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private ICommodityService commodityService;


    @Autowired
    CustomerRelationerMapper customerRelationerMapper;//客户联系人表
    @Autowired
    CustomerMaintenanceSetupMapper customerMaintenanceSetupMapper;//客户维护人表


    @Override
    public IPage<CustomerFormVO> findByPage(QueryCustomerForm form) {
        Page<CommodityFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public Customer getCustomer(String customerName) {

        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Customer::getCustomerName,customerName);
        queryWrapper.lambda().eq(Customer::getVoided,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean addCustomer(AddCustomerNameForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Customer customer = ConvertUtil.convert(form, Customer.class);
        customer.setCrtBy(systemUser.getId().intValue());
        customer.setCrtByDtm(LocalDateTime.now());
        customer.setCrtByName(systemUser.getUserName());
        customer.setCustomerNo(commodityService.getOrderNo("1002",LocalDateTime.now()));
        boolean save = this.save(customer);

        if(save){
            log.warn("客户添加成功");
            CustomerClass customerClass = new CustomerClass();
            customerClass.setCustomerId(customer.getId());
            customerClass.setClassName(form.getType() != null ? form.getType() : "客户");
            customerClass.setCrtBy(systemUser.getId().intValue());
            customerClass.setCrtByDtm(LocalDateTime.now());
            customerClass.setCrtByName(systemUser.getUserName());
            boolean save1 = this.customerClassService.save(customerClass);
            if(save1){
                log.warn("客户财务编号添加成功");
            }

            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            customerFollow.setCustomerId(customer.getId());
            customerFollow.setFollowContext(OperationEnum.INSERT.getCode());
            customerFollow.setSType("客户表"+OperationEnum.INSERT.getDesc()+customer.getId());
            boolean save2 = this.customerFollowService.save(customerFollow);
            if(save2){
                log.warn("客户操作日志添加成功");
            }
            return true;
        }
        return false;
    }

    @Override
    public CustomerVO getCustomerById(Integer id) {
        Customer customer = this.baseMapper.selectById(id);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        List<CustomerClass> customerClasses = customerClassService.getCustomerClassByCustomerId(customer.getId());
        if(CollectionUtils.isNotEmpty(customerClasses)){
            StringBuffer stringBuffer = new StringBuffer();
            for (CustomerClass customerClass : customerClasses) {
                stringBuffer.append(customerClass.getClassName()).append(",");
            }
            customerVO.setType(stringBuffer.toString().substring(0,stringBuffer.length()-1));

        }

        return customerVO;
    }

    @Override
    public boolean updateCustomer(AddCustomerForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Customer customer = ConvertUtil.convert(form, Customer.class);
        customer.setMdyBy(systemUser.getId().intValue());
        customer.setMdyByDtm(LocalDateTime.now());
        customer.setMdyByName(UserOperator.getToken());
        boolean update = this.updateById(customer);
        if(update){
            log.warn("客户修改成功");
            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            customerFollow.setCustomerId(customer.getId());
            customerFollow.setFollowContext(OperationEnum.UPDATE.getCode());
            customerFollow.setSType("客户表"+OperationEnum.UPDATE.getDesc()+customer.getId());
            boolean save2 = this.customerFollowService.save(customerFollow);
            if(save2){
                log.warn("客户操作日志添加成功");
            }

            List<CustomerClass> customerClasses = new ArrayList<>();
            for (String s : form.getType().split(",")) {
                CustomerClass customerClass = customerClassService.getCustomerClassByCustomerIdAndClassName(customer.getId(),s);
                if(customerClass == null){
                    CustomerClass customerClass1 = new CustomerClass();
                    customerClass1.setCustomerId(customer.getId());
                    customerClass1.setClassName(s);
                    customerClass1.setCrtBy(systemUser.getId().intValue());
                    customerClass1.setCrtByDtm(LocalDateTime.now());
                    customerClass1.setCrtByName(systemUser.getUserName());
                    customerClasses.add(customerClass1);
                }
            }
            boolean b = this.customerClassService.saveBatch(customerClasses);
            if(b){
                log.warn("客户财务编号添加成功");
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
//        List<Customer> customers = new ArrayList<>();
        List<CustomerFollow> customerFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
//            Customer customer = new Customer();
//            customer.setId(id.intValue());
//            customer.setVoided(1);
//            customer.setVoidedBy(deleteForm.getId().intValue());
//            customer.setVoidedByDtm(deleteForm.getDeleteTime());
//            customer.setVoidedByName(deleteForm.getName());
//            customers.add(customer);

            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setCustomerId(id.intValue());
            customerFollow.setSType(OperationEnum.DELETE.getCode());
            customerFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            customerFollow.setCrtBy(deleteForm.getId().intValue());
            customerFollow.setCrtByDtm(deleteForm.getDeleteTime());
            customerFollow.setCrtByName(deleteForm.getName());
            customerFollows.add(customerFollow);
        }
//        boolean b = this.updateBatchById(customers);
//        if(b){
//            log.warn("商品删除成功："+customers);
//            boolean b1 = customerFollowService.saveBatch(customerFollows);
//            if(!b1){
//                log.warn("操作记录表添加失败"+customerFollows);
//            }
//        }
        boolean b1 = customerFollowService.saveBatch(customerFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+customerFollows);
        }
        return b1;
    }

    @Override
    public boolean updateCustomerName(Customer customer) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        customer.setMdyBy(systemUser.getId().intValue());
        customer.setMdyByDtm(LocalDateTime.now());
        customer.setMdyByName(UserOperator.getToken());
        boolean update = this.updateById(customer);
        if(update){
            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            customerFollow.setCustomerId(customer.getId());
            customerFollow.setFollowContext(OperationEnum.UPDATE.getCode());
            customerFollow.setSType(systemUser.getUserName()+"修改客户名称为"+customer.getCustomerName());
            boolean save2 = this.customerFollowService.save(customerFollow);
            if(save2){
                log.warn("客户操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean AddCustomerFollow(AddCustomerFollowForm followForm) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Customer customer = new Customer();
        customer.setId(followForm.getCustomerId());
        customer.setCustomerState(followForm.getCustomerState());
        customer.setMdyBy(systemUser.getId().intValue());
        customer.setMdyByDtm(LocalDateTime.now());
        customer.setMdyByName(systemUser.getUserName());
        boolean update = this.saveOrUpdate(customer);
        if(update){
            log.warn("用户修改为"+followForm.getCustomerState()+"成功");
        }

        CustomerFollow customerFollow = ConvertUtil.convert(followForm, CustomerFollow.class);
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(systemUser.getUserName());
        customerFollow.setFollowContext("修改客户为"+followForm.getCustomerState()+",内容："+customerFollow.getFollowContext());

        boolean save = this.customerFollowService.save(customerFollow);
        if(!save){
            log.warn("商品操作日志添加失败"+customerFollow);
            return false;
        }
        return true;
    }

    /**
     * 审核
     * @param form
     * @return
     */
    @Override
    public CommonResult toExamine(PermissionForm form) {
        Map<String,Object> map = new HashMap<>();
        map.put("actionCode",form.getActionCode());
        map.put("table",form.getTable());
        map.put("id",form.getId());
        map.put("userId",form.getUserId());
        map.put("userName",form.getUserName());
        this.baseMapper.toExamine(map);
        if(map.get("state").equals(0)){
            return CommonResult.success();
        }else {
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }

    }

    /**
     * 反审
     * @param form
     * @return
     */
    @Override
    public CommonResult deApproval(PermissionForm form) {
        Map<String,Object> map = new HashMap<>();
        map.put("table",form.getTable());
        map.put("actionCode",form.getActionCode());
        map.put("id",form.getId());
        map.put("userId",form.getUserId());
        map.put("userName",form.getUserName());
        this.baseMapper.deApproval(map);
        if(map.get("state").equals(0)){
            return CommonResult.success();
        }else {
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
    }

    @Override
    public List<VFeeModel> findVFeeModelByCustomerId(Integer customerId) {
        List<VFeeModel> list = this.baseMapper.findVFeeModelByCustomerId(customerId);
        return list;
    }

    @Override
    public CustomerOperatorVO findCustomerOperatorByCustomerId(Integer customerId) {
        CustomerOperatorVO customerOperatorVO = new CustomerOperatorVO();
        Customer customer = this.getById(customerId);
        if(ObjectUtil.isEmpty(customer)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "客户不存在");
        }
        //商务员list
        String roleName = "商务";
        List<CustomerMaintenanceSetupVO> followerList = customerMaintenanceSetupMapper.findCustomerMaintenanceSetupBycustomerIdAndRoleName(customerId, roleName);
        customerOperatorVO.setFollowerList(followerList);
        //业务员list
        roleName = "业务";
        List<CustomerMaintenanceSetupVO> fsalesList = customerMaintenanceSetupMapper.findCustomerMaintenanceSetupBycustomerIdAndRoleName(customerId, roleName);
        customerOperatorVO.setFsalesList(fsalesList);
        //客户下单人list
        String stype = "3";//3 客户下单人
        List<CustomerRelationerVO> buyerList = customerRelationerMapper.findCustomerRelationerByCustomerIdAndStype(customerId, stype);
        customerOperatorVO.setBuyerList(buyerList);
        return customerOperatorVO;
    }
}
