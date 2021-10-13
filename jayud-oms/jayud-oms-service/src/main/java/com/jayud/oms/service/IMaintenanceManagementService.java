package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddMaintenanceManagementForm;
import com.jayud.oms.model.bo.QueryMaintenanceManagementForm;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.MaintenanceManagementVO;

/**
 * <p>
 * 维修管理 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
public interface IMaintenanceManagementService extends IService<MaintenanceManagement> {

    void saveOrUpdate(AddMaintenanceManagementForm form);

    IPage<MaintenanceManagementVO> findByPage(QueryMaintenanceManagementForm form);
}
