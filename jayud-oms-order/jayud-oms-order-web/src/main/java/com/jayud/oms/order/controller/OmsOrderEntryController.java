package com.jayud.oms.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.oms.order.service.IOmsOrderEntryService;
import com.jayud.oms.order.model.po.OmsOrderEntry;

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
 * 订单管理-订单明细表 控制类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Api(tags = "订单管理-订单明细表")
@RestController
@RequestMapping("/omsOrderEntry")
public class OmsOrderEntryController {



    @Autowired
    public IOmsOrderEntryService omsOrderEntryService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderEntry
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrderEntry>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<OmsOrderEntry>> selectPage(OmsOrderEntry omsOrderEntry,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(omsOrderEntryService.selectPage(omsOrderEntry,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrderEntry
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.oms.order.model.po.OmsOrderEntry>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<OmsOrderEntry>> selectList(OmsOrderEntry omsOrderEntry,
                                                HttpServletRequest req) {
        return BaseResult.ok(omsOrderEntryService.selectList(omsOrderEntry));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrderEntry
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody OmsOrderEntry omsOrderEntry ){
        omsOrderEntryService.save(omsOrderEntry);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderEntry
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody OmsOrderEntry omsOrderEntry ){
        omsOrderEntryService.updateById(omsOrderEntry);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        omsOrderEntryService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        omsOrderEntryService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.oms.order.model.po.OmsOrderEntry>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<OmsOrderEntry> queryById(@RequestParam(name="id",required=true) int id) {
        OmsOrderEntry omsOrderEntry = omsOrderEntryService.getById(id);
        return BaseResult.ok(omsOrderEntry);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-23
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出订单管理-订单明细表")
    @PostMapping(path = "/exportOmsOrderEntry")
    public void exportOmsOrderEntry(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "工作单类型",
                "订单主表ID",
                "工作单主表ID",
                "商品ID",
                "商品序号",
                "商品编码",
                "型号",
                "名称",
                "品牌",
                "产地",
                "商品描述",
                "配件",
                "数量",
                "单位",
                "币别",
                "采购单价",
                "报关单价",
                "海关编码",
                "申报要素",
                "监管条件",
                "香港管制",
                "是否商检",
                "商检类型",
                "商检编码",
                "包装方式",
                "件数",
                "板数",
                "毛重",
                "净重",
                "体积",
                "唛头",
                "料号",
                "批号",
                "箱号",
                "尺寸",
                "功能",
                "是否结关税(0为结,1为不结)",
                "指定关税率",
                "关税率",
                "消费税",
                "退税率",
                "品牌类型",
                "出口享惠情况",
                "用途/生产厂家",
                "境内目的地/境内货源地，进口指境内目的地，出口指境内货源地",
                "原产地区代码【原产国内的生产区域，如州、省等】",
                "组织机构ID",
                "多租户ID",
                "备注",
                "创建人名称",
                "创建时间",
                "最后修改人名称",
                "最后修改时间",
                "删除标志",
                "删除人",
                "删除时间"
            );
            List<LinkedHashMap<String, Object>> dataList = omsOrderEntryService.queryOmsOrderEntryForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "订单管理-订单明细表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
