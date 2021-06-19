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
import com.jayud.storage.model.po.Location;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.ILocationService;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    private ILocationService locationService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private OmsClient omsClient;

    @Value("${address.shelvesUrl}")
    String shelvesUrl;

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
                        record.setUpdateTime(warehouseAreaShelvesLocation.getCreateTime());
                        record.setQrUrl(shelvesUrl+record.getShelvesName());
                    }
                }
            }
        }
        CommonPageResult<WarehouseAreaShelvesFormVO> pageVO = new CommonPageResult(page);
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
            List<Location> list = locationService.getList(record.getId());
            List<String> strings = new ArrayList<>();
            for (Location location : list) {
                strings.add(location.getLocationCode());
            }
            record.setKuCodeList(strings);
        }
        CommonPageResult<WarehouseAreaShelvesVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "增加或修改库位信息")
    @PostMapping("/saveOrUpdateWarehouseAreaShelvesLocation")
    public CommonResult saveOrUpdateWarehouseAreaShelvesLocation(@RequestBody List<WarehouseAreaShelvesLocationForm> form){

        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();
        int j = 0;
        for (WarehouseAreaShelvesLocationForm warehouseAreaShelvesLocationForm : form) {

            if(warehouseAreaShelvesLocationForm.getId() == null){
                for (int i = j+1; i < form.size(); i++) {
                    if(warehouseAreaShelvesLocationForm.getShelvesLine().equals(form.get(i).getShelvesLine()) &&
                            warehouseAreaShelvesLocationForm.getShelvesType().equals("AB面")){
                        return CommonResult.error(444,warehouseAreaShelvesLocationForm.getShelvesLine()+"层,填的数据重复");
                    }

                    if(warehouseAreaShelvesLocationForm.getShelvesLine().equals(form.get(i).getShelvesLine()) &&
                            warehouseAreaShelvesLocationForm.getShelvesType().equals(form.get(i).getShelvesType())){
                        return CommonResult.error(444,warehouseAreaShelvesLocationForm.getShelvesLine()+"层，填的数据重复");
                    }
                }

                WarehouseAreaShelvesLocation warehouseAreaShelvesLocation = warehouseAreaShelvesLocationService.getLocationByShelvesLine(warehouseAreaShelvesLocationForm.getShelvesLine(),warehouseAreaShelvesLocationForm.getShelvesId());
                if(warehouseAreaShelvesLocation != null){
                    if(warehouseAreaShelvesLocation.getShelvesType().equals("AB面")){
                        return CommonResult.error(444,warehouseAreaShelvesLocation.getShelvesLine()+"层没有多余面");
                    }
                    if(warehouseAreaShelvesLocation.getShelvesType().equals(warehouseAreaShelvesLocationForm.getShelvesType())){
                        String shelvesTypeName = null;
                        for (InitComboxSVO datum : data) {
                            if(datum.getId().equals(warehouseAreaShelvesLocationForm.getShelvesType())){
                                shelvesTypeName = datum.getValue();
                            }
                        }
                        return CommonResult.error(444,warehouseAreaShelvesLocation.getShelvesLine()+"层"+shelvesTypeName+"已存在");
                    }
                }

                WarehouseAreaShelvesLocation warehouseAreaShelvesLocation1 = warehouseAreaShelvesLocationService.getLocation(warehouseAreaShelvesLocationForm.getShelvesLine(),warehouseAreaShelvesLocationForm.getShelvesId(),warehouseAreaShelvesLocationForm.getShelvesType());
                if(warehouseAreaShelvesLocation1 != null){
                    return CommonResult.error(444,warehouseAreaShelvesLocation1.getShelvesLine()+"层已存在");
                }


            }else{
                WarehouseAreaShelvesLocation byId = warehouseAreaShelvesLocationService.getById(warehouseAreaShelvesLocationForm.getId());
                if(warehouseAreaShelvesLocationForm.getShelvesColumn()<byId.getShelvesColumn()){
                    return CommonResult.error(444,"列数修改只能比原来大");
                }
            }

            j++;
        }

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

    @Value("${address.locationUrl}")
    String locationUrl;

    @ApiOperation(value = "查看库位编码")
    @PostMapping("/viewLocationCode")
    public CommonResult<List<LocationCodeVO>> viewLocationCode(@RequestBody QueryWarehouseAreaShelvesLocationForm form){
        List<WarehouseAreaShelvesLocationVO> warehouseAreaShelvesLocations = this.warehouseAreaShelvesLocationService.getListByShelvesId(form);

        List<LocationCodeVO> locationCodeVOS = new ArrayList<>();

        for (WarehouseAreaShelvesLocationVO warehouseAreaShelvesLocation : warehouseAreaShelvesLocations) {
            List<Location> locations = locationService.getList(warehouseAreaShelvesLocation.getId());
            for (Location location : locations) {
                LocationCodeVO locationCodeVO = ConvertUtil.convert(warehouseAreaShelvesLocation, LocationCodeVO.class);
                locationCodeVO.setLocationCode(location.getLocationCode());
                locationCodeVO.setQrUrl(locationUrl+location.getLocationCode());
                locationCodeVOS.add(locationCodeVO);
            }
        }
        return CommonResult.success(locationCodeVOS);
    }

    @ApiOperation(value = "获取该库位下所有商品")
    @GetMapping("/findGoodByKuCode")
    public List<String> findGoodByKuCode(@RequestParam("kuCode")String kuCode){
        //获取该库位下所有商品
        List<QRCodeLocationGoodVO> locationGoodVOS = inGoodsOperationRecordService.getListByKuCode(kuCode);
        List<String> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(locationGoodVOS)){
            for (QRCodeLocationGoodVO locationGoodVO : locationGoodVOS) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(locationGoodVO.getName()).append(" ").append(locationGoodVO.getSku()).append(" ").append(locationGoodVO.getSpecificationModel()).append(" ").append(locationGoodVO.getNumber());
                list.add(buffer.toString());
            }

        }
        return list;
    }

}

