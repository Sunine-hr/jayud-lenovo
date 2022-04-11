package com.jayud.wms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.wms.service.IWmsInventoryMovementService;
import com.jayud.wms.model.po.WmsInventoryMovement;

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
 * 库存移动订单表 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "库存移动订单表")
@RestController
@RequestMapping("/wmsInventoryMovement")
public class WmsInventoryMovementController {



    @Autowired
    public IWmsInventoryMovementService wmsInventoryMovementService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: wmsInventoryMovement
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsInventoryMovement>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<WmsInventoryMovement>> selectPage(WmsInventoryMovement wmsInventoryMovement,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,

                                                    HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(wmsInventoryMovementService.selectPage(wmsInventoryMovement,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: wmsInventoryMovement
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsInventoryMovement>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsInventoryMovement>> selectList(WmsInventoryMovement wmsInventoryMovement,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsInventoryMovementService.selectList(wmsInventoryMovement));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: wmsInventoryMovement
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsInventoryMovement wmsInventoryMovement ){
        wmsInventoryMovementService.save(wmsInventoryMovement);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: wmsInventoryMovement
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsInventoryMovement wmsInventoryMovement ){
        wmsInventoryMovementService.updateById(wmsInventoryMovement);
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
        wmsInventoryMovementService.phyDelById(id);
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
        wmsInventoryMovementService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.wms.model.po.WmsInventoryMovement>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsInventoryMovement> queryById(@RequestParam(name="id",required=true) int id) {
        WmsInventoryMovement wmsInventoryMovement = wmsInventoryMovementService.getById(id);
        return BaseResult.ok(wmsInventoryMovement);
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
    @ApiOperation("根据查询条件导出库存移动订单表")
    @PostMapping(path = "/exportWmsInventoryMovement")
    public void exportWmsInventoryMovement(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "移库单号",
                "订单状态(1:待移库,2:已移库,3：)",
                "租户编码",
                "移库完成时间",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsInventoryMovementService.queryWmsInventoryMovementForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存移动订单表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
