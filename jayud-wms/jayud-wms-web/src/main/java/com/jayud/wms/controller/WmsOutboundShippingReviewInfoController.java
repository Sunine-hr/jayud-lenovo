package com.jayud.wms.controller;

import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoService;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;

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
 * wms-出库发运复核 控制类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Slf4j
@Api(tags = "wms-出库发运复核")
@RestController
@RequestMapping("/wmsOutboundShippingReviewInfo")
public class WmsOutboundShippingReviewInfoController {



    @Autowired
    public IWmsOutboundShippingReviewInfoService wmsOutboundShippingReviewInfoService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>>
     **/
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<ListPageRuslt<WmsOutboundShippingReviewInfo>> selectPage(@RequestBody WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(wmsOutboundShippingReviewInfoService.selectPage(wmsOutboundShippingReviewInfo,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-07
    * @param: wmsOutboundShippingReviewInfo
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundShippingReviewInfo>> selectList(WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundShippingReviewInfoService.selectList(wmsOutboundShippingReviewInfo));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-07
    * @param: wmsOutboundShippingReviewInfo
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo ){
        wmsOutboundShippingReviewInfoService.save(wmsOutboundShippingReviewInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfo
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo ){
        wmsOutboundShippingReviewInfoService.updateById(wmsOutboundShippingReviewInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        wmsOutboundShippingReviewInfoService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        wmsOutboundShippingReviewInfoService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundShippingReviewInfo> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo = wmsOutboundShippingReviewInfoService.getById(id);
        return BaseResult.ok(wmsOutboundShippingReviewInfo);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-07
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出wms-出库发运复核")
    @PostMapping(path = "/exportWmsOutboundShippingReviewInfo")
    public void exportWmsOutboundShippingReviewInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库通知单号",
                "出库单号",
                "发运复核订单号",
                "主订单号",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "货主id",
                "货主编码",
                "货主名称",
                "单据类型(1待复核,2复核中,3已复核)",
                "订单来源(1ERP下发,2EMS下发,3手工创建)",
                "外部订单号1",
                "外部订单号2",
                "客户id",
                "客户编码",
                "客户名称",
                "备用字段1",
                "备用字段2",
                "合计数量",
                "合计重量",
                "合计体积",
                "单位",
                "车牌号",
                "车型",
                "司机名称",
                "联系方式",
                "作业人员id",
                "作业人员名称",
                "确认人员id",
                "确认人员名称",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundShippingReviewInfoService.queryWmsOutboundShippingReviewInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "wms-出库发运复核", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    @ApiOperation("根据编码获取数据")
    @GetMapping(value = "/queryByCode")
    public BaseResult<WmsOutboundShippingReviewInfoVO> queryByCode(WmsOutboundShippingReviewInfo info){
        return BaseResult.ok(wmsOutboundShippingReviewInfoService.queryByCode(info));
    }

    @ApiOperation("根据编码集合删除")
    @GetMapping(value = "/delByOrderNumbers")
    public BaseResult delByOrderNumbers(DeleteForm deleteForm){
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }



}
