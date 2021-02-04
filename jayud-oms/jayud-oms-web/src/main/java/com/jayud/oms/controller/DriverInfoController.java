package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddDriverInfoForm;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.vo.DriverInfoVO;
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
import java.util.List;
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
//        for (DriverInfoVO record : iPage.getRecords()) {
//            //拼接车牌
//            record.splicingPlateNumber();
//        }
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑司机信息")
    @PostMapping(value = "/saveOrUpdateDriverInfo")
    public CommonResult saveOrUpdateDriverInfo(@Valid @RequestBody AddDriverInfoForm form) {
        DriverInfo info = new DriverInfo().setId(form.getId()).setName(form.getName());
        if (this.driverInfoService.checkUnique(info)) {
            return CommonResult.error(400, "司机姓名已存在");
        }
        //校验手机是否存在
        DriverInfo tmp = this.driverInfoService.getByPhone(form.getPhone());
        if (tmp != null && !tmp.getId().equals(form.getId())) {
            return CommonResult.error(400, "已经存在司机号码");
        }

//        if (form.getId() != null) {
//            DriverInfo old = driverInfoService.getById(form.getId());
//            //密码相同不需要进行修改
//            if (form.getPassword().equals(old.getPassword())) {
//                form.setPassword(null);
//            }
//        }

        DriverInfo driverInfo = ConvertUtil.convert(form, DriverInfo.class);
        driverInfo.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
        if (this.driverInfoService.saveOrUpdateDriverInfo(driverInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "重置司机密码")
    @PostMapping(value = "/resetPassword")
    public CommonResult resetPassword(@Valid @RequestBody Map<String, String> map) {
        String id = map.get("id");
        if (StringUtils.isEmpty(id)) {
            return CommonResult.error(400, "id不能为空");
        }
        this.driverInfoService.saveOrUpdateDriverInfo(new DriverInfo()
                .setId(Long.parseLong(id)).setPassword("E10ADC3949BA59ABBE56E057F20F883E"));
        return CommonResult.success();
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

    @ApiOperation(value = "更改启用/禁用车辆状态,id是车辆信息主键")
    @PostMapping(value = "/enableOrDisableDriver")
    public CommonResult enableOrDisableDriver(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.driverInfoService.enableOrDisableDriver(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }


    @ApiOperation(value = "查询审核通过的司机")
    @PostMapping(value = "/getEnableDriverInfo")
    public CommonResult<List<DriverInfo>> getEnableDriverInfo() {
        return CommonResult.success(this.driverInfoService.getEnableDriverInfo());
    }
}

