package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AuditCustomsClearanceForm;
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

import javax.validation.Valid;
import java.util.List;

@Api(tags = "A012-admin-清关商品资料接口")
@ApiSort(value = 12)
@RestController
@RequestMapping("/customsclearance")
public class CustomsClearanceController {

    @Autowired
    ICustomsClearanceService customsClearanceService;

    @ApiOperation(value = "分页查询清关资料")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findCustomsClearanceByPage")
    public CommonResult<CommonPageResult<CustomsClearanceVO>> findCustomsClearanceByPage(
            @RequestBody QueryCustomsClearanceForm form) {
        IPage<CustomsClearanceVO> pageList = customsClearanceService.findCustomsClearanceByPage(form);
        CommonPageResult<CustomsClearanceVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存清关资料")
    @ApiOperationSupport(order = 2)
    @PostMapping("/saveCustomsClearance")
    public CommonResult saveCustomsClearance(@Valid @RequestBody CustomsClearanceForm form){
        return customsClearanceService.saveCustomsData(form);
    }

    @ApiOperation(value = "查询清关商品资料list")
    @ApiOperationSupport(order = 3)
    @PostMapping("/findCustomsClearance")
    public CommonResult<List<CustomsClearanceVO>> findCustomsClearance() {
        List<CustomsClearanceVO> list = customsClearanceService.findCustomsClearance();
        return CommonResult.success(list);
    }

    @ApiOperation(value = "审核-清关商品资料")
    @ApiOperationSupport(order = 4)
    @PostMapping("/auditCustomsClearance")
    public CommonResult auditCustomsClearance(@Valid @RequestBody AuditCustomsClearanceForm form){
        customsClearanceService.auditCustomsClearance(form);
        return CommonResult.success("操作成功");
    }


}
