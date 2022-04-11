package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.result.BasePage;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoToMaterialService;
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
 * 出库通知订单-物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "出库通知订单-物料信息")
@RestController
@RequestMapping("/wmsOutboundNoticeOrderInfoToMaterial")
public class WmsOutboundNoticeOrderInfoToMaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundNoticeOrderInfoToMaterialService wmsOutboundNoticeOrderInfoToMaterialService;

    /**
     * 分页查询数据
     *
     * @param wmsOutboundNoticeOrderInfoToMaterialVO   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsOutboundNoticeOrderInfoToMaterialVO>>> selectPage(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsOutboundNoticeOrderInfoToMaterialService.selectPage(wmsOutboundNoticeOrderInfoToMaterialVO,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundNoticeOrderInfoToMaterialVO   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundNoticeOrderInfoToMaterialVO>> selectList(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO,
                                                                           HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundNoticeOrderInfoToMaterialService.selectList(wmsOutboundNoticeOrderInfoToMaterialVO));
    }

    /**
    * 新增
    * @param wmsOutboundNoticeOrderInfoToMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundNoticeOrderInfoToMaterial wmsOutboundNoticeOrderInfoToMaterial ){
        wmsOutboundNoticeOrderInfoToMaterialService.save(wmsOutboundNoticeOrderInfoToMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundNoticeOrderInfoToMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundNoticeOrderInfoToMaterial wmsOutboundNoticeOrderInfoToMaterial ){
        wmsOutboundNoticeOrderInfoToMaterialService.updateById(wmsOutboundNoticeOrderInfoToMaterial);
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
        wmsOutboundNoticeOrderInfoToMaterialService.phyDelById(id);
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
        wmsOutboundNoticeOrderInfoToMaterialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundNoticeOrderInfoToMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundNoticeOrderInfoToMaterial wmsOutboundNoticeOrderInfoToMaterial = wmsOutboundNoticeOrderInfoToMaterialService.getById(id);
        return BaseResult.ok(wmsOutboundNoticeOrderInfoToMaterial);
    }

    /**
     * 根据查询条件导出出库通知订单-物料信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库通知订单-物料信息")
    @PostMapping(path = "/exportWmsOutboundNoticeOrderInfoToMaterial")
    public void exportWmsOutboundNoticeOrderInfoToMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库通知单号",
                "物料id",
                "物料编码",
                "物料名称",
                "数量",
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
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundNoticeOrderInfoToMaterialService.queryWmsOutboundNoticeOrderInfoToMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库通知订单-物料信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("获取库存物料信息")
    @PostMapping(path = "/getInventoryMetailDetailList")
    public BaseResult<List<WmsOutboundNoticeOrderInfoToMaterialVO>> getInventoryMetailDetailList(@RequestBody InventoryDetail inventoryDetail){
        return BaseResult.ok(wmsOutboundNoticeOrderInfoToMaterialService.getInventoryMetailDetailList(inventoryDetail));
    }

    @ApiOperation("获取货品信息")
    @PostMapping(path = "/selectInvenDetail")
    public BaseResult<BasePage<WmsOutboundNoticeOrderInfoToMaterialVO>> selectInvenDetail(@RequestBody WmsOutboundNoticeOrderInfoToMaterialVO material,
                                                                                          @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                          HttpServletRequest req){
        return BaseResult.ok(wmsOutboundNoticeOrderInfoToMaterialService.selectInvenDetail(material, currentPage, pageSize));
    }

}
