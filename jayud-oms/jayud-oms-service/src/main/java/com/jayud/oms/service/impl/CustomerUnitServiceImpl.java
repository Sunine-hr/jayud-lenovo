package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.oms.model.po.CustomerUnit;
import com.jayud.oms.mapper.CustomerUnitMapper;
import com.jayud.oms.service.ICustomerUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户结算单位 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-23
 */
@Service
public class CustomerUnitServiceImpl extends ServiceImpl<CustomerUnitMapper, CustomerUnit> implements ICustomerUnitService {

    @Override
    public void saveOrUpdateUnit(CustomerUnit customerUnit) {
        if (customerUnit.getId() == null) {
            customerUnit.setCreateTime(LocalDateTime.now());
            customerUnit.setCreateUser(UserOperator.getToken());
        } else {
            customerUnit.setUpdateTime(LocalDateTime.now());
            customerUnit.setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(customerUnit);
    }

    @Override
    public boolean checkUnique(Long id, Long customerId, String businessType, Long optDepartmentId) {
        QueryWrapper<CustomerUnit> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerUnit::getCustomerId, customerId)
                .eq(CustomerUnit::getBusinessType, businessType)
                .eq(CustomerUnit::getOptDepartmentId, optDepartmentId)
                .eq(CustomerUnit::getStatus, StatusEnum.ENABLE.getCode());
        if (id != null) {
            condition.lambda().ne(CustomerUnit::getId, id);
        }
        return this.count(condition) == 0;
    }

    @Override
    public List<CustomerUnit> getByCondition(CustomerUnit customerUnit) {
        return this.baseMapper.selectList(new QueryWrapper<>(customerUnit));
    }
}
