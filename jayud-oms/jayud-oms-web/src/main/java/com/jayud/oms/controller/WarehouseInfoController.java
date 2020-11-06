package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddVehicleInfoForm;
import com.jayud.oms.model.bo.AddWarehouseInfoForm;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.bo.QueryWarehouseInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.po.WarehouseInfo;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.model.vo.WarehouseInfoVO;
import com.jayud.oms.service.IRegionCityService;
import com.jayud.oms.service.IWarehouseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 中转仓库信息表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/warehouseInfo")
@Api(tags = "中转仓库信息")
public class WarehouseInfoController {


    @Autowired
    private IWarehouseInfoService warehouseInfoService;
    @Autowired
    private IRegionCityService regionCityService;

    @ApiOperation(value = "分页查询中转仓库信息")
    @PostMapping(value = "/findWarehouseInfoByPage")
    public CommonResult<CommonPageResult<WarehouseInfoVO>> findWarehouseInfoByPage(@RequestBody QueryWarehouseInfoForm form) {
        IPage<WarehouseInfoVO> iPage = warehouseInfoService.findWarehouseInfoByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑中转仓库信息")
    @PostMapping(value = "/saveOrUpdateWarehouseInfo")
    public CommonResult saveOrUpdateWarehouseInfo(@Valid @RequestBody AddWarehouseInfoForm form) {
        WarehouseInfo info = new WarehouseInfo().setId(form.getId())
                .setWarehouseName(form.getWarehouseName()).setWarehouseCode(form.getWarehouseCode());
        if (this.warehouseInfoService.checkUnique(info)) {
            return CommonResult.error(400, "中转仓仓库已存在");
        }

        WarehouseInfo warehouseInfo = ConvertUtil.convert(form, WarehouseInfo.class);
        if (this.warehouseInfoService.saveOrUpdateWarehouseInfo(warehouseInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取中转仓库信息详情,id是中转仓库信息主键")
    @PostMapping(value = "/getWarehouseInfoById")
    public CommonResult getWarehouseInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        WarehouseInfo warehouseInfo = this.warehouseInfoService.getById(id);
        return CommonResult.success(ConvertUtil.convert(warehouseInfo, WarehouseInfoVO.class));
    }

    @ApiOperation(value = "更改启用/禁用中转仓库状态,id是中转仓库信息主键")
    @PostMapping(value = "/enableOrDisableWarehouse")
    public CommonResult enableOrDisableWarehouse(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.warehouseInfoService.enableOrDisableWarehouse(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }
}
