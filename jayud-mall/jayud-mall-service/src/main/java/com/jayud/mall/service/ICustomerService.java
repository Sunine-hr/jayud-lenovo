package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.Customer;
import com.jayud.mall.model.vo.CustomerVO;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 分页查询客户
     * @param form
     * @return
     */
    IPage<CustomerVO> findCustomerByPage(QueryCustomerForm form);

    /**
     * 保存
     * @param form
     * @return
     */
    CommonResult saveCustomer(CustomerEditForm form);

    /**
     * 审核-客户
     * @param form
     * @return
     */
    CommonResult<CustomerVO> auditCustomer(CustomerAuditForm form);

    /**
     * 客户注册
     * @param form
     * @return
     */
    CommonResult<CustomerVO> customerRegister(CustomerRegisterForm form);

    /**
     * 忘记密码-客户验证
     * @param form
     * @return
     */
    CommonResult customerVerify(CustomerVerifyForm form);

    /**
     * 修改密码-客户确认密码
     * @param form
     * @return
     */
    CommonResult customerUpdatePwd(CustomerPwdForm form);
}
