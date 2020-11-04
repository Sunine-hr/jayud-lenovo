package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.po.VehicleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.VehicleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 供应商对应车辆信息 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Mapper
public interface VehicleInfoMapper extends BaseMapper<VehicleInfo> {

    /**
     * 分页查询车辆信息
     */
    IPage<VehicleInfoVO> findVehicleInfoByPage(Page page, @Param("form") QueryVehicleInfoForm form);
}

