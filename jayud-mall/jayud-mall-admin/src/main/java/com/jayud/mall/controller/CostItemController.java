package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CostItemForm;
import com.jayud.mall.model.vo.CostItemVO;
import com.jayud.mall.service.ICostItemService;
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
@RequestMapping("/costitem")
@Api(tags = "S016-后台-费用项目接口")
@ApiSort(value = 16)
public class CostItemController {

    @Autowired
    ICostItemService costItemService;

    @ApiOperation(value = "查询费用项目list")
    @PostMapping("/findCostItem")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<CostItemVO>> findCostItem(@RequestBody CostItemForm form) {
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

}
