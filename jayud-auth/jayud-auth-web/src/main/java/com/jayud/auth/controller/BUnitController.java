package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.IBUnitService;
import com.jayud.auth.model.po.BUnit;

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
 * 计量单位代码表 控制类
 *
 * @author jayud
 * @since 2022-03-25
 */
@Slf4j
@Api(tags = "计量单位代码表")
@RestController
@RequestMapping("/bUnit")
public class BUnitController {



    @Autowired
    public IBUnitService bUnitService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-25
     * @param: bUnit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BUnit>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<BUnit>> selectPage(BUnit bUnit,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(bUnitService.selectPage(bUnit,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-25
    * @param: bUnit
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.BUnit>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<BUnit>> selectList(BUnit bUnit,
                                                HttpServletRequest req) {
        return BaseResult.ok(bUnitService.selectList(bUnit));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-25
    * @param: bUnit
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody BUnit bUnit ){
        bUnitService.save(bUnit);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-25
     * @param: bUnit
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody BUnit bUnit ){
        bUnitService.updateById(bUnit);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        bUnitService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        bUnitService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.BUnit>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<BUnit> queryById(@RequestParam(name="id",required=true) int id) {
        BUnit bUnit = bUnitService.getById(id);
        return BaseResult.ok(bUnit);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-25
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出计量单位代码表")
    @PostMapping(path = "/exportBUnit")
    public void exportBUnit(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "单位CODE",
                "中文名称",
                "英文名称",
                "备注",
                "创建人名称",
                "创建时间",
                "最后修改人名称",
                "最后修改时间",
                "删除标记"
            );
            List<LinkedHashMap<String, Object>> dataList = bUnitService.queryBUnitForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "计量单位代码表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
