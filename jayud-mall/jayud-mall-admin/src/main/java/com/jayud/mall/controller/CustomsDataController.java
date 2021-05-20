package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AuditCustomsDataForm;
import com.jayud.mall.model.bo.CustomsDataForm;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.vo.CustomsDataVO;
import com.jayud.mall.service.ICustomsDataService;
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

@RestController
@RequestMapping("/customsdata")
@Api(tags = "A011-admin-报关商品资料接口")
@ApiSort(value = 11)
public class CustomsDataController {

    @Autowired
    ICustomsDataService customsDataService;

    @ApiOperation(value = "分页查询报关资料")
    @PostMapping("/findCustomsDataByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<CustomsDataVO>> findCustomsDataByPage(@RequestBody QueryCustomsDataForm form) {
        IPage<CustomsDataVO> pageList = customsDataService.findCustomsDataByPage(form);
        CommonPageResult<CustomsDataVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存报关资料")
    @PostMapping("/saveCustomsData")
    @ApiOperationSupport(order = 2)
    public CommonResult saveCustomsData(@Valid @RequestBody CustomsDataForm form){
        return customsDataService.saveCustomsData(form);
    }

    @ApiOperation(value = "查询报关商品资料list")
    @PostMapping("/findCustomsData")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<CustomsDataVO>> findCustomsData() {
        List<CustomsDataVO> list = customsDataService.findCustomsData();
        return CommonResult.success(list);
    }

    @ApiOperation(value = "审核-报关商品资料")
    @PostMapping("/auditCustomsData")
    @ApiOperationSupport(order = 4)
    public CommonResult auditCustomsData(@Valid @RequestBody AuditCustomsDataForm form){
        customsDataService.auditCustomsData(form);
        return CommonResult.success("操作成功");
    }


}
