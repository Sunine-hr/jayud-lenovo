package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.vo.VehicleDetailsVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.model.vo.VehicleSizeInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据司机查询车辆信息
     *
     * @param driverName
     * @return
     */
    List<VehicleInfoVO> findTmsVehicle();

    /**
     * 获取所有的车型尺寸
     *
     * @return
     */
    List<VehicleSizeInfoVO> findVehicleSize();

    /**
     * @return
     */
    VehicleDetailsVO getVehicleDetailsById(@Param("vehicleId") Long vehicleId);


    /**
     * 根据车牌集合获取车辆和供应商信息
     *
     * @return
     */
    List<VehicleDetailsVO> getDetailsByPlateNum(@Param("plateNumbers") List<String> plateNumbers);
}

