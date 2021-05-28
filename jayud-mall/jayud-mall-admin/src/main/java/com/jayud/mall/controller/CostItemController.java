package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CostItemForm;
import com.jayud.mall.model.bo.CostItemStatusForm;
import com.jayud.mall.model.bo.CostItemSupForm;
import com.jayud.mall.model.bo.QueryCostItemForm;
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
@Api(tags = "A016-admin-费用项目接口")
@ApiSort(value = 16)
public class CostItemController {

    @Autowired
    ICostItemService costItemService;

    @ApiOperation(value = "查询费用项目list")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findCostItem")
    public CommonResult<List<CostItemVO>> findCostItem(@RequestBody CostItemForm form) {
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    //查询应付费用list
    @ApiOperation(value = "查询应付费用list")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findCostItemByPay")
    public CommonResult<List<CostItemVO>> findCostItemByPay(@RequestBody CostItemForm form) {
        form.setIdentifying("2");//辨认费用类型(1应收费用 2应付费用)
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    //查询应收费用list
    @ApiOperation(value = "查询应收费用list")
    @ApiOperationSupport(order = 3)
    @PostMapping("/findCostItemByRec")
    public CommonResult<List<CostItemVO>> findCostItemByRec(@RequestBody CostItemForm form) {
        form.setIdentifying("1");//辨认费用类型(1应收费用 2应付费用)
        List<CostItemVO> list = costItemService.findCostItem(form);
        return CommonResult.success(list);
    }

    //查询其他应收费用(过滤掉 海运费、内陆费)list
    @ApiOperation(value = "查询其他应收费用list")
    @ApiOperationSupport(order = 4)
    @PostMapping("/findCostItemByOtherRec")
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
    @ApiOperationSupport(order = 5)
    @PostMapping("/findCostItemBySupId")
    public CommonResult<List<CostItemVO>> findCostItemBySupId(@Valid @RequestBody CostItemSupForm form) {
        List<CostItemVO> list = costItemService.findCostItemBySupId(form);
        return CommonResult.success(list);
    }


    //分页
    @ApiOperation(value = "分页-费用项目分页查询")
    @ApiOperationSupport(order = 6)
    @PostMapping("/findCostItemByPage")
    public CommonResult<CommonPageResult<CostItemVO>> findCostItemByPage(
            @RequestBody QueryCostItemForm form) {
        IPage<CostItemVO> pageList = costItemService.findCostItemByPage(form);
        CommonPageResult<CostItemVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //保存
    @ApiOperation(value = "保存-费用项目")
    @ApiOperationSupport(order = 7)
    @PostMapping("/saveCostItem")
    public CommonResult<CostItemVO> saveCostItem(@Valid @RequestBody CostItemForm form){
        CostItemVO costItemVO = costItemService.saveCostItem(form);
        return CommonResult.success(costItemVO);
    }

    //停用启用
    @ApiOperation(value = "停用启用-费用项目")
    @ApiOperationSupport(order = 8)
    @PostMapping("/stopOrEnabled")
    public CommonResult stopOrEnabled(@Valid @RequestBody CostItemStatusForm form){
        costItemService.stopOrEnabled(form);
        return CommonResult.success("操作成功");
    }

}
