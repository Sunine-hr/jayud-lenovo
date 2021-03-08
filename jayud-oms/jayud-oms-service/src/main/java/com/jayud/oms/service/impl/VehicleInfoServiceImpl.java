package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.VehicleInfoMapper;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.model.vo.VehicleDetailsVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.model.vo.VehicleSizeInfoVO;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.IVehicleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @Autowired
    private IDriverInfoService driverInfoService;

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
            vehicleInfo
                    .setStatus(StatusEnum.ENABLE.getCode())
                    .setCreateTime(LocalDateTime.now())
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

    @Override
    public boolean enableOrDisableVehicle(Long id) {
        //查询当前状态
        QueryWrapper<VehicleInfo> condition = new QueryWrapper<>();
        condition.lambda().select(VehicleInfo::getStatus).eq(VehicleInfo::getId, id);
        VehicleInfo tmp = this.baseMapper.selectOne(condition);

        String status = StatusEnum.ENABLE.getCode().equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        VehicleInfo vehicleInfo = new VehicleInfo().setId(id).setStatus(status);
        return this.updateById(vehicleInfo);
    }

    @Override
    public List<VehicleInfoVO> findVehicleByDriverName(String driverName) {
        return baseMapper.findVehicleByDriverName(driverName);
    }

    @Override
    public List<VehicleSizeInfoVO> findVehicleSize() {
        return baseMapper.findVehicleSize();
    }

    /**
     * 根据车辆id查询车辆详情
     */
    @Override
    public VehicleDetailsVO getVehicleDetailsById(Long vehicleId) {
        VehicleDetailsVO vehicleDetail = this.baseMapper.getVehicleDetailsById(vehicleId);
        //司机信息
        Collection<DriverInfo> driverInfos = this.driverInfoService.listByIds(vehicleDetail.getDriverIds());
        if (!CollectionUtils.isEmpty(driverInfos)) {
            List<DriverInfoVO> list = new ArrayList<>();
            for (DriverInfo driverInfo : driverInfos) {
                list.add(ConvertUtil.convert(driverInfo, DriverInfoVO.class));
            }
            vehicleDetail.setDriverInfoVOS(list);
        }

        return vehicleDetail;
    }

    /**
     * 根据车辆id获取车辆和供应商信息
     */
    @Override
    public VehicleDetailsVO getVehicleAndSupplierInfo(Long vehicleId) {
        return this.baseMapper.getVehicleDetailsById(vehicleId);
    }

    @Override
    public List<VehicleInfo> getByCondition(VehicleInfo vehicleInfo) {
        QueryWrapper<VehicleInfo> condition = new QueryWrapper<>();
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据条件获取车辆信息
     */
}
