package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddCustomerInfoForm;
import com.jayud.oms.model.po.CustomerRelaLegal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
public interface ICustomerRelaLegalService extends IService<CustomerRelaLegal> {


    /**
     * 确认关联客户，即结算单位
     * @param form
     * @return
     */
    Boolean saveCusRelLegal(AddCustomerInfoForm form);
}
