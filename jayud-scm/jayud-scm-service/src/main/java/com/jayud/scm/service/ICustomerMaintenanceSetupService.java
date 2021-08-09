package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerMaintenanceSetupForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerMaintenanceSetup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerMaintenanceSetupVO;

/**
 * <p>
 * 客户维护人表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-07
 */
public interface ICustomerMaintenanceSetupService extends IService<CustomerMaintenanceSetup> {

    IPage<CustomerMaintenanceSetupVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerMaintenanceSetup(AddCustomerMaintenanceSetupForm form);

    CustomerMaintenanceSetupVO getCustomerMaintenanceSetupById(Integer id);
}
