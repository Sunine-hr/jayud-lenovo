package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddOilCardManagementForm;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.po.OilCardManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.OilCardManagementVO;

/**
 * <p>
 * 油卡管理 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
public interface IOilCardManagementService extends IService<OilCardManagement> {

    void saveOrUpdate(AddOilCardManagementForm form);

    IPage<OilCardManagementVO> findByPage(QueryOilCardManagementForm form);

    boolean enableOrDisable(Long id);
}
