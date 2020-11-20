package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.po.DeliveryAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.DeliveryAddressVO;

/**
 * <p>
 * 提货地址基础数据表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
public interface IDeliveryAddressService extends IService<DeliveryAddress> {

    /**
     * 提货、收货地址基础数据表接口
     * @param form
     * @return
     */
    IPage<DeliveryAddressVO> findDeliveryAddressByPage(QueryDeliveryAddressForm form);
}
