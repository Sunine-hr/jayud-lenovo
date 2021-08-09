package com.jayud.scm.service;

import com.jayud.scm.model.po.RegionCity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 省市区关联表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-06
 */
public interface IRegionCityService extends IService<RegionCity> {

    List<RegionCity> cascadeQueryRegionCity(Long id);
}
