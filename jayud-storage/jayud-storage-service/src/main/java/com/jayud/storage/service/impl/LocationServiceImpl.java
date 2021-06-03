package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.storage.model.po.Location;
import com.jayud.storage.mapper.LocationMapper;
import com.jayud.storage.service.ILocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 库位对应的库位编码 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

    @Override
    public List<Location> getList(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("location_id",id);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isExistence(String kuCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("location_code",kuCode);
        Location location = this.baseMapper.selectOne(queryWrapper);
        if(location == null){
            return false;
        }
        return true;
    }
}
