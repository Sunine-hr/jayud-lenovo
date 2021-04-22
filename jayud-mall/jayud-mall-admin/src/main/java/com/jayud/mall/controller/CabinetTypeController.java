package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CabinetTypeForm;
import com.jayud.mall.model.vo.CabinetTypeVO;
import com.jayud.mall.service.ICabinetTypeService;
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
@RequestMapping("/cabinettype")
@Api(tags = "A041-admin-柜型基本信息接口")
@ApiSort(value = 41)
public class CabinetTypeController {

    @Autowired
    ICabinetTypeService cabinetTypeService;

    @ApiOperation(value = "柜型基本信息list")
    @PostMapping("/findCabinetType")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<CabinetTypeVO>> findCabinetType(@RequestBody CabinetTypeForm form) {
        List<CabinetTypeVO> list = cabinetTypeService.findCabinetType(form);
        return CommonResult.success(list);
    }

}
