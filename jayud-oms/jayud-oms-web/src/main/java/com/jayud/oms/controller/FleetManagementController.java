package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.oms.model.bo.AddFleetManagementForm;
import com.jayud.oms.model.bo.AddMaintenanceManagementForm;
import com.jayud.oms.model.bo.QueryFleetManagementForm;
import com.jayud.oms.model.bo.QueryMaintenanceManagementForm;
import com.jayud.oms.model.po.FleetManagement;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.jayud.oms.model.vo.FleetManagementVO;
import com.jayud.oms.model.vo.MaintenanceManagementVO;
import com.jayud.oms.service.IFleetManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 车队管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@RestController
@Api(tags = "车队管理")
@RequestMapping("/fleetManagement")
public class FleetManagementController {

    @Autowired
    private IFleetManagementService fleetManagementService;

    @ApiOperation(value = "新增/编辑信息")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddFleetManagementForm form) {
        this.fleetManagementService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "分页查询油卡信息")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<FleetManagementVO>> findByPage(@RequestBody QueryFleetManagementForm form) {
        form.setStatus(Arrays.asList(StatusEnum.ENABLE.getCode(), StatusEnum.DISABLE.getCode()));
        IPage<FleetManagementVO> iPage = this.fleetManagementService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }


    @ApiOperation(value = "删除维修信息")
    @PostMapping("/delete")
    public CommonResult delete(Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.fleetManagementService.updateById(new FleetManagement().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


    @ApiOperation(value = "获取车队详情信息")
    @PostMapping("/getById")
    public CommonResult<FleetManagement> getById(Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return CommonResult.success(this.fleetManagementService.getById(id));
    }

    @ApiOperation(value = "启用/禁用")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.fleetManagementService.enableOrDisable(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "自动生成编号")
    @PostMapping(value = "/autoGenerateNum")
    public CommonResult autoGenerateNum() {
        return CommonResult.success(this.fleetManagementService.autoGenerateNum());
    }
}

