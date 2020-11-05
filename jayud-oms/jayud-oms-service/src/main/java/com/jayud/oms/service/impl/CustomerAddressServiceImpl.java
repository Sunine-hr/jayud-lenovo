package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CustomerAddress;
import com.jayud.oms.mapper.CustomerAddressMapper;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.service.ICustomerAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 客户地址 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

    @Override
    public IPage<CustomerAddressVO> findCustomerAddressByPage(QueryCustomerAddressForm form) {
        Page<CustomerAddressVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findCustomerAddressByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerAddress(CustomerAddress customerAddress) {
        if (Objects.isNull(customerAddress.getId())) {
            customerAddress.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken()).setStatus(StatusEnum.ENABLE.getCode());
            return this.save(customerAddress);
        } else {
            customerAddress
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(customerAddress);
        }
    }
}
