package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CostItemForm;
import com.jayud.mall.model.bo.CostItemSupForm;
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

import javax.validation.Valid;
import java.util.Arrays;
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

    //查询应付费用list
    @ApiOperation(value = "查询应付费用list")
    @PostMapping("/findCostItemByPay")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<CostItemVO>> findCostItemByPay(@RequestBody CostItemForm form) {
        form.setIdentifying("2");//辨认费用类型(1应收费用 2应付费用)
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    //查询应收费用list
    @ApiOperation(value = "查询应收费用list")
    @PostMapping("/findCostItemByRec")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<CostItemVO>> findCostItemByRec(@RequestBody CostItemForm form) {
        form.setIdentifying("1");//辨认费用类型(1应收费用 2应付费用)
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    //查询其他应收费用(过滤掉 海运费、内陆费)list
    @ApiOperation(value = "查询其他应收费用list")
    @PostMapping("/findCostItemByOtherRec")
    @ApiOperationSupport(order = 4)
    public CommonResult<List<CostItemVO>> findCostItemByOtherRec(@RequestBody CostItemForm form) {
        form.setIdentifying("1");//辨认费用类型(1应收费用 2应付费用)
        /*
        JYD-REC-COS-00001,海运费
        JYD-REC-COS-00002,内陆费
         */
        form.setNotCostCodes(Arrays.asList("JYD-REC-COS-00001","JYD-REC-COS-00002"));
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "根据供应商id，获取供应商费用（应付费用信息）")
    @PostMapping("/findCostItemBySupId")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<CostItemVO>> findCostItemBySupId(@Valid @RequestBody CostItemSupForm form) {
        List<CostItemVO> list = costItemService.findCostItemBySupId(form);
        return CommonResult.success(list);
    }

}
