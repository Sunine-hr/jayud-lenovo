package com.jayud.oms.service;

import com.jayud.oms.model.po.CustomerUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 客户结算单位 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-23
 */
public interface ICustomerUnitService extends IService<CustomerUnit> {

    /**
     * @param customerUnit
     */
    void saveOrUpdateUnit(CustomerUnit customerUnit);

    boolean checkUnique(Long id, Long customerId, String businessType, String optDepartmentCode);

    List<CustomerUnit> getByCondition(CustomerUnit customerUnit);
}
