package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ICostGenreTaxRateService;
import com.jayud.auth.model.po.CostGenreTaxRate;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 费用类型税率表 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "费用类型税率表")
@RestController
@RequestMapping("/costGenreTaxRate")
public class CostGenreTaxRateController {



    @Autowired
    public ICostGenreTaxRateService costGenreTaxRateService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: costGenreTaxRate
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CostGenreTaxRate>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CostGenreTaxRate>> selectPage(CostGenreTaxRate costGenreTaxRate,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(costGenreTaxRateService.selectPage(costGenreTaxRate,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: costGenreTaxRate
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.CostGenreTaxRate>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CostGenreTaxRate>> selectList(CostGenreTaxRate costGenreTaxRate,
                                                HttpServletRequest req) {
        return BaseResult.ok(costGenreTaxRateService.selectList(costGenreTaxRate));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: costGenreTaxRate
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CostGenreTaxRate costGenreTaxRate ){
        costGenreTaxRateService.save(costGenreTaxRate);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: costGenreTaxRate
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CostGenreTaxRate costGenreTaxRate ){
        costGenreTaxRateService.updateById(costGenreTaxRate);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        costGenreTaxRateService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        costGenreTaxRateService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.CostGenreTaxRate>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CostGenreTaxRate> queryById(@RequestParam(name="id",required=true) int id) {
        CostGenreTaxRate costGenreTaxRate = costGenreTaxRateService.getById(id);
        return BaseResult.ok(costGenreTaxRate);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-11
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出费用类型税率表")
    @PostMapping(path = "/exportCostGenreTaxRate")
    public void exportCostGenreTaxRate(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "费用类型id",
                "费用类别id",
                "税率"
            );
            List<LinkedHashMap<String, Object>> dataList = costGenreTaxRateService.queryCostGenreTaxRateForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "费用类型税率表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
