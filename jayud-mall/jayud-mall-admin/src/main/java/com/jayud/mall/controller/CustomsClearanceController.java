package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.vo.CustomsClearanceVO;
import com.jayud.mall.service.ICustomsClearanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customsclearance")
@Api(tags = "S012-后台-清关资料接口")
@ApiSort(value = 12)
public class CustomsClearanceController {

    @Autowired
    ICustomsClearanceService customsClearanceService;

    @ApiOperation(value = "分页查询清关资料")
    @PostMapping("/findCustomsClearanceByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<CustomsClearanceVO>> findCustomsClearanceByPage(@RequestBody QueryCustomsClearanceForm form) {
        IPage<CustomsClearanceVO> pageList = customsClearanceService.findCustomsClearanceByPage(form);
        CommonPageResult<CustomsClearanceVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存清关资料")
    @PostMapping("/saveCustomsClearance")
    @ApiOperationSupport(order = 2)
    public CommonResult saveCustomsClearance(@RequestBody CustomsClearanceForm form){
        return customsClearanceService.saveCustomsData(form);
    }

}
