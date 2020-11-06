package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.CustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CustomerAddressVO;

/**
 * <p>
 * 客户地址 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
public interface ICustomerAddressService extends IService<CustomerAddress> {

    /**
     * 分页查询客户地址
     * @param form
     * @return
     */
    IPage<CustomerAddressVO> findCustomerAddressByPage(QueryCustomerAddressForm form);

    /**
     * 新增编辑客户地址
     * @param customerAddress
     * @return
     */
    boolean saveOrUpdateCustomerAddress(CustomerAddress customerAddress);
}
