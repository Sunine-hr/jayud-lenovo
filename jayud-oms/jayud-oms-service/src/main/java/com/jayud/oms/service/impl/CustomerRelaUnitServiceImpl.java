package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.CustomerRelaUnitMapper;
import com.jayud.oms.model.bo.ConfirmRelateUnitForm;
import com.jayud.oms.model.bo.CustomerInfoForm;
import com.jayud.oms.model.po.CustomerRelaUnit;
import com.jayud.oms.service.ICustomerRelaUnitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
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
}
