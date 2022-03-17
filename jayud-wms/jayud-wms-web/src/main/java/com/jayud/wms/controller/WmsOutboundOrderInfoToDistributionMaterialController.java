package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import com.jayud.wms.service.IWmsOutboundOrderInfoToDistributionMaterialService;
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
 * 出库单-分配物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "出库单-分配物料信息")
@RestController
@RequestMapping("/wmsOutboundOrderInfoToDistributionMaterial")
public class WmsOutboundOrderInfoToDistributionMaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundOrderInfoToDistributionMaterialService wmsOutboundOrderInfoToDistributionMaterialService;

    /**
     * 分页查询数据
     *
     * @param wmsOutboundOrderInfoToDistributionMaterial   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsOutboundOrderInfoToDistributionMaterial>>> selectPage(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsOutboundOrderInfoToDistributionMaterialService.selectPage(wmsOutboundOrderInfoToDistributionMaterial,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundOrderInfoToDistributionMaterial   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundOrderInfoToDistributionMaterial>> selectList(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundOrderInfoToDistributionMaterialService.selectList(wmsOutboundOrderInfoToDistributionMaterial));
    }

    /**
    * 新增
    * @param wmsOutboundOrderInfoToDistributionMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial ){
        wmsOutboundOrderInfoToDistributionMaterialService.save(wmsOutboundOrderInfoToDistributionMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundOrderInfoToDistributionMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial ){
        wmsOutboundOrderInfoToDistributionMaterialService.updateById(wmsOutboundOrderInfoToDistributionMaterial);
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
        wmsOutboundOrderInfoToDistributionMaterialService.phyDelById(id);
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
        wmsOutboundOrderInfoToDistributionMaterialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundOrderInfoToDistributionMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial = wmsOutboundOrderInfoToDistributionMaterialService.getById(id);
        return BaseResult.ok(wmsOutboundOrderInfoToDistributionMaterial);
    }

    /**
     * 根据查询条件导出出库单-分配物料信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库单-分配物料信息")
    @PostMapping(path = "/exportWmsOutboundOrderInfoToDistributionMaterial")
    public void exportWmsOutboundOrderInfoToDistributionMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库单物料信息id",
                "出库单号",
                "波次号",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "货主id",
                "货主编码",
                "货主名称",
                "物料id",
                "物料编码",
                "物料名称",
                "出库物料id",
                "库区ID",
                "库区编号",
                "库区名称",
                "库位ID",
                "库位编号",
                "容器id",
                "容器号",
                "分配量",
                "实际分配量",
                "单位",
                "外部单号",
                "外部单行号",
                "批次号",
                "生产时间",
                "自定义1",
                "自定义2",
                "自定义3",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundOrderInfoToDistributionMaterialService.queryWmsOutboundOrderInfoToDistributionMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库单-分配物料信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @GetMapping(path = "/cancleDistributionToUnpacking")
    public BaseResult cancleDistributionToUnpacking(String orderNumber,boolean isWave){
        return wmsOutboundOrderInfoToDistributionMaterialService.cancleDistributionToUnpacking(orderNumber,isWave);
    }

}
