package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanCounterIdForm;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.service.IOceanCounterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/oceancounter")
@Api(tags = "A039-admin-提单对应货柜信息接口")
@ApiSort(value = 39)
public class OceanCounterController {

    @Autowired
    IOceanCounterService oceanCounterService;

    //查询(提单)柜子信息-包括费用
    @ApiOperation(value = "查询(提单)柜子信息-包括费用")
    @ApiOperationSupport(order = 1)
    @PostMapping(value = "/findOceanCounterById")
    public CommonResult<OceanCounterVO> findOceanCounterById(@Valid @RequestBody OceanCounterIdForm form){
        Long id = form.getId();
        OceanCounterVO oceanCounterVO = oceanCounterService.findOceanCounterById(id);
        return CommonResult.success(oceanCounterVO);
    }


}
