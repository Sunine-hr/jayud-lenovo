package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.mapper.VehicleInfoMapper;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.service.IVehicleInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 供应商对应车辆信息 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Service
public class VehicleInfoServiceImpl extends ServiceImpl<VehicleInfoMapper, VehicleInfo> implements IVehicleInfoService {


    /**
     * 分页查询车辆信息
     */
    @Override
    public IPage<VehicleInfoVO> findVehicleInfoByPage(QueryVehicleInfoForm form) {
        Page<VehicleInfo> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findVehicleInfoByPage(page, form);
    }

    /**
     * 新增编辑车辆信息
     *
     * @param vehicleInfo
     * @return
     */
    @Override
    public boolean saveOrUpdateVehicleInfo(VehicleInfo vehicleInfo) {

        if (Objects.isNull(vehicleInfo.getId())) {
            vehicleInfo.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(vehicleInfo);
        } else {
            vehicleInfo
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(vehicleInfo);
        }
    }

    /**
     * 是否存在车辆信息
     */
    @Override
    public boolean checkUnique(VehicleInfo vehicleInfo) {
        QueryWrapper<VehicleInfo> condition = new QueryWrapper<>();
        if (vehicleInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(VehicleInfo::getId, vehicleInfo.getId())
                    .eq(VehicleInfo::getPlateNumber, vehicleInfo.getPlateNumber()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(VehicleInfo::getPlateNumber, vehicleInfo.getPlateNumber());
        return this.count(condition) > 0;
    }


    /**
     * 根据状态获取车辆信息
     *
     * @param status
     * @return
     */
    @Override
    public List<VehicleInfo> getVehicleInfoByStatus(String status) {
        QueryWrapper<VehicleInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(VehicleInfo::getStatus, status);
        return this.baseMapper.selectList(condition);
    }


}
