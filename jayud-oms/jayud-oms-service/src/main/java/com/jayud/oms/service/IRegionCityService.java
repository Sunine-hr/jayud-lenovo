package com.jayud.oms.service;

import com.jayud.oms.model.po.RegionCity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 省市区关系表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
public interface IRegionCityService extends IService<RegionCity> {

    /**
     * 级联查询
     *
     * @param id
     * @return
     */
    List<RegionCity> cascadeQueryRegionCity(Long id);

    /**
     * 获取地址名称
     */
    List<RegionCity> getAddrName(Long... id);

    /**
     * 通过省份名称获取省份ID
     * @param provinceName
     * @return
     */
    RegionCity getProvinceIdByName(String provinceName);

    /**
     * 通过城市或区域名称获取ID
     * @param parentId
     * @param cityName
     * @return
     */
    RegionCity getCityOrAreaIdByName(Long parentId, String cityName);
}
