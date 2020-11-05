package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.po.VehicleInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.VehicleInfoVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商对应车辆信息 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
public interface IVehicleInfoService extends IService<VehicleInfo> {

    /**
     * 分页查询车辆信息
     */
    IPage<VehicleInfoVO> findVehicleInfoByPage(QueryVehicleInfoForm form);

    /**
     * 新增编辑车辆信息
     * @param vehicleInfo
     * @return
     */
    boolean saveOrUpdateVehicleInfo(VehicleInfo vehicleInfo);

    /**
     * 是否存在车辆信息
     */
    boolean checkUnique(VehicleInfo vehicleInfo);

    /**
     * 根据状态获取车辆信息
     * @param status
     * @return
     */
    List<VehicleInfo> getVehicleInfoByStatus(String status);

    boolean enableOrDisableVehicle(Long id);
}
