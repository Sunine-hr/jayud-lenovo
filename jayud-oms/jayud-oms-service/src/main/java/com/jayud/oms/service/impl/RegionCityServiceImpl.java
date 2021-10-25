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

    /**
     * 通过省份名称获取省份ID
     * @param provinceName
     * @return
     */
    @Override
    public RegionCity getProvinceIdByName(String provinceName) {
        QueryWrapper<RegionCity> condition = new QueryWrapper<>();
        condition.lambda()
                .select(RegionCity::getId, RegionCity::getName)
                .eq(RegionCity::getParentId, 0)
                .in(RegionCity::getName, provinceName);
        return this.getOne(condition, false);
    }

    /**
     * 通过城市或区域名称获取ID
     * @param parentId
     * @param cityName
     * @return
     */
    @Override
    public RegionCity getCityOrAreaIdByName(Long parentId, String cityName) {
        QueryWrapper<RegionCity> condition = new QueryWrapper<>();
        condition.lambda()
                .select(RegionCity::getId, RegionCity::getName)
                .eq(RegionCity::getParentId, parentId)
                .in(RegionCity::getName, cityName);
        return this.getOne(condition, false);
    }
}
