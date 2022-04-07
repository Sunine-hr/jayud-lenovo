package com.jayud.wms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.wms.service.IWmsOutboundOrderInfoToServiceService;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToService;

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
 * 出库单-附加服务 控制类
 *
 * @author jayud
 * @since 2022-04-06
 */
@Slf4j
@Api(tags = "出库单-附加服务")
@RestController
@RequestMapping("/wmsOutboundOrderInfoToService")
public class WmsOutboundOrderInfoToServiceController {



    @Autowired
    public IWmsOutboundOrderInfoToServiceService wmsOutboundOrderInfoToServiceService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-06
     * @param: wmsOutboundOrderInfoToService
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<WmsOutboundOrderInfoToService>> selectPage(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(wmsOutboundOrderInfoToServiceService.selectPage(wmsOutboundOrderInfoToService,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-06
    * @param: wmsOutboundOrderInfoToService
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundOrderInfoToService>> selectList(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundOrderInfoToServiceService.selectList(wmsOutboundOrderInfoToService));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-06
    * @param: wmsOutboundOrderInfoToService
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService ){
        wmsOutboundOrderInfoToServiceService.save(wmsOutboundOrderInfoToService);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-06
     * @param: wmsOutboundOrderInfoToService
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService ){
        wmsOutboundOrderInfoToServiceService.updateById(wmsOutboundOrderInfoToService);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        wmsOutboundOrderInfoToServiceService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        wmsOutboundOrderInfoToServiceService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundOrderInfoToService> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService = wmsOutboundOrderInfoToServiceService.getById(id);
        return BaseResult.ok(wmsOutboundOrderInfoToService);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-06
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出出库单-附加服务")
    @PostMapping(path = "/exportWmsOutboundOrderInfoToService")
    public void exportWmsOutboundOrderInfoToService(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库单号",
                "服务名称",
                "服务名称文本",
                "物料名称",
                "物料规格",
                "数量",
                "单位",
                "单价",
                "总价",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundOrderInfoToServiceService.queryWmsOutboundOrderInfoToServiceForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库单-附加服务", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
