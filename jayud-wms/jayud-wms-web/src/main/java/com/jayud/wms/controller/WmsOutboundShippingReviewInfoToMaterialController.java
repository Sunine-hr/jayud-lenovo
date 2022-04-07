package com.jayud.wms.controller;

import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoToMaterialService;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;

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
 * wms-出库发运复核物料信息 控制类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Slf4j
@Api(tags = "wms-出库发运复核物料信息")
@RestController
@RequestMapping("/wmsOutboundShippingReviewInfoToMaterial")
public class WmsOutboundShippingReviewInfoToMaterialController {



    @Autowired
    public IWmsOutboundShippingReviewInfoToMaterialService wmsOutboundShippingReviewInfoToMaterialService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<WmsOutboundShippingReviewInfoToMaterial>> selectPage(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(wmsOutboundShippingReviewInfoToMaterialService.selectPage(wmsOutboundShippingReviewInfoToMaterial,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-07
    * @param: wmsOutboundShippingReviewInfoToMaterial
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> selectList(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundShippingReviewInfoToMaterialService.selectList(wmsOutboundShippingReviewInfoToMaterial));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-07
    * @param: wmsOutboundShippingReviewInfoToMaterial
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial ){
        wmsOutboundShippingReviewInfoToMaterialService.save(wmsOutboundShippingReviewInfoToMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial ){
        wmsOutboundShippingReviewInfoToMaterialService.updateById(wmsOutboundShippingReviewInfoToMaterial);
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
        wmsOutboundShippingReviewInfoToMaterialService.phyDelById(id);
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
        wmsOutboundShippingReviewInfoToMaterialService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundShippingReviewInfoToMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial = wmsOutboundShippingReviewInfoToMaterialService.getById(id);
        return BaseResult.ok(wmsOutboundShippingReviewInfoToMaterial);
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
    @ApiOperation("根据查询条件导出wms-出库发运复核物料信息")
    @PostMapping(path = "/exportWmsOutboundShippingReviewInfoToMaterial")
    public void exportWmsOutboundShippingReviewInfoToMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "发运复核编号",
                "出库单物料id",
                "物料id",
                "物料编码",
                "物料名称",
                "物料规格",
                "需求量",
                "单位",
                "外部单号",
                "外部单行号",
                "批次号",
                "生产时间",
                "自定义1",
                "自定义2",
                "自定义3",
                "状态(1未发运复核，2已发运复核)",
                "容器id",
                "容器编码",
                "上传图片地址",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundShippingReviewInfoToMaterialService.queryWmsOutboundShippingReviewInfoToMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "wms-出库发运复核物料信息", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    @ApiOperation("确认发运复核")
    @PostMapping("/comfirmReview")
    public BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> comfirmReview(@RequestBody WmsOutboundShippingReviewInfoVO reviewInfo){
        return wmsOutboundShippingReviewInfoToMaterialService.comfirmReview(reviewInfo);
    }

    @ApiOperation("撤销发运复核")
    @PostMapping("/cancelReview")
    public BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> cancelReview(@RequestBody WmsOutboundShippingReviewInfoVO reviewInfo){
        return wmsOutboundShippingReviewInfoToMaterialService.comfirmReview(reviewInfo);
    }

}
