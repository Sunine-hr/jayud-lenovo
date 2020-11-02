package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tms.mapper.DeliveryAddressMapper;
import com.jayud.tms.model.bo.QueryDeliveryAddressForm;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.vo.DeliveryAddressVO;
import com.jayud.tms.service.IDeliveryAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提货地址基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Service
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements IDeliveryAddressService {

    @Override
    public List<DeliveryAddressVO> findDeliveryAddress(QueryDeliveryAddressForm form) {
        return baseMapper.findDeliveryAddress(form);
    }
}
