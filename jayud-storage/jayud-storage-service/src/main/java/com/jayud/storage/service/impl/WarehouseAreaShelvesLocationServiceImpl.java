package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.Location;
import com.jayud.storage.model.po.WarehouseAreaShelves;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.mapper.WarehouseAreaShelvesLocationMapper;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.ILocationService;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 仓库区域货架库位表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Service
public class WarehouseAreaShelvesLocationServiceImpl extends ServiceImpl<WarehouseAreaShelvesLocationMapper, WarehouseAreaShelvesLocation> implements IWarehouseAreaShelvesLocationService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IWarehouseAreaShelvesService warehouseAreaShelvesService;

    @Autowired
    private ILocationService locationService;

    @Override
    public List<WarehouseAreaShelvesLocation> getUpdateTime() {
        return this.baseMapper.getUpdateTime();
    }

    @Override
    public IPage<WarehouseAreaShelvesLocationVO> findWarehouseAreaShelvesLocationByPage(QueryWarehouseAreaShelvesLocationForm form) {
        Page<WarehouseAreaShelvesLocationVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaShelvesLocationVO> pageInfo = this.baseMapper.findWarehouseAreaShelvesLocationByPage(page,form);
        return pageInfo;
    }

    @Override
    public boolean saveOrUpdateWarehouseAreaShelvesLocation(List<WarehouseAreaShelvesLocationForm> form) {

        for (WarehouseAreaShelvesLocationForm warehouseAreaShelvesLocationForm : form) {
            WarehouseAreaShelvesLocation warehouseAreaShelvesLocation = ConvertUtil.convert(warehouseAreaShelvesLocationForm, WarehouseAreaShelvesLocation.class);
            warehouseAreaShelvesLocation.setStatus(1);
            warehouseAreaShelvesLocation.setCreateTime(LocalDateTime.now());
            warehouseAreaShelvesLocation.setCreateUser(UserOperator.getToken());
            boolean b = this.saveOrUpdate(warehouseAreaShelvesLocation);
            if(!b){
                return false;
            }

            if(warehouseAreaShelvesLocationForm.getId() == null){
                //库位添加成功,生成库位编码
                List<String> locationCode = this.createLocationCode(warehouseAreaShelvesLocation);
                List<Location> locations = new ArrayList<>();
                for (String s : locationCode) {
                    Location location = new Location();
                    location.setLocationId(warehouseAreaShelvesLocation.getId());
                    location.setCreateUser(UserOperator.getToken());
                    location.setLocationCode(s);
                    location.setCreateTime(LocalDateTime.now());
                    location.setStatus(0);
                    locations.add(location);
                }
                boolean b1 = this.locationService.saveOrUpdateBatch(locations);
                if(!b){
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public List<WarehouseAreaShelvesLocationVO> getListByShelvesId(QueryWarehouseAreaShelvesLocationForm form) {
        return this.baseMapper.getListByShelvesId(form);
    }

    @Override
    public List<LocationCodeVO> getList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<WarehouseAreaShelvesLocation> list = this.baseMapper.selectList(queryWrapper);
        List<LocationCodeVO> locationCodeVOS = new ArrayList<>();

        for (WarehouseAreaShelvesLocation warehouseAreaShelvesLocation : list) {
            List<Location> locations = locationService.getList(warehouseAreaShelvesLocation.getId());
            for (Location location : locations) {
                LocationCodeVO locationCodeVO = ConvertUtil.convert(warehouseAreaShelvesLocation, LocationCodeVO.class);
                locationCodeVO.setLocationCode(location.getLocationCode());
                locationCodeVOS.add(locationCodeVO);
            }
        }
        return locationCodeVOS;
    }

    private List<String> createLocationCode(WarehouseAreaShelvesLocation warehouseAreaShelvesLocation){

        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();
        String shelvesTypeName = null;
        for (InitComboxSVO datum : data) {
            if (datum.getId().equals(warehouseAreaShelvesLocation.getShelvesType())){
                shelvesTypeName = datum.getValue();
            }
        }

        //根据货架id获取货架信息
        WarehouseAreaShelvesFormVO warehouseAreaShelvesVO = warehouseAreaShelvesService.getWarehouseAreaShelvesByShelvesId(warehouseAreaShelvesLocation.getShelvesId());

        List<String> string = new ArrayList<>();
        if(shelvesTypeName.equals("A面")){
            StringBuffer locationCode = new StringBuffer();
            locationCode.append(warehouseAreaShelvesVO.getCode()).append("-")
                    .append(warehouseAreaShelvesVO.getAreaCode()).append("-")
                    .append(warehouseAreaShelvesVO.getShelvesName()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                    .append("A").append(warehouseAreaShelvesLocation.getShelvesColumn());
            string.add(locationCode.toString());
        }
        if(shelvesTypeName.equals("B面")){
            StringBuffer locationCode = new StringBuffer();
            locationCode.append(warehouseAreaShelvesVO.getCode()).append("-")
                    .append(warehouseAreaShelvesVO.getAreaCode()).append("-")
                    .append(warehouseAreaShelvesVO.getShelvesName()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                    .append("B").append(warehouseAreaShelvesLocation.getShelvesColumn());
            string.add(locationCode.toString());
        }
        if(shelvesTypeName.equals("AB面")){
            StringBuffer locationCode = new StringBuffer();
            locationCode.append(warehouseAreaShelvesVO.getCode()).append("-")
                    .append(warehouseAreaShelvesVO.getAreaCode()).append("-")
                    .append(warehouseAreaShelvesVO.getShelvesName()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                    .append("A").append(warehouseAreaShelvesLocation.getShelvesColumn());
            string.add(locationCode.toString());

            StringBuffer locationCode1 = new StringBuffer();
            locationCode1.append(warehouseAreaShelvesVO.getCode()).append("-")
                    .append(warehouseAreaShelvesVO.getAreaCode()).append("-")
                    .append(warehouseAreaShelvesVO.getShelvesName()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesLine()).append("-")
                    .append(warehouseAreaShelvesLocation.getShelvesColumn()).append("-")
                    .append("B").append(warehouseAreaShelvesLocation.getShelvesColumn());
            string.add(locationCode.toString());

        }else {
            log.warn("货架类型不存在，无法生成货架编码");
        }
        return string;
    }
}
