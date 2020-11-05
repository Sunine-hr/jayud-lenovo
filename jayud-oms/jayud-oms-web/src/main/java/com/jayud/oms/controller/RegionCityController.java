package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.IRegionCityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 省市区关系表 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/regionCity")
@Api(tags = "省市区管理")
public class RegionCityController {

    @Autowired
    private IRegionCityService regionCityService;

    @ApiOperation(value = "查询省/市/区地址,id-上一级id,没有传查所有市地址")
    @PostMapping(value = "/cascadeQueryRegionCity")
    public CommonResult cascadeQueryRegionCity(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        List<RegionCity> regionCitys = regionCityService.cascadeQueryRegionCity(id);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (RegionCity regionCity : regionCitys) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(regionCity.getId());
            initComboxVO.setName(regionCity.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }
}

