package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddDriverInfoForm;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.service.IDriverInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 供应商对应司机信息 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/driverInfo")
@Api(tags = "司机管理")
public class DriverInfoController {

    @Autowired
    private IDriverInfoService driverInfoService;

    @ApiOperation(value = "分页查询司机信息")
    @PostMapping(value = "/findDriverInfoByPage")
    public CommonResult<CommonPageResult<DriverInfoVO>> findVehicleInfoByPage(@RequestBody QueryDriverInfoForm form) {
        IPage<DriverInfoVO> iPage = driverInfoService.findDriverInfoByPage(form);
        for (DriverInfoVO record : iPage.getRecords()) {
            //拼接车牌
            record.splicingPlateNumber();
        }
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑司机信息")
    @PostMapping(value = "/saveOrUpdateDriverInfo")
    public CommonResult saveOrUpdateDriverInfo(@Valid @RequestBody AddDriverInfoForm form) {
        if (this.driverInfoService.checkUnique(new DriverInfo().setName(form.getName()))) {
            return CommonResult.error(400, "司机姓名已存在");
        }
        DriverInfo driverInfo = ConvertUtil.convert(form, DriverInfo.class);
        if (this.driverInfoService.saveOrUpdateDriverInfo(driverInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除司机信息,id是司机主键")
    @PostMapping(value = "/deleteDriverInfo")
    public CommonResult deleteDriverInfo(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        DriverInfo driverInfo = new DriverInfo().setId(id).setStatus(StatusEnum.INVALID.getCode());
        if (this.driverInfoService.saveOrUpdateDriverInfo(driverInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取司机信息详情,id是司机信息主键")
    @PostMapping(value = "/getDriverInfoById")
    public CommonResult getDriverInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        DriverInfo driverInfo = this.driverInfoService.getById(id);
        return CommonResult.success(ConvertUtil.convert(driverInfo, DriverInfoVO.class));
    }
}

