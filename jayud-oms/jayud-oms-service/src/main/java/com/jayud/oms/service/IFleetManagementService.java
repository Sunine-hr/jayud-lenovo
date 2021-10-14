package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddFleetManagementForm;
import com.jayud.oms.model.bo.QueryFleetManagementForm;
import com.jayud.oms.model.po.FleetManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.FleetManagementVO;

/**
 * <p>
 * 车队管理 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
public interface IFleetManagementService extends IService<FleetManagement> {

    void saveOrUpdate(AddFleetManagementForm form);

    IPage<FleetManagementVO> findByPage(QueryFleetManagementForm form);

    boolean enableOrDisable(Long id);

    String autoGenerateNum();

}
