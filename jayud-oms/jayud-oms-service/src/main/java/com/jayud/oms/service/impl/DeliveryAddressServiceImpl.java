package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DeliveryAddress;
import com.jayud.oms.mapper.DeliveryAddressMapper;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.service.IDeliveryAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 提货地址基础数据表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-12
 */
@Service
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements IDeliveryAddressService {

    @Override
    public IPage<CustomerAddressVO> findCustomerAddressByPage(QueryCustomerAddressForm form) {
        Page<CustomerAddressVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findCustomerAddressByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerAddress(DeliveryAddress deliveryAddress) {
        if (Objects.isNull(deliveryAddress.getId())) {
            deliveryAddress.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken())
                    .setStatus(Integer.valueOf(StatusEnum.ENABLE.getCode()));
            return this.save(deliveryAddress);
        } else {
            return this.updateById(deliveryAddress);
        }
    }
}
