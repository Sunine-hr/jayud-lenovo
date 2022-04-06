package com.jayud.wms.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.InventoryAdjustmentForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.InventoryMovementForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.service.IInventoryDetailService;
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
 * 库存明细表 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "库存明细")
@RestController
@RequestMapping("/inventoryDetail")
public class InventoryDetailController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IInventoryDetailService inventoryDetailService;

    /**
     * 物料库存查询
     */
    @ApiOperation("物料库存查询")
    @GetMapping("/selectMaterialPage")
    public BaseResult<CommonPageResult<IPage<InventoryDetail>>> selectMaterialPage(InventoryDetail inventoryDetail,
                                                                                   @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                   HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryDetailService.selectMaterialPage(inventoryDetail, currentPage, pageSize, req)));
    }

    /**
     * 物料库存查询导出
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("物料库存查询导出")
    @PostMapping(path = "/exportInventoryMaterial")
    public void exportInventoryMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "仓库名称",
                    "货主名称",
                    "物料编号",
                    "物料名称",
                    "物料类型",
                    "物料规格",
                    "现有量",
                    "分配量",
                    "拣货量",
                    "可用量"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryDetailService.exportInventoryMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "物料库存", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 分页查询数据
     *
     * @param inventoryDetail 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<InventoryDetail>>> selectPage(@RequestBody InventoryDetail inventoryDetail,
                                                                           HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryDetailService.selectPage(inventoryDetail, inventoryDetail.getCurrentPage(), inventoryDetail.getPageSize(), req)));
    }

    /**
     * 分页查询数据Feign
     *
     * @param paramMap 查询条件
     * @return
     */
    @ApiOperation("分页查询数据Feign")
    @PostMapping("/selectPageByFeign")
    public BaseResult<CommonPageResult<IPage<InventoryDetail>>> selectPageByFeign(@RequestBody Map<String, Object> paramMap,
                                                                                  HttpServletRequest req) {
        InventoryDetail inventoryDetail = BeanUtil.fillBeanWithMap(paramMap, new InventoryDetail(), false);
        return BaseResult.ok(new CommonPageResult(inventoryDetailService.selectPage(inventoryDetail, inventoryDetail.getCurrentPage(), inventoryDetail.getPageSize(), req)));
    }

    /**
     * 列表查询数据
     *
     * @param inventoryDetail 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<InventoryDetail>> selectList(InventoryDetail inventoryDetail,
                                                        HttpServletRequest req) {
        return BaseResult.ok(inventoryDetailService.selectList(inventoryDetail));
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<InventoryDetail> queryById(@RequestParam(name = "id", required = true) int id) {
        InventoryDetail inventoryDetail = inventoryDetailService.getById(id);

        if (ObjectUtil.isEmpty(inventoryDetail)) {
            throw new IllegalArgumentException("库存明细不存在！");//(Ps：测试OpenFeign的异常情况)
        }
        return BaseResult.ok(inventoryDetail);
    }

    /**
     * 根据查询条件导出库存明细表
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出库存明细表")
    @PostMapping(path = "/exportInventoryDetail")
    public void exportInventoryDetail(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "仓库名称",
                    "货主",
                    "库位编号",
                    "库位状态",
                    "物料编号",
                    "物料名称",
                    "物料类别",
                    "现有量",
                    "可用量",
                    "分配量",
                    "拣货量",
                    "物料规格",
                    "容器号",
                    "批次号",
                    "生产日期",
                    "备注1",
                    "备注2",
                    "备注3",
                    "上架时间"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryDetailService.queryInventoryDetailForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存明细", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 入库
     **/
    @ApiOperation("入库")
    @PostMapping("/input")
    public BaseResult input(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.input(list);
    }

    /**
     * 出库分配
     */
    @ApiOperation("出库分配")
    @PostMapping("/outputAllocation")
    public BaseResult outputAllocation(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.outputAllocation(list);
    }

    /**
     * 库存调整
     */
    @ApiOperation("库存调整")
    @PostMapping("/adjustment")
    public BaseResult adjustment(@Valid @RequestBody InventoryAdjustmentForm bo) {
        return inventoryDetailService.adjustment(bo);
    }

    /**
     * 库存移动 改为移库任务了，现在没有用到这个接口
     */
    @ApiOperation("库存移动")
    @PostMapping("/movement")
    public BaseResult movement(@Valid @RequestBody List<InventoryMovementForm> list) {
        return inventoryDetailService.movement(list);

    }

    /**
     * 撤销出库分配
     */
    @ApiOperation("撤销出库分配")
    @PostMapping("/cancelOutputAllocation")
    public BaseResult cancelOutputAllocation(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.cancelOutputAllocation(list);
    }

    /**
     * 生成拣货
     */
    @ApiOperation("生成拣货")
    @PostMapping("/generatePicking")
    public BaseResult generatePicking(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.generatePicking(list);
    }

    /**
     * 撤销拣货
     */
    @ApiOperation("撤销拣货")
    @PostMapping("/cancelPicking")
    public BaseResult cancelPicking(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.cancelPicking(list);
    }

    /**
     * 出库 这里是`真正的出库`，同步扣减 拣货量、现有量
     */
    @ApiOperation("出库")
    @PostMapping("/output")
    public BaseResult output(@Valid @RequestBody List<InventoryDetailForm> list) {
        return inventoryDetailService.output(list);
    }

    /**
     * 库存可视化查询？
     */

    @ApiOperation("库存报表")
    @PostMapping(value = "/selectInventoryReport")
    public BaseResult selectInventoryReport(@RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        return inventoryDetailService.selectInventoryReport(queryShelfOrderTaskForm);
    }
}
