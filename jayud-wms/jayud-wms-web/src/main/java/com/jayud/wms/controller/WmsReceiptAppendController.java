package com.jayud.wms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.wms.service.IWmsReceiptAppendService;
import com.jayud.wms.model.po.WmsReceiptAppend;

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
 * 收货单附加服务表 控制类
 *
 * @author jayud
 * @since 2022-03-31
 */
@Slf4j
@Api(tags = "收货单附加服务表")
@RestController
@RequestMapping("/wmsReceiptAppend")
public class WmsReceiptAppendController {



    @Autowired
    public IWmsReceiptAppendService wmsReceiptAppendService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-31
     * @param: wmsReceiptAppend
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsReceiptAppend>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<WmsReceiptAppend>> selectPage(WmsReceiptAppend wmsReceiptAppend,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(wmsReceiptAppendService.selectPage(wmsReceiptAppend,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-31
    * @param: wmsReceiptAppend
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsReceiptAppend>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsReceiptAppend>> selectList(WmsReceiptAppend wmsReceiptAppend,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsReceiptAppendService.selectList(wmsReceiptAppend));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-31
    * @param: wmsReceiptAppend
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsReceiptAppend wmsReceiptAppend ){
        wmsReceiptAppendService.save(wmsReceiptAppend);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-31
     * @param: wmsReceiptAppend
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsReceiptAppend wmsReceiptAppend ){
        wmsReceiptAppendService.updateById(wmsReceiptAppend);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        wmsReceiptAppendService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        wmsReceiptAppendService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.wms.model.po.WmsReceiptAppend>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsReceiptAppend> queryById(@RequestParam(name="id",required=true) int id) {
        WmsReceiptAppend wmsReceiptAppend = wmsReceiptAppendService.getById(id);
        return BaseResult.ok(wmsReceiptAppend);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-31
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出收货单附加服务表")
    @PostMapping(path = "/exportWmsReceiptAppend")
    public void exportWmsReceiptAppend(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "收货单id",
                "收货单号",
                "服务名称",
                "数量",
                "单位",
                "总价",
                "租户编码",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsReceiptAppendService.queryWmsReceiptAppendForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "收货单附加服务表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
