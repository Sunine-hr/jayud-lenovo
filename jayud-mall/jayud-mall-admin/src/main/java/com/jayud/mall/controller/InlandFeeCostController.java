package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.InlandFeeCostForm;
import com.jayud.mall.model.bo.InlandFeeCostIdForm;
import com.jayud.mall.model.bo.QueryInlandFeeCostForm;
import com.jayud.mall.model.vo.InlandFeeCostVO;
import com.jayud.mall.service.IInlandFeeCostService;
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

@RestController
@RequestMapping("/inlandfeecost")
@Api(tags = "A058-admin-内陆费费用接口")
@ApiSort(value = 58)
public class InlandFeeCostController {
    @Autowired
    IInlandFeeCostService inlandFeeCostService;

    @ApiOperation(value = "1.分页查询内陆费费用page")
    @PostMapping("/findInlandFeeCostByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<InlandFeeCostVO>> findInlandFeeCostByPage(@RequestBody QueryInlandFeeCostForm form) {
        IPage<InlandFeeCostVO> pageList = inlandFeeCostService.findInlandFeeCostByPage(form);
        CommonPageResult<InlandFeeCostVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "2.保存内陆费费用")
    @PostMapping("/saveInlandFeeCost")
    @ApiOperationSupport(order = 2)
    public CommonResult saveInlandFeeCost(@Valid @RequestBody InlandFeeCostForm form){
        return inlandFeeCostService.saveInlandFeeCost(form);
    }

    @ApiOperation(value = "3.根据id，查询内陆费费用")
    @PostMapping("/findInlandFeeCostById")
    @ApiOperationSupport(order = 3)
    public CommonResult<InlandFeeCostVO> findInlandFeeCostById(@Valid @RequestBody InlandFeeCostIdForm form){
        Long id = form.getId();
        InlandFeeCostVO inlandFeeCostVO = inlandFeeCostService.findInlandFeeCostById(id);
        return CommonResult.success(inlandFeeCostVO);
    }

}
