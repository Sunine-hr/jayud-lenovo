package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IWarehouseAreaService;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @ApiOperation(value = "不分页查询list")
    @PostMapping("/findWarehouseArea")
    public CommonResult<List<WarehouseAreaVO>> findWarehouseArea(@RequestBody QueryWarehouseAreaForm form) {
        form.setStartTime();
        List<WarehouseAreaVO> warehouseAreaVOS = warehouseAreaService.getList(form);
        return CommonResult.success(warehouseAreaVOS);
    }

    @ApiOperation(value = "区域分页查询list")
    @PostMapping("/findWarehouseAreaByPage")
    public CommonResult findWarehouseAreaByPage(@RequestBody QueryWarehouseAreaForm form){
        IPage<WarehouseAreaVO> page = this.warehouseAreaService.findWarehouseAreaByPage(form);
        return CommonResult.success(page);
    }

    @ApiOperation(value = "新增或修改仓库区域信息")
    @PostMapping("/saveOrUpdateWarehouseArea")
    public CommonResult saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseAreaForm){
        boolean result = warehouseAreaService.saveOrUpdateWarehouseArea(warehouseAreaForm);
        if(!result){
            return CommonResult.error(444,"数据插入失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "3.区域操作：启用、禁用")
    @PostMapping("/operationWarehouseArea")
    public CommonResult operationWarehouseArea(@RequestBody OperationForm form){
        warehouseAreaService.operationWarehouseArea(form);
        return CommonResult.success("操作成功！");
    }


    @ApiOperation(value = "新增或修改仓库区域信息")
    @PostMapping("/saveOrUpdateWarehouseAreaShelves")
    public CommonResult saveOrUpdateWarehouseAreaShelves(WarehouseAreaShelvesForm form){
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
    public CommonResult findWarehouseAreaShelvesByPage(@RequestBody QueryWarehouseAreaShelvesForm form){
        IPage<WarehouseAreaShelvesVO> page = this.warehouseAreaShelvesService.findWarehouseAreaShelvesByPage(form);
        return CommonResult.success(page);
    }

}

