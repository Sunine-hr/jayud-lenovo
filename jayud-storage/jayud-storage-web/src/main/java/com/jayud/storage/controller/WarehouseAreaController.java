package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.WarehouseArea;
import com.jayud.storage.model.po.WarehouseAreaShelves;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IWarehouseAreaService;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库区域表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@RestController
@Api(tags = "仓库区域管理")
@RequestMapping("/warehouseArea")
public class WarehouseAreaController {

    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @Autowired
    private IWarehouseAreaShelvesService warehouseAreaShelvesService;

    @Autowired
    private IWarehouseAreaShelvesLocationService warehouseAreaShelvesLocationService;

    @ApiOperation(value = "不分页查询list")
    @PostMapping("/findWarehouseArea")
    public CommonResult<List<WarehouseAreaVO>> findWarehouseArea(@RequestBody QueryWarehouseAreaForm form) {
        form.setStartTime();
        List<WarehouseAreaVO> warehouseAreaVOS = warehouseAreaService.getList(form);
        return CommonResult.success(warehouseAreaVOS);
    }

    @ApiOperation(value = "区域分页查询list")
    @PostMapping("/findWarehouseAreaByPage")
    public CommonResult<CommonPageResult<WarehouseAreaVO>> findWarehouseAreaByPage(@RequestBody QueryWarehouseAreaForm form){
        IPage<WarehouseAreaVO> page = this.warehouseAreaService.findWarehouseAreaByPage(form);
        CommonPageResult<WarehouseAreaVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增或修改仓库区域信息")
    @PostMapping("/saveOrUpdateWarehouseArea")
    public CommonResult saveOrUpdateWarehouseArea(@RequestBody WarehouseAreaForm warehouseAreaForm){
        List<AreaForm> areaForms = warehouseAreaForm.getAreaForms();
        for (AreaForm areaForm : areaForms) {
            if(areaForm.getAreaCode()==null || areaForm.getAreaName()==null){
                return CommonResult.error(444,"数据不完整");
            }
            if(areaForm.getId() == null){
                WarehouseArea warehouseArea = warehouseAreaService.getWarehouseAreaByAreaCode(areaForm.getAreaCode());
                if(warehouseArea!=null){
                    return CommonResult.error(444,areaForm.getAreaCode()+"该代码已存在");
                }
                WarehouseArea warehouseArea1 = warehouseAreaService.getWarehouseAreaByAreaName(areaForm.getAreaName());
                if(warehouseArea1!=null){
                    return CommonResult.error(444,areaForm.getAreaName()+"该名字已存在");
                }
            }

        }
        boolean result = warehouseAreaService.saveOrUpdateWarehouseArea(warehouseAreaForm);
        if(!result){
            return CommonResult.error(444,"数据插入失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取仓库区域")
    @PostMapping("/getWarehouseAreaById")
    public CommonResult getWarehouseAreaById(@RequestBody Map<String,Object> map){
        Long id = MapUtil.getLong(map, "id");
        WarehouseArea warehouseArea = this.warehouseAreaService.getById(id);
        WarehouseAreaVO convert = ConvertUtil.convert(warehouseArea, WarehouseAreaVO.class);
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "3.区域操作：启用、禁用")
    @PostMapping("/operationWarehouseArea")
    public CommonResult operationWarehouseArea(@RequestBody OperationForm form){
        warehouseAreaService.operationWarehouseArea(form);
        return CommonResult.success("操作成功！");
    }


    @ApiOperation(value = "新增或修改仓库区域货架信息")
    @PostMapping("/saveOrUpdateWarehouseAreaShelves")
    public CommonResult saveOrUpdateWarehouseAreaShelves(@RequestBody WarehouseAreaShelvesForm form){
        for (ShelvesForm shelvesForm : form.getShelvesName()) {
            WarehouseAreaShelves warehouseAreaShelves = warehouseAreaShelvesService.getWarehouseAreaShelvesByShelvesName(shelvesForm.getName());
            if(warehouseAreaShelves!=null){
                return CommonResult.error(444,shelvesForm.getName()+"该货架名已存在");
            }
        }
        boolean result = warehouseAreaShelvesService.saveOrUpdateWarehouseAreaShelves(form);
        if(!result){
            return CommonResult.error(444,"数据插入失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "3.货架操作：启用、禁用")
    @PostMapping("/operationWarehouseAreaShelves")
    public CommonResult operationWarehouseAreaShelves(@RequestBody OperationForm form){
        warehouseAreaShelvesService.operationWarehouseAreaShelves(form);
        return CommonResult.success("操作成功！");
    }

    @ApiOperation(value = "货架分页查询list")
    @PostMapping("/findWarehouseAreaShelvesByPage")
    public CommonResult<CommonPageResult<WarehouseAreaShelvesVO>> findWarehouseAreaShelvesByPage(@RequestBody QueryWarehouseAreaShelvesForm form){
        IPage<WarehouseAreaShelvesVO> page = this.warehouseAreaShelvesService.findWarehouseAreaShelvesByPage(form);
        CommonPageResult<WarehouseAreaShelvesVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }


    @ApiOperation(value = "获取该区域下所有商品信息")
    @GetMapping("/findGoodByWarehouseArea")
    public CommonResult findGoodByWarehouseArea(@RequestParam("areaName") String areaName){
        //根据区域名称获取区域信息
        WarehouseArea warehouseAreaByAreaName = warehouseAreaService.getWarehouseAreaByAreaName(areaName);
        //获取该区域下所有库位信息，库位下存储的所有商品信息
//        List<WarehouseAreaShelvesLocation> warehouseAreaShelvesLocations = warehouseAreaShelvesLocationService.getListByAreaName(areaName);
        return CommonResult.success(warehouseAreaByAreaName);

    }


}

