package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.RegionCity;
import com.jayud.auth.mapper.RegionCityMapper;
import com.jayud.auth.service.IRegionCityService;
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
 * 省市区关联表 服务实现类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Service
public class RegionCityServiceImpl extends ServiceImpl<RegionCityMapper, RegionCity> implements IRegionCityService {


    @Autowired
    private RegionCityMapper regionCityMapper;

    @Override
    public IPage<RegionCity> selectPage(RegionCity regionCity,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<RegionCity> page=new Page<RegionCity>(currentPage,pageSize);
        IPage<RegionCity> pageList= regionCityMapper.pageList(page, regionCity);
        return pageList;
    }

    @Override
    public List<RegionCity> selectList(RegionCity regionCity){
        return regionCityMapper.list(regionCity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        regionCityMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        regionCityMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryRegionCityForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryRegionCityForExcel(paramMap);
    }

}
