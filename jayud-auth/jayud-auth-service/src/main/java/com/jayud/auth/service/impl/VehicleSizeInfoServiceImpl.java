package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.VehicleSizeInfo;
import com.jayud.auth.mapper.VehicleSizeInfoMapper;
import com.jayud.auth.service.IVehicleSizeInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆尺寸信息 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class VehicleSizeInfoServiceImpl extends ServiceImpl<VehicleSizeInfoMapper, VehicleSizeInfo> implements IVehicleSizeInfoService {


    @Autowired
    private VehicleSizeInfoMapper vehicleSizeInfoMapper;

    @Override
    public IPage<VehicleSizeInfo> selectPage(VehicleSizeInfo vehicleSizeInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<VehicleSizeInfo> page=new Page<VehicleSizeInfo>(currentPage,pageSize);
        IPage<VehicleSizeInfo> pageList= vehicleSizeInfoMapper.pageList(page, vehicleSizeInfo);
        return pageList;
    }

    @Override
    public List<VehicleSizeInfo> selectList(VehicleSizeInfo vehicleSizeInfo){
        return vehicleSizeInfoMapper.list(vehicleSizeInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        vehicleSizeInfoMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        vehicleSizeInfoMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryVehicleSizeInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryVehicleSizeInfoForExcel(paramMap);
    }

    @Override
    public List<VehicleSizeInfo> findVehicleSize() {
        return this.vehicleSizeInfoMapper.findVehicleSize();
    }

}
