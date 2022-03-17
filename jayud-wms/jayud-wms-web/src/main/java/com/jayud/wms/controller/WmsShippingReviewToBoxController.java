package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsShippingReviewToBox;
import com.jayud.wms.service.IWmsShippingReviewToBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核-箱数关系 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "发运复核-箱数关系")
@RestController
@RequestMapping("/wmsShippingReviewToBox")
public class WmsShippingReviewToBoxController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsShippingReviewToBoxService wmsShippingReviewToBoxService;

    /**
     * 分页查询数据
     *
     * @param wmsShippingReviewToBox   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsShippingReviewToBox>>> selectPage(WmsShippingReviewToBox wmsShippingReviewToBox,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsShippingReviewToBoxService.selectPage(wmsShippingReviewToBox,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsShippingReviewToBox   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsShippingReviewToBox>> selectList(WmsShippingReviewToBox wmsShippingReviewToBox,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsShippingReviewToBoxService.selectList(wmsShippingReviewToBox));
    }

    /**
    * 新增
    * @param wmsShippingReviewToBox
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsShippingReviewToBox wmsShippingReviewToBox ){
        wmsShippingReviewToBoxService.save(wmsShippingReviewToBox);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsShippingReviewToBox
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsShippingReviewToBox wmsShippingReviewToBox ){
        wmsShippingReviewToBoxService.updateById(wmsShippingReviewToBox);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam int id){
        wmsShippingReviewToBoxService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 逻辑删除
    * @param id
    **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        wmsShippingReviewToBoxService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsShippingReviewToBox> queryById(@RequestParam(name="id",required=true) int id) {
        WmsShippingReviewToBox wmsShippingReviewToBox = wmsShippingReviewToBoxService.getById(id);
        return BaseResult.ok(wmsShippingReviewToBox);
    }

    /**
     * 根据查询条件导出发运复核-箱数关系
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出发运复核-箱数关系")
    @PostMapping(path = "/exportWmsShippingReviewToBox")
    public void exportWmsShippingReviewToBox(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库单号",
                "拣货下架单id",
                "拣货下架单号",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "货主id",
                "货主编码",
                "货主名称",
                "物料id",
                "物料编码",
                "物料名称",
                "拣货数量",
                "箱编号",
                "数量",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsShippingReviewToBoxService.queryWmsShippingReviewToBoxForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "发运复核-箱数关系", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
