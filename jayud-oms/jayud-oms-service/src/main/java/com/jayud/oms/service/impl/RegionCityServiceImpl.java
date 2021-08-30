package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.mapper.RegionCityMapper;
import com.jayud.oms.service.IRegionCityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 省市区关系表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Service
public class RegionCityServiceImpl extends ServiceImpl<RegionCityMapper, RegionCity> implements IRegionCityService {

    /**
     * 级联查询
     *
     * @param id
     * @return
     */
    @Override
    public List<RegionCity> cascadeQueryRegionCity(Long id) {
        QueryWrapper<RegionCity> condition = new QueryWrapper<>();
        if (id == null) {
            id = 0L;
        }
        condition.lambda().eq(RegionCity::getParentId, id);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取地址名称
     */
    @Override
    public List<RegionCity> getAddrName(Long... id) {
        QueryWrapper<RegionCity> condition = new QueryWrapper<>();
        condition.lambda().in(RegionCity::getId, id);
        return this.baseMapper.selectList(condition);
    }
}
