package com.jayud.tms.service.impl;

import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.mapper.DeliveryAddressMapper;
import com.jayud.tms.service.IDeliveryAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提货地址基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements IDeliveryAddressService {

}
