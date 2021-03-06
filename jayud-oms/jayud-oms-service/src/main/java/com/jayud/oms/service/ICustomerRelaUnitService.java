package com.jayud.oms.service;

import com.jayud.oms.model.bo.ConfirmRelateUnitForm;
import com.jayud.oms.model.po.CustomerRelaUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
public interface ICustomerRelaUnitService extends IService<CustomerRelaUnit> {


    /**
     * 确认关联客户，即结算单位
     *
     * @param form
     * @return
     */
    Boolean confirmRelateUnit(ConfirmRelateUnitForm form);

    void saveBatchRelaUnit(Long customerId, List<Long> unitCodeIds);
}
