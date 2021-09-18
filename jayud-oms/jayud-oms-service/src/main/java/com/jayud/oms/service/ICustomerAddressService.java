package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.CustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CustomerAddrVO;
import com.jayud.oms.model.vo.CustomerAddressVO;

/**
 * <p>
 * 客户地址 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-18
 */
public interface ICustomerAddressService extends IService<CustomerAddress> {


    IPage<CustomerAddrVO> findCustomerAddressByPage(QueryCustomerAddressForm form);

    public Long saveOrUpdateAddr(CustomerAddress customerAddress);
}
