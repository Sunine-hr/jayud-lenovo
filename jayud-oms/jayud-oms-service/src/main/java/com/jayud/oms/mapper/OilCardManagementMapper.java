package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.po.OilCardManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 油卡管理 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
public interface OilCardManagementMapper extends BaseMapper<OilCardManagement> {

    IPage<OilCardManagementVO> findByPage(@Param("page") Page<OilCardManagementVO> page, @Param("form") QueryOilCardManagementForm form);
}
