package com.jayud.tools.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.vo.SensitiveCommodityVO;
import com.jayud.tools.service.ISensitiveCommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sensitivecommodity")
@Api(tags = "佳裕达小工具-敏感品名管理")
public class SensitiveCommodityController {

    @Autowired
    ISensitiveCommodityService sensitiveCommodityService;

    @ApiOperation(value = "查询敏感品名list")
    @PostMapping(value = "/getSensitiveCommodityList")
    public CommonResult<List<SensitiveCommodity>> getSensitiveCommodityList(@RequestBody QuerySensitiveCommodityForm form){
        List<SensitiveCommodity> userList = sensitiveCommodityService.getSensitiveCommodityList(form);
        return CommonResult.success(userList);
    }

    @ApiOperation(value = "保存`敏感品名`（新增或修改）")
    @PostMapping(value = "/saveSensitiveCommodity")
    public CommonResult saveSensitiveCommodity(@Valid @RequestBody SensitiveCommodityForm sensitiveCommodityForm){
        sensitiveCommodityService.saveSensitiveCommodity(sensitiveCommodityForm);
        return CommonResult.success();
    }

    @ApiOperation(value = "删除`敏感品名`")
    @PostMapping(value = "/deleteSensitiveCommodityById")
    public CommonResult deleteSensitiveCommodityById(@RequestParam(value = "id") Long id){
        sensitiveCommodityService.deleteSensitiveCommodityById(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "敏感品名分页查询")
    @PostMapping(value = "/findSensitiveCommodityByPage")
    public CommonResult<CommonPageResult<SensitiveCommodityVO>> findSensitiveCommodityByPage(@RequestBody QuerySensitiveCommodityForm  form){
        IPage<SensitiveCommodityVO> pageList = sensitiveCommodityService.findSensitiveCommodityByPage(form);
        CommonPageResult<SensitiveCommodityVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
