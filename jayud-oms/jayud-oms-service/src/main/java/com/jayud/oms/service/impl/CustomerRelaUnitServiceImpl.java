package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.oms.mapper.CustomerRelaUnitMapper;
import com.jayud.oms.model.bo.ConfirmRelateUnitForm;
import com.jayud.oms.model.bo.CustomerInfoForm;
import com.jayud.oms.model.po.CustomerRelaUnit;
import com.jayud.oms.service.ICustomerRelaUnitService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
@Service
public class CustomerRelaUnitServiceImpl extends ServiceImpl<CustomerRelaUnitMapper, CustomerRelaUnit> implements ICustomerRelaUnitService {

    @Override
    public Boolean confirmRelateUnit(ConfirmRelateUnitForm form) {
        List<CustomerRelaUnit> customerRelaUnitList = new ArrayList<>();
        for (CustomerInfoForm customerInfoForm : form.getUnitInfos()) {
            CustomerRelaUnit customerRelaUnit = new CustomerRelaUnit();
            customerRelaUnit.setCustomerInfoId(form.getCustomerInfoId());
            customerRelaUnit.setUnitId(customerInfoForm.getId());
            customerRelaUnit.setCreatedUser(form.getLoginUserName());
            customerRelaUnitList.add(customerRelaUnit);
        }
        return saveBatch(customerRelaUnitList);
    }

    @Override
    public void saveBatchRelaUnit(Long customerId, List<Long> unitCodeIds) {
        if (CollectionUtils.isEmpty(unitCodeIds)) {
            return;
        }
        List<CustomerRelaUnit> customerRelaUnitList = new ArrayList<>();
        for (Long unitCodeId : unitCodeIds) {
            CustomerRelaUnit customerRelaUnit = new CustomerRelaUnit();
            customerRelaUnit.setCustomerInfoId(customerId);
            customerRelaUnit.setUnitId(unitCodeId);
            customerRelaUnit.setCreatedUser(UserOperator.getToken());
            customerRelaUnitList.add(customerRelaUnit);
        }
        saveBatch(customerRelaUnitList);
    }
}
