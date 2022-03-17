package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsPackingOffshelfTaskVO;
import com.jayud.wms.model.vo.WmsPackingOffshelfVO;
import com.jayud.wms.service.IWmsPackingOffshelfTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架任务 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "拣货下架任务")
@RestController
@RequestMapping("/wmsPackingOffshelfTask")
public class WmsPackingOffshelfTaskController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsPackingOffshelfTaskService wmsPackingOffshelfTaskService;

    /**
     * 分页查询数据
     *
     * @param wmsPackingOffshelfTask   查询条件
     * @return
     */
    @SysDataPermission(clazz = WmsPackingOffshelfTask.class)
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsPackingOffshelfTask>>> selectPage(WmsPackingOffshelfTask wmsPackingOffshelfTask,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsPackingOffshelfTaskService.selectPage(wmsPackingOffshelfTask,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsPackingOffshelfTask   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsPackingOffshelfTask>> selectList(WmsPackingOffshelfTask wmsPackingOffshelfTask,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsPackingOffshelfTaskService.selectList(wmsPackingOffshelfTask));
    }

    /**
    * 新增
    * @param wmsPackingOffshelfTask
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsPackingOffshelfTask wmsPackingOffshelfTask ){
        wmsPackingOffshelfTaskService.save(wmsPackingOffshelfTask);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsPackingOffshelfTask
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsPackingOffshelfTask wmsPackingOffshelfTask ){
        wmsPackingOffshelfTaskService.updateById(wmsPackingOffshelfTask);
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
        wmsPackingOffshelfTaskService.phyDelById(id);
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
        wmsPackingOffshelfTaskService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsPackingOffshelfTask> queryById(@RequestParam(name="id",required=true) int id) {
        WmsPackingOffshelfTask wmsPackingOffshelfTask = wmsPackingOffshelfTaskService.getById(id);
        return BaseResult.ok(wmsPackingOffshelfTask);
    }

    /**
     * 根据查询条件导出拣货下架任务
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出拣货下架任务")
    @PostMapping(path = "/exportWmsPackingOffshelfTask")
    public void exportWmsPackingOffshelfTask(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键id",
                    "拣货下架单号",
                    "出库单号",
                    "波次号",
                    "分配物料明细id",
                    "任务明细号",
                    "仓库id",
                    "仓库编码",
                    "仓库名称",
                    "货主id",
                    "货主编码",
                    "货主名称",
                    "物料id",
                    "物料编码",
                    "物料名称",
                    "库区ID",
                    "库区编号",
                    "库区名称",
                    "库位ID",
                    "库位编号",
                    "容器id",
                    "容器编码",
                    "容器名称",
                    "下架库位id",
                    "下架库位编号",
                    "下架库位名称",
                    "待拣货下架数量",
                    "实际货下架数量",
                    "单位",
                    "拣货下架状态(1 待拣货下架,2 拣货下架中,3 拣货下架完成)",
                    "送达工作站id",
                    "送达工作站名称",
                    "送达播种位id",
                    "送达播种位名称",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsPackingOffshelfTaskService.queryWmsPackingOffshelfTaskForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "拣货下架任务", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("生成拣货下架单")
    @PostMapping(value = "/createPackingOffShelf")
    public BaseResult createPackingOffShelf(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO) {
        return wmsPackingOffshelfTaskService.createPackingOffShelf(outboundOrderNumberVO);
    }


    @ApiOperation("撤销拣货下架单")
    @PostMapping(value = "/cancelPackingOffShelf")
    public BaseResult cancelPackingOffShelf(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO) {
        return wmsPackingOffshelfTaskService.cancelPackingOffShelf(outboundOrderNumberVO);
    }

    @ApiOperation("接收拣货下架单详情")
    @GetMapping(value = "/receivePackingOffShelf")
    public BaseResult receivePackingOffShelf(WmsPackingOffshelfTask wmsPackingOffshelfTask) {
        return wmsPackingOffshelfTaskService.receivePackingOffShelf(wmsPackingOffshelfTask);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "taskDetailNumber",value = "任务明细码",dataType = "String",required = true),
                        @ApiImplicitParam(name = "realOffshelfAccount",value = "实际下架数量",dataType = "int",required = true),
                        @ApiImplicitParam(name = "offshelfLocationCode",value = "拣货下架库位编号",dataType = "String",required = true),
                        @ApiImplicitParam(name = "materialCode",value = "拣货下架物料编号",dataType = "String",required = true),
                        @ApiImplicitParam(name = "isEnd",value = "是否结束流程",dataType = "Boolean",required = true)})
    @ApiOperation("完成拣货下架单详情")
    @GetMapping(value = "/finishPackingOffShelf")
    public BaseResult finishPackingOffShelf(String taskDetailNumber, BigDecimal realOffshelfAccount,String offshelfLocationCode,String materialCode,Boolean isEnd) {
        WmsPackingOffshelfTask wmsPackingOffshelfTask = new WmsPackingOffshelfTask();
        wmsPackingOffshelfTask.setTaskDetailNumber(taskDetailNumber);
        wmsPackingOffshelfTask.setRealOffshelfAccount(realOffshelfAccount);
        wmsPackingOffshelfTask.setOffshelfLocationCode(offshelfLocationCode);
        wmsPackingOffshelfTask.setMaterialCode(materialCode);
        wmsPackingOffshelfTask.setIsEnd(isEnd);
        return wmsPackingOffshelfTaskService.finishPackingOffShelf(wmsPackingOffshelfTask);
    }

    @ApiOperation("获取需拣货下架任务")
    @GetMapping(value = "/getPackingTask")
    public BaseResult getPackingTask(String warehouseCode,String packingOffshelfNumber){
        WmsPackingOffshelfVO wmsPackingOffshelfVO = new WmsPackingOffshelfVO();
        wmsPackingOffshelfVO.setWarehouseCode(warehouseCode);
        wmsPackingOffshelfVO.setPackingOffshelfNumber(packingOffshelfNumber);
        return wmsPackingOffshelfTaskService.getPackingTask(wmsPackingOffshelfVO);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "packingOffshelfNumber",value = "下架任务号",dataType = "String",required = true),
                        @ApiImplicitParam(name = "descMsg",value = "降序",dataType = "String",required = true),
                        @ApiImplicitParam(name = "ascMsg",value = "升序",dataType = "String",required = true)})
    @ApiOperation("获取拣货下架任务详情")
    @GetMapping(value = "/getPackingTaskDetail")
    public BaseResult getPackingTaskDetail(String packingOffshelfNumber,String descMsg,String ascMsg){
        WmsPackingOffshelfVO wmsPackingOffshelfVO = new WmsPackingOffshelfVO();
        wmsPackingOffshelfVO.setPackingOffshelfNumber(packingOffshelfNumber);
        wmsPackingOffshelfVO.setDescMsg(descMsg);
        wmsPackingOffshelfVO.setAscMsg(ascMsg);
        return wmsPackingOffshelfTaskService.getPackingTaskDetail(wmsPackingOffshelfVO);
    }

    @ApiImplicitParam(name = "taskDetailNumber",value = "拣货下架任务详情号",dataType = "String",required = true)
    @ApiOperation("跳过拣货下架任务")
    @GetMapping(value = "/skipTaskDetail")
    public BaseResult skipTaskDetail(String taskDetailNumber){
        return wmsPackingOffshelfTaskService.skipTaskDetail(taskDetailNumber);
    }

    @ApiOperation("出库报表")
    @PostMapping(value = "/selectDeliveryReport")
    public BaseResult selectDeliveryReport(@RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        return wmsPackingOffshelfTaskService.selectDeliveryReport(queryShelfOrderTaskForm);
    }


    @ApiOperation("查询库位信息、拣货下架数据")
    @PostMapping(value = "/selectTaskAndLocationByMsg")
    public BaseResult<WmsPackingOffshelfTaskVO> selectTaskAndLocationByMsg(@RequestBody WmsPackingOffshelfTaskVO packingOffshelfTaskVO) {
        return wmsPackingOffshelfTaskService.selectTaskAndLocationByMsg(packingOffshelfTaskVO);
    }


    @ApiOperation("完成并结束拣货下架")
    @PostMapping(value = "/finishTaskAndEndOrder")
    public BaseResult finishTaskAndEndOrder(@RequestBody WmsPackingOffshelfTaskVO packingOffshelfTaskVO) {
        return wmsPackingOffshelfTaskService.finishTaskAndEndOrder(packingOffshelfTaskVO);
    }

}
