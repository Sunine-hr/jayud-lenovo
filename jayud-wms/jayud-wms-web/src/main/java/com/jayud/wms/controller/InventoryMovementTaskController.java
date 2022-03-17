package com.jayud.wms.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.InventoryMovementTaskCompletedForm;
import com.jayud.wms.model.bo.InventoryMovementTaskForm;
import com.jayud.wms.model.po.InventoryMovementTask;
import com.jayud.wms.model.vo.InventoryMovementTaskAppVO;
import com.jayud.wms.model.vo.InventoryMovementTaskVO;
import com.jayud.wms.service.IInventoryMovementTaskService;
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
 * 库存移库任务 控制类
 *
 * @author jyd
 * @since 2021-12-30
 */
@Api(tags = "库存移库任务")
@RestController
@RequestMapping("/inventoryMovementTask")
public class InventoryMovementTaskController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IInventoryMovementTaskService inventoryMovementTaskService;

    /**
     * 分页查询数据
     *
     * @param inventoryMovementTask   查询条件
     * @return
     */
    @SysDataPermission(clazz = InventoryMovementTask.class)
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<InventoryMovementTask>>> selectPage(InventoryMovementTask inventoryMovementTask,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryMovementTaskService.selectPage(inventoryMovementTask,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param inventoryMovementTask   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<InventoryMovementTask>> selectList(InventoryMovementTask inventoryMovementTask,
                                                HttpServletRequest req) {
        return BaseResult.ok(inventoryMovementTaskService.selectList(inventoryMovementTask));
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<InventoryMovementTask> queryById(@RequestParam(name="id",required=true) int id) {
        InventoryMovementTask inventoryMovementTask = inventoryMovementTaskService.getById(id);
        return BaseResult.ok(inventoryMovementTask);
    }

    /**
     * 根据查询条件导出库存移库任务
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出库存移库任务")
    @PostMapping(path = "/exportInventoryMovementTask")
    public void exportInventoryMovementTask(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "移库任务号",
                "任务明细号",
                "状态",
                "仓库名称",
                "货主",
                "物料编号",
                "容器号",
                "起始库位编号",
                "目标库位编号",
                "目标容器号",
                "移动数量",
                "现有量",
                "可用量",
                "分配量",
                "拣货量",
                "物料规格",
                "批次号",
                "生产日期",
                "备注1",
                "备注2",
                "备注3",
                "移库执行人",
                "移库时间"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryMovementTaskService.queryInventoryMovementTaskForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存移库任务", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

    /**
     * 生成库存移动任务 generateInventoryMovementTasks
     */
    @ApiOperation("生成库存移动任务")
    @PostMapping("/generateInventoryMovementTasks")
    public BaseResult<InventoryMovementTaskVO> generateInventoryMovementTasks(@Valid @RequestBody InventoryMovementTaskForm form){
        InventoryMovementTaskVO vo = inventoryMovementTaskService.generateInventoryMovementTasks(form);
        return BaseResult.ok(vo);
    }

    /**
     * 库存移动任务确认 inventoryMovementTaskConfirmation
     */
    @ApiOperation("库存移动任务确认")
    @PostMapping("/inventoryMovementTaskConfirmation")
    public BaseResult inventoryMovementTaskConfirmation(@Valid @RequestBody InventoryMovementTaskForm form){
        inventoryMovementTaskService.inventoryMovementTaskConfirmation(form);
        return BaseResult.ok();
    }

    /**
     * 库存移动任务完成 inventoryMovementTaskCompleted
     */
    @ApiOperation("库存移动任务完成")
    @PostMapping("/inventoryMovementTaskCompleted")
    public BaseResult inventoryMovementTaskCompleted(@Valid @RequestBody InventoryMovementTaskCompletedForm form){
        inventoryMovementTaskService.inventoryMovementTaskCompleted(form);
        return BaseResult.ok();
    }

    /**
     * 移库作业(汇总)查询Feign
     *
     * @param paramMap   查询条件
     * @return
     */
    @ApiOperation("分页查询数据Feign")
    @PostMapping("/selectPageByFeign")
    public BaseResult<CommonPageResult<IPage<InventoryMovementTaskAppVO>>> selectPageByFeign(@RequestBody Map<String, Object> paramMap,
                                                                                         HttpServletRequest req) {
        InventoryMovementTask inventoryMovementTask = BeanUtil.fillBeanWithMap(paramMap, new InventoryMovementTask(), false);
        return BaseResult.ok(new CommonPageResult(inventoryMovementTaskService.selectPageByFeign(inventoryMovementTask,inventoryMovementTask.getCurrentPage(), inventoryMovementTask.getPageSize(),req)));
    }

    /**
     * 去移库(移库详情)
     */
    @ApiOperation("去移库(移库详情)")
    @PostMapping("/queryInventoryMovementTaskByApp")
    public BaseResult<InventoryMovementTaskAppVO> queryInventoryMovementTaskByMainCode(@RequestBody Map<String, Object> paramMap){
        InventoryMovementTaskAppVO inventoryMovementTaskAppVO = BeanUtil.fillBeanWithMap(paramMap, new InventoryMovementTaskAppVO(), false);
        String mainCode = inventoryMovementTaskAppVO.getMainCode();
        InventoryMovementTaskAppVO vo = inventoryMovementTaskService.queryInventoryMovementTaskByMainCode(mainCode);
        return BaseResult.ok(vo);
    }

    /**
     * 移库确认
     */
    @ApiOperation("移库确认")
    @PostMapping("/inventoryMovementTaskCompletedByApp")
    public BaseResult<InventoryMovementTaskAppVO> inventoryMovementTaskCompletedByApp(@RequestBody Map<String, Object> paramMap){
        InventoryMovementTaskAppVO inventoryMovementTaskAppVO = BeanUtil.fillBeanWithMap(paramMap, new InventoryMovementTaskAppVO(), false);
        //如果有多条 确认当前任务后，返回下一条的任务，没有下一条任务，返回空
        InventoryMovementTaskAppVO vo = inventoryMovementTaskService.inventoryMovementTaskCompletedByApp(inventoryMovementTaskAppVO);
        return BaseResult.ok(vo);
    }

}
