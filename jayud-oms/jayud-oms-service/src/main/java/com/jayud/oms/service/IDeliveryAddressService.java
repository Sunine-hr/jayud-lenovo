package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.DeliveryAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CustomerAddressVO;

/**
 * <p>
 * 提货地址基础数据表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-12
 */
public interface IDeliveryAddressService extends IService<DeliveryAddress> {

    /**
     * 分页查询客户地址
     * @param form
     * @return
     */
    IPage<CustomerAddressVO> findCustomerAddressByPage(QueryCustomerAddressForm form);

    /**
     * 新增编辑客户地址
     * @return
     */
    boolean saveOrUpdateCustomerAddress(DeliveryAddress deliveryAddress);
}
