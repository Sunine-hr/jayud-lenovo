package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.HarbourInfoForm;
import com.jayud.mall.model.vo.HarbourInfoVO;
import com.jayud.mall.service.IHarbourInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/harbourinfo")
@Api(tags = "S028-后台-机场港口信息接口")
@ApiSort(value = 28)
public class HarbourInfoController {

    @Autowired
    IHarbourInfoService harbourInfoService;

    @ApiOperation(value = "查询机场港口信息List")
    @PostMapping("/findHarbourInfo")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<HarbourInfoVO>> findHarbourInfo(@RequestBody HarbourInfoForm form) {
        List<HarbourInfoVO> list = harbourInfoService.findHarbourInfo(form);
        return CommonResult.success(list);
    }

}
