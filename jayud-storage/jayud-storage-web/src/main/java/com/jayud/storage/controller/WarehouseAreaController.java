package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.GoodForm;
import com.jayud.storage.model.bo.QueryGoodForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaForm;
import com.jayud.storage.model.bo.WarehouseAreaForm;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IWarehouseAreaService;
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
@RequestMapping("/warehouseArea")
public class WarehouseAreaController {

    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @ApiOperation(value = "不分页查询list")
    @PostMapping("/findWarehouseArea")
    public CommonResult<List<WarehouseAreaVO>> findWarehouseArea(@RequestBody QueryWarehouseAreaForm form) {
        List<WarehouseAreaVO> warehouseAreaVOS = warehouseAreaService.getList(form);
        return CommonResult.success(warehouseAreaVOS);
    }

    @ApiOperation(value = "分页查询list")
    @PostMapping("/findWarehouseAreaByPage")
    public CommonResult findWarehouseAreaByPage(@RequestBody QueryWarehouseAreaForm form){
        IPage<WarehouseAreaVO> page = this.warehouseAreaService.findWarehouseAreaByPage(form);
        return CommonResult.success(page);
    }

    @ApiOperation(value = "新增或修改商品信息")
    @PostMapping("/saveOrUpdateWarehouseArea")
    public CommonResult saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseAreaForm){
        boolean result = warehouseAreaService.saveOrUpdateWarehouseArea(warehouseAreaForm);
        if(!result){
            return CommonResult.error(444,"数据插入失败");
        }
        return CommonResult.success();
    }

}

