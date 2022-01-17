package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.po.VFeeModel;
import com.jayud.scm.model.vo.CustomerFormVO;
import com.jayud.scm.model.vo.CustomerOperatorVO;
import com.jayud.scm.model.vo.CustomerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerService extends IService<Customer> {

    IPage<CustomerFormVO> findByPage(QueryCustomerForm form);

    Customer getCustomer(String customerName);

    boolean addCustomer(AddCustomerNameForm form);

    CustomerVO getCustomerById(Integer id);

    boolean updateCustomer(AddCustomerForm form);

    boolean delete(DeleteForm deleteForm);

    boolean updateCustomerName(Customer customer);

    boolean AddCustomerFollow(AddCustomerFollowForm followForm);

    CommonResult toExamine(PermissionForm form);

    CommonResult deApproval(PermissionForm form);

    /**
     * 根据客户id，查询结算方案(结算条款)
     * @param customerId
     * @return
     */
    List<VFeeModel> findVFeeModelByCustomerId(Integer customerId,Integer modelType);

    /**
     * 根据客户id，查询客户的操作人员list信息（`商务员`、`业务员`、`客户下单人`）
     * @param customerId
     * @return
     */
    CustomerOperatorVO findCustomerOperatorByCustomerId(Integer customerId,Integer modelType);

    Map getClassById(QueryCommonForm form);

    List<CustomerVO> getCustomerByClassType(String classType);
}
