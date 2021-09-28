package com.jayud.scm.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.StringUtils;
import com.jayud.scm.model.bo.AddHgTruckForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgTruck;
import com.jayud.scm.model.vo.HgTruckVO;
import com.jayud.scm.service.IHgTruckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api")
@Api(tags = "港车对外接口")
@RestController
@Slf4j
public class HgTruckApi {

    @Autowired
    private IHgTruckService hgTruckService;

    @ApiOperation(value = "订车提交")
    @PostMapping(value = "/carBookingSubmission")
    public CommonResult carBookingSubmission(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);

        return CommonResult.success();
    }

    @ApiOperation(value = "明细提交")
    @PostMapping(value = "/detailSubmission")
    public CommonResult detailSubmission(@RequestBody Map<String,Object> map) {

        return CommonResult.success();
    }

    @ApiOperation(value = "获取车次状态")
    @PostMapping(value = "/getTrainNumberStatus")
    public CommonResult getTrainNumberStatus(@RequestBody QueryCommonForm form) {
        if(StringUtils.isEmpty(form.getTrainStatus())){
            return CommonResult.error(444,"车次状态为空");
        }
        if(StringUtils.isEmpty(form.getTruckNo())){
            return CommonResult.error(444,"港车编号为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.updateTrainNumberStatus(form);
        if(result){
            return CommonResult.error(444,"车次状态修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取载货清单")
    @PostMapping(value = "/getManifest")
    public CommonResult getManifest(@RequestBody Map<String,Object> map) {

        String exHkNo = MapUtil.getStr(map, "exHkNo");
        String truckNo = MapUtil.getStr(map, "truckNo");

        if(StringUtils.isEmpty(truckNo)){
            return CommonResult.error(444,"港车编号为空");
        }
        if(StringUtils.isEmpty(exHkNo)){
            return CommonResult.error(444,"载货清单为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,truckNo);
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }

        boolean result = hgTruckService.getManifest(exHkNo,truckNo);
        if(result){
            return CommonResult.error(444,"载货清单修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "接受运输公司等信息")
    @PostMapping(value = "/acceptTransportationInformation")
    public CommonResult acceptTransportationInformation(@RequestBody AddHgTruckForm form) {
        if(StringUtils.isEmpty(form.getTruckCompany())){
            return CommonResult.error(444,"运输公司不能为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.saveOrUpdateHgTruck(form);
        if(result){
            return CommonResult.error(444,"接收成功，运输信息修改失败");
        }
        return CommonResult.success();
    }
}
