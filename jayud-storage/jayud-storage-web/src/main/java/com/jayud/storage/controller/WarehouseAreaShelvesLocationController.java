package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelves2Form;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * 仓库区域货架库位表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@RestController
@Slf4j
@Api(tags = "仓库区域货架库位管理")
@RequestMapping("/warehouseAreaShelvesLocation")
public class WarehouseAreaShelvesLocationController {

    @Autowired
    private IWarehouseAreaShelvesLocationService warehouseAreaShelvesLocationService;

    @Autowired
    private IWarehouseAreaShelvesService warehouseAreaShelvesService;

    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "分页查询所有货架")
    @PostMapping("/findWarehouseAreaShelvesByPage")
    public CommonResult findWarehouseAreaShelvesByPage(@RequestBody QueryWarehouseAreaShelves2Form form){
        IPage<WarehouseAreaShelvesFormVO> page = this.warehouseAreaShelvesService.findWarehouseAreaShelvesLocationByPage(form);
        //获取每个货架的最新库位情况
        List<WarehouseAreaShelvesLocation> warehouseAreaShelvesLocationList = this.warehouseAreaShelvesLocationService.getUpdateTime();
        if(CollectionUtils.isNotEmpty(warehouseAreaShelvesLocationList)){
            for (WarehouseAreaShelvesLocation warehouseAreaShelvesLocation : warehouseAreaShelvesLocationList) {
                for (WarehouseAreaShelvesFormVO record : page.getRecords()) {
                    if(record.getId().equals(warehouseAreaShelvesLocation.getShelvesId())){
                        record.setUpdateTime(warehouseAreaShelvesLocation.getCreateTime().toString());
                    }
                }
            }
        }
        CommonPageResult<WarehouseAreaShelvesVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询货架下所有库位")
    @PostMapping("/findWarehouseAreaShelvesLocationByPage")
    public CommonResult findWarehouseAreaShelvesLocationByPage(@RequestBody QueryWarehouseAreaShelvesLocationForm form){
        IPage<WarehouseAreaShelvesLocationVO> page = this.warehouseAreaShelvesLocationService.findWarehouseAreaShelvesLocationByPage(form);
        //获取所有货架类型
        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();
        for (WarehouseAreaShelvesLocationVO record : page.getRecords()) {
            record.setMaximumVolume();
            for (InitComboxSVO datum : data) {
                if (datum.getId().equals(record.getShelvesType())){
                    record.setShelvesTypeName(datum.getValue());
                }
            }
        }
        CommonPageResult<WarehouseAreaShelvesVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "增加或修改库位信息")
    @PostMapping("/saveOrUpdateWarehouseAreaShelvesLocation")
    public CommonResult saveOrUpdateWarehouseAreaShelvesLocation(@RequestBody List<WarehouseAreaShelvesLocationForm> form){
        boolean result = this.warehouseAreaShelvesLocationService.saveOrUpdateWarehouseAreaShelvesLocation(form);
        if(!result){
            return CommonResult.error(444,"数据插入失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取库位详情")
    @PostMapping("/findWarehouseAreaShelvesLocationById")
    public CommonResult findWarehouseAreaShelvesLocationById(@RequestBody Map<String,Object> map){
        Long id = MapUtil.getLong(map,"id");
        WarehouseAreaShelvesLocation byId = this.warehouseAreaShelvesLocationService.getById(id);
        WarehouseAreaShelvesLocationVO convert = ConvertUtil.convert(byId, WarehouseAreaShelvesLocationVO.class);
        List<WarehouseAreaShelvesLocationVO> locationVOS = new ArrayList<>();
        locationVOS.add(convert);
        return CommonResult.success(locationVOS);
    }

    @ApiOperation(value = "查看库位编码")
    @PostMapping("/viewLocationCode")
    public CommonResult<List<LocationCodeVO>> viewLocationCode(@RequestBody QueryWarehouseAreaShelvesLocationForm form){
        List<WarehouseAreaShelvesLocationVO> warehouseAreaShelvesLocations = this.warehouseAreaShelvesLocationService.getListByShelvesId(form);

        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();

        List<LocationCodeVO> locationCodeVOS = new ArrayList<>();

        for (WarehouseAreaShelvesLocationVO warehouseAreaShelvesLocation : warehouseAreaShelvesLocations) {
            String shelvesTypeName = null;
            for (InitComboxSVO datum : data) {
                if (datum.getId().equals(warehouseAreaShelvesLocation.getShelvesType())){
                    shelvesTypeName = datum.getValue();
                }
            }
            LocationCodeVO locationCodeVO = ConvertUtil.convert(warehouseAreaShelvesLocation, LocationCodeVO.class);
            if(shelvesTypeName.equals("A面")){
                StringBuffer locationCode = new StringBuffer();
                locationCode.append(warehouseAreaShelvesLocation.getCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getAreaCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesName()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                        .append("A").append(warehouseAreaShelvesLocation.getShelvesColumn());
                locationCodeVO.setLocationCode(locationCode.toString());
                locationCodeVOS.add(locationCodeVO);
            }
            if(shelvesTypeName.equals("B面")){
                StringBuffer locationCode = new StringBuffer();
                locationCode.append(warehouseAreaShelvesLocation.getCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getAreaCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesName()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                        .append("B").append(warehouseAreaShelvesLocation.getShelvesColumn());
                locationCodeVO.setLocationCode(locationCode.toString());
                locationCodeVOS.add(locationCodeVO);
            }
            if(shelvesTypeName.equals("AB面")){
                StringBuffer locationCode = new StringBuffer();
                locationCode.append(warehouseAreaShelvesLocation.getCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getAreaCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesName()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                        .append("A").append(warehouseAreaShelvesLocation.getShelvesColumn());
                locationCodeVO.setLocationCode(locationCode.toString());
                LocationCodeVO locationCodeVO1 = locationCodeVO;

                StringBuffer locationCode1 = new StringBuffer();
                locationCode1.append(warehouseAreaShelvesLocation.getCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getAreaCode()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesName()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                        .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                        .append("B").append(warehouseAreaShelvesLocation.getShelvesColumn());
                locationCodeVO1.setLocationCode(locationCode1.toString());
                locationCodeVOS.add(locationCodeVO);
                locationCodeVOS.add(locationCodeVO1);
            }else {
                log.warn("货架类型不存在，无法生成货架编码");
            }
        }
        return CommonResult.success(locationCodeVOS);
    }



}

