package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.RegionCity;
import com.jayud.scm.mapper.RegionCityMapper;
import com.jayud.scm.service.IRegionCityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 省市区关联表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-06
 */
@Service
public class RegionCityServiceImpl extends ServiceImpl<RegionCityMapper, RegionCity> implements IRegionCityService {

    @Override
    public List<RegionCity> cascadeQueryRegionCity(Long id) {
        QueryWrapper<RegionCity> condition = new QueryWrapper<>();
        if (id == null) {
            id = 0L;
        }
        condition.lambda().eq(RegionCity::getParentid, id);
        return this.baseMapper.selectList(condition);
    }
}
