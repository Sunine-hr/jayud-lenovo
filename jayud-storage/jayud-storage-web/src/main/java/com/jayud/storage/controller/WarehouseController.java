package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.OperationWarehouseForm;
import com.jayud.storage.model.bo.QueryWarehouseForm;
import com.jayud.storage.model.bo.SaveWarehouseForm;
import com.jayud.storage.model.bo.WarehouseIdForm;
import com.jayud.storage.model.vo.WarehouseVO;
import com.jayud.storage.service.IWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/warehouse")
@Api(tags = "订单管理/仓储订单/仓库管理/仓库接口")
public class WarehouseController {

    @Autowired
    IWarehouseService warehouseService;

    @ApiOperation(value = "查询list")
    @PostMapping("/findWarehouse")
    public CommonResult<List<WarehouseVO>> findWarehouse() {
        List<WarehouseVO> list = warehouseService.findWarehouse();
        return CommonResult.success(list);
    }


    @ApiOperation(value = "2.分页查询page")
    @PostMapping("/findWarehouseByPage")
    public CommonResult<CommonPageResult<WarehouseVO>> findWarehouseByPage(@RequestBody QueryWarehouseForm form) {
        form.setStartTime();
        IPage<WarehouseVO> pageList = warehouseService.findWarehouseByPage(form);
        CommonPageResult<WarehouseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "3.操作：启用、禁用")
    @PostMapping("/operationWarehouse")
    public CommonResult operationWarehouse(@RequestBody OperationWarehouseForm form){
        warehouseService.operationWarehouse(form);
        return CommonResult.success("操作成功！");
    }


    @ApiOperation(value = "4.保存：新增、编辑")
    @PostMapping("/saveWarehouse")
    public CommonResult saveWarehouse(@Valid @RequestBody SaveWarehouseForm form){
        warehouseService.saveWarehouse(form);
        return CommonResult.success("保存成功！");
    }

    @ApiOperation(value = "5.根据id，查询仓库")
    @PostMapping("/findWarehouseById")
    public CommonResult<WarehouseVO> findWarehouseById(@Valid @RequestBody WarehouseIdForm form){
        Long id = form.getId();
        WarehouseVO warehouseVO = warehouseService.findWarehouseById(id);
        return CommonResult.success(warehouseVO);
    }

}

