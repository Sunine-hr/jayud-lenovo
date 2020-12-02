package com.jayud.oms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.model.vo.CustAccountVO;

import java.util.List;
import java.util.Map;

/**
 * 客户信息
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * 客户列表分页查询
     *
     * @param from
     * @return
     */
    IPage<CustomerInfoVO> findCustomerInfoByPage(QueryCustomerInfoForm from);

    /**
     * 根据id获取客户信息
     *
     * @param id
     * @return
     */
    CustomerInfoVO getCustomerInfoById(Long id);

    /**
     * 根据条件获取客户
     *
     * @return
     */
    List<CustomerInfo> findCustomerInfoByCondition(Map<String, Object> param);


    /**
     * 获取客户账号
     *
     * @param param
     * @return
     */
    CustAccountVO getCustAccountByCondition(Map<String, Object> param);

    /**
     * 客户账户列表分页查询
     *
     * @param form
     * @return
     */
    IPage<CustAccountVO> findCustAccountByPage(QueryCusAccountForm form);

    /**
     * 分页查询客户基本信息
     */
    IPage<CustomerInfoVO> findCustomerBasicsInfoByPage(QueryCustomerInfoForm form);

    /**
     * 根据客户CODE获取结算单位
     * @param idCode
     * @return
     */
    List<CustomerInfoVO> findUnitInfoByCode(String idCode);
}
