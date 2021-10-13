package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryMaintenanceManagementForm;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.MaintenanceManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 维修管理 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Mapper
public interface MaintenanceManagementMapper extends BaseMapper<MaintenanceManagement> {

    IPage<MaintenanceManagementVO> findByPage(@Param("page") Page<MaintenanceManagementVO> page,
                                              @Param("form") QueryMaintenanceManagementForm form);
}
