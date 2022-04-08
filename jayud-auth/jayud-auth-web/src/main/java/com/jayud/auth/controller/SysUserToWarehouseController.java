package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysUserToWarehouseService;
import com.jayud.auth.model.po.SysUserToWarehouse;

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
 * 用户与仓库关联表 控制类
 *
 * @author jayud
 * @since 2022-04-08
 */
@Slf4j
@Api(tags = "用户与仓库关联表")
@RestController
@RequestMapping("/sysUserToWarehouse")
public class SysUserToWarehouseController {



    @Autowired
    public ISysUserToWarehouseService sysUserToWarehouseService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-08
     * @param: sysUserToWarehouse
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUserToWarehouse>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<SysUserToWarehouse>> selectPage(SysUserToWarehouse sysUserToWarehouse,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(sysUserToWarehouseService.selectPage(sysUserToWarehouse,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-08
    * @param: sysUserToWarehouse
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysUserToWarehouse>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysUserToWarehouse>> selectList(SysUserToWarehouse sysUserToWarehouse,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysUserToWarehouseService.selectList(sysUserToWarehouse));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-08
    * @param: sysUserToWarehouse
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysUserToWarehouse sysUserToWarehouse ){
        sysUserToWarehouseService.save(sysUserToWarehouse);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-08
     * @param: sysUserToWarehouse
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysUserToWarehouse sysUserToWarehouse ){
        sysUserToWarehouseService.updateById(sysUserToWarehouse);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-08
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysUserToWarehouseService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-08
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysUserToWarehouseService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-08
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUserToWarehouse>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysUserToWarehouse> queryById(@RequestParam(name="id",required=true) int id) {
        SysUserToWarehouse sysUserToWarehouse = sysUserToWarehouseService.getById(id);
        return BaseResult.ok(sysUserToWarehouse);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-08
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出用户与仓库关联表")
    @PostMapping(path = "/exportSysUserToWarehouse")
    public void exportSysUserToWarehouse(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键ID",
                "用户id",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "租户编码",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间",
                "删除状态：0-未删除，1-已删除"
            );
            List<LinkedHashMap<String, Object>> dataList = sysUserToWarehouseService.querySysUserToWarehouseForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "用户与仓库关联表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
