package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.vo.MaintenanceManagementVO;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.service.IMaintenanceManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 维修管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@RestController
@RequestMapping("/maintenanceManagement")
public class MaintenanceManagementController {

    @Autowired
    private IMaintenanceManagementService maintenanceManagementService;

    @ApiOperation(value = "新增/编辑信息")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddMaintenanceManagementForm form) {
        this.maintenanceManagementService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "分页查询油卡信息")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<MaintenanceManagementVO>> findByPage(@RequestBody QueryMaintenanceManagementForm form) {
        form.setStatus(StatusEnum.ENABLE.getCode());
        IPage<MaintenanceManagementVO> iPage = this.maintenanceManagementService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }


    @ApiOperation(value = "删除维修信息")
    @PostMapping("/delete")
    public CommonResult delete(Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.maintenanceManagementService.updateById(new MaintenanceManagement().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }
}

