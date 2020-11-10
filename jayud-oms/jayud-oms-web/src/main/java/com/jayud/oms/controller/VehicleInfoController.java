package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddVehicleInfoForm;
import com.jayud.oms.model.bo.QueryVehicleInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.model.vo.VehicleInfoVO;
import com.jayud.oms.service.ISupplierInfoService;
import com.jayud.oms.service.IVehicleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 供应商对应车辆信息 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/vehicleInfo")
@Api(tags = "车辆管理")
public class VehicleInfoController {

    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private ISupplierInfoService supplierInfoService;

    @ApiOperation(value = "分页查询车辆信息")
    @PostMapping(value = "/findVehicleInfoByPage")
    public CommonResult<CommonPageResult<VehicleInfoVO>> findVehicleInfoByPage(@RequestBody QueryVehicleInfoForm form) {
        IPage<VehicleInfoVO> iPage = vehicleInfoService.findVehicleInfoByPage(form);

        //供应商待审核状态，无法进行编辑
        List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                BeanUtils.convertToFieldName(true,
                        SupplierInfo::getId, SupplierInfo::getSupplierChName));
        if (CollectionUtils.isEmpty(supplierInfos)) {
            iPage.getRecords().forEach(e -> e.setIsExist(false));
        } else {
            Map<Long, String> map = supplierInfos.stream()
                    .collect(Collectors.toMap(SupplierInfo::getId, SupplierInfo::getSupplierChName));
            iPage.getRecords().forEach(e -> {
                if (map.get(e.getSupplierId()) == null) {
                    e.setIsExist(false);
                } else {
                    e.setIsExist(true);
                }
            });
        }
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑车辆信息")
    @PostMapping(value = "/saveOrUpdateVehicleInfo")
    public CommonResult saveOrUpdateVehicleInfo(@Valid @RequestBody AddVehicleInfoForm form) {
        VehicleInfo info = new VehicleInfo().setPlateNumber(form.getPlateNumber()).setId(form.getId());
        if (this.vehicleInfoService.checkUnique(info)) {
            return CommonResult.error(400, "大陆车牌已存在");
        }

        VehicleInfo vehicleInfo = ConvertUtil.convert(form, VehicleInfo.class);
        //拼接附件地址
        vehicleInfo.setFiles(com.jayud.common.utils.StringUtils.getFileStr(form.getFileViews()))
                .setFileName(com.jayud.common.utils.StringUtils.getFileNameStr(form.getFileViews()));

        if (this.vehicleInfoService.saveOrUpdateVehicleInfo(vehicleInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除车辆信息,id是车辆信息主键")
    @PostMapping(value = "/deleteVehicleInfo")
    public CommonResult deleteVehicleInfo(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        VehicleInfo vehicleInfo = new VehicleInfo().setId(id).setStatus(StatusEnum.INVALID.getCode());
        if (this.vehicleInfoService.saveOrUpdateVehicleInfo(vehicleInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取车辆信息详情,id是车辆信息主键")
    @PostMapping(value = "/getVehicleInfoById")
    public CommonResult getVehicleInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        VehicleInfo vehicleInfo = this.vehicleInfoService.getById(id);
        return CommonResult.success(ConvertUtil.convert(vehicleInfo, VehicleInfoVO.class));
    }


    @ApiOperation(value = "车辆管理-下拉框-大陆车牌号")
    @PostMapping(value = "/initInlandPlateNumber")
    public CommonResult<List<InitComboxVO>> initInlandPlateNumber() {
        List<VehicleInfo> vehicleInfos = vehicleInfoService.getVehicleInfoByStatus(StatusEnum.ENABLE.getCode());
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (VehicleInfo vehicleInfo : vehicleInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(vehicleInfo.getId());
            initComboxVO.setName(vehicleInfo.getPlateNumber());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "更改启用/禁用车辆状态,id是车辆信息主键")
    @PostMapping(value = "/enableOrDisableVehicle")
    public CommonResult enableOrDisableVehicle(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.vehicleInfoService.enableOrDisableVehicle(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

}

