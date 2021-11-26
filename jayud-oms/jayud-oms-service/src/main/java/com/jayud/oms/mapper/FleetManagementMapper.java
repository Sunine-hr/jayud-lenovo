package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryFleetManagementForm;
import com.jayud.oms.model.po.FleetManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.FleetManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 车队管理 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Mapper
public interface FleetManagementMapper extends BaseMapper<FleetManagement> {

    IPage<FleetManagementVO> findByPage(@Param("page") Page<FleetManagement> page, @Param("form") QueryFleetManagementForm form);

}
