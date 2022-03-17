package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.QueryClientShelfOrderTaskForm;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrderTask;
import com.jayud.wms.model.vo.ShelfOrderTaskVO;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IShelfOrderTaskService;
import com.jayud.wms.service.IWarehouseLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 上架任务单 控制类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Api(tags = "上架任务单")
@RestController
@RequestMapping("/shelfOrderTask")
public class ShelfOrderTaskController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IShelfOrderTaskService shelfOrderTaskService;

    @Autowired
    private AuthService authService;

    @Autowired
    private IWarehouseLocationService warehouseLocationService;

    /**
     * 分页查询数据
     *
     * @param queryShelfOrderTaskForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryShelfOrderTaskForm.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<ShelfOrderTaskVO>> selectPage(@RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                                      HttpServletRequest req) {
        IPage<ShelfOrderTaskVO> shelfOrderTaskIPage = shelfOrderTaskService.selectPage(queryShelfOrderTaskForm, queryShelfOrderTaskForm.getCurrentPage(), queryShelfOrderTaskForm.getPageSize(), req);
        return BaseResult.ok(shelfOrderTaskIPage);
    }


    /**
     * app app外部调用分页查询数据
     *
     * @param queryClientShelfOrderTaskForm
     * @return
     */
    @SysDataPermission(clazz = QueryShelfOrderTaskForm.class)
    @ApiOperation("app外部调用分页查询数据")
    @PostMapping(value = "/client/selectPage")
    public BaseResult<IPage<ShelfOrderTaskVO>> selectPageFeign(@RequestBody QueryClientShelfOrderTaskForm queryClientShelfOrderTaskForm) {
        HttpServletRequest req = null;
        QueryShelfOrderTaskForm queryShelfOrderTaskForm = ConvertUtil.convert(queryClientShelfOrderTaskForm, QueryShelfOrderTaskForm.class);
        IPage<ShelfOrderTaskVO> shelfOrderTaskIPage = shelfOrderTaskService.selectPage(queryShelfOrderTaskForm, queryShelfOrderTaskForm.getCurrentPage(), queryShelfOrderTaskForm.getPageSize(), req);
        return BaseResult.ok(shelfOrderTaskIPage);
    }

    /**
     * 列表查询数据
     *
     * @param queryShelfOrderTaskForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryShelfOrderTaskForm.class)
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<ShelfOrderTaskVO>> selectList(@RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                                     HttpServletRequest req) {
        return BaseResult.ok(shelfOrderTaskService.selectList(queryShelfOrderTaskForm));
    }

    /**
     * 强制上架
     **/
    @ApiOperation("强制上架")
    @PostMapping("/forcedShelf")
    public BaseResult forcedShelf(@Valid @RequestBody List<ShelfOrderTask> shelfOrderTasks) {
        for (ShelfOrderTask shelfOrderTask : shelfOrderTasks) {
            if (StringUtils.isEmpty(shelfOrderTask.getRecommendedLocation())) {
                return BaseResult.error(400, "上架任务号:" + shelfOrderTask.getTaskDetailNum() + "没有推荐库位");
            }
        }
        shelfOrderTaskService.forcedShelf(shelfOrderTasks);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    @ApiOperation("撤销上架")
    @PostMapping("/cancel")
    public BaseResult cancel(@Valid @RequestBody List<ShelfOrderTask> shelfOrderTasks) {
        shelfOrderTaskService.cancel(shelfOrderTasks);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

//    /**
//     * 编辑
//     *
//     * @param shelfOrderTask
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody ShelfOrderTask shelfOrderTask) {
//        shelfOrderTaskService.updateById(shelfOrderTask);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }
//
//    /**
//     * 保存(新增+编辑)
//     *
//     * @param shelfOrderTask
//     **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody ShelfOrderTask shelfOrderTask) {
//        ShelfOrderTask shelfOrderTask1 = shelfOrderTaskService.saveOrUpdateShelfOrderTask(shelfOrderTask);
//        return BaseResult.ok(shelfOrderTask1);
//    }

    /**
     * 物理删除
     *
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id) {
        shelfOrderTaskService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        shelfOrderTaskService.delShelfOrderTask(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<ShelfOrderTask> queryById(@RequestParam(name = "id", required = true) int id) {
        ShelfOrderTask shelfOrderTask = shelfOrderTaskService.getById(id);
        return BaseResult.ok(shelfOrderTask);
    }

    /**
     * 根据查询条件导出上架任务单
     *
     * @param response                响应对象
     * @param queryShelfOrderTaskForm 参数Map
     */
    @SysDataPermission(clazz = QueryShelfOrderTaskForm.class)
    @ApiOperation("根据查询条件导出上架任务单")
    @PostMapping(path = "/exportShelfOrderTask")
    public void exportShelfOrderTask(HttpServletResponse response, @RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                     HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList(
                    "仓库",
                    "货主",
                    "上架任务号",
                    "任务明细号",
                    "物料编号",
                    "单位",
                    "待上架数量",
                    "实际上架数量",
                    "容器号",
                    "推荐库位",
                    "实际上架库位",
                    "上架状态",
                    "起始工作站",
                    "起始播种位",
                    "上架执行人",
                    "上架时间",
                    "批次号",
                    "生产日期",
                    "备注1",
                    "备注2",
                    "备注3",
                    "创建人",
                    "创建时间"
            );
            List<LinkedHashMap<String, Object>> dataList = shelfOrderTaskService.queryShelfOrderTaskForExcel(queryShelfOrderTaskForm, req);
            ExcelUtils.exportExcel(headList, dataList, "上架任务单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


    @ApiOperation("上架任务查询下拉值")
    @GetMapping(value = "/getShelfOrderTaskType")
    public BaseResult getShelfOrderTaskType() {
        //上架任务查询下拉值
        List<LinkedHashMap<String, Object>> onTheTaskOnState = authService.queryDictByDictType("onTheTaskOnState");

        Map<String, Object> map = new HashMap<>();
        map.put("onTheTaskOnState", onTheTaskOnState);
        return BaseResult.ok(map);
    }


    //校验 上架任务单信息  以容器号校验
    @ApiOperation("以容器查询上架物料信息")
    @PostMapping(value = "/client/checkoutQueryClientShelfOrderTaskContainer")
    public BaseResult checkoutQueryClientShelfOrderTaskContainer(@RequestBody QueryClientShelfOrderTaskForm queryClientShelfOrderTaskForm) {

        //容器号
        QueryShelfOrderTaskForm queryShelfOrderTaskForm = new QueryShelfOrderTaskForm();
        queryShelfOrderTaskForm.setContainerNum(queryClientShelfOrderTaskForm.getContainerNum());//容器号
        queryShelfOrderTaskForm.setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId());//仓库id

        List<ShelfOrderTaskVO> shelfOrderTaskVOS = shelfOrderTaskService.selectListFeign(queryShelfOrderTaskForm);

        if (shelfOrderTaskVOS.size() == 0) {
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }
        return BaseResult.ok(shelfOrderTaskVOS.get(0));
    }


    //以上架任务单和物料编号校验
    @ApiOperation("以上架任务单和物料编号校验")
    @PostMapping(value = "/client/checkoutQueryClientShelfOrderTaskMaterialCode")
    public BaseResult checkoutQueryClientShelfOrderTaskMaterialCode(@RequestBody QueryClientShelfOrderTaskForm queryClientShelfOrderTaskForm) {
        //物料编号
        //上架任务号

        QueryShelfOrderTaskForm queryShelfOrderTaskForm = new QueryShelfOrderTaskForm();
        queryShelfOrderTaskForm.setShelfNum(queryClientShelfOrderTaskForm.getShelfNum());//上架任务号
        queryShelfOrderTaskForm.setMaterialCode(queryClientShelfOrderTaskForm.getMaterialCode());//物料编号
        queryShelfOrderTaskForm.setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId());//仓库id
        List<ShelfOrderTaskVO> shelfOrderTaskVOS = shelfOrderTaskService.selectListFeign(queryShelfOrderTaskForm);

        if (shelfOrderTaskVOS.size() == 0) {
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }
        return BaseResult.ok(shelfOrderTaskVOS.get(0));
    }


    @ApiOperation("以容器查询上架物料信息")
    @PostMapping(value = "/client/updateQueryClientShelfOrderTaskContainer")
    public BaseResult updateQueryClientShelfOrderTaskContainer(@RequestBody QueryClientShelfOrderTaskForm queryClientShelfOrderTaskForm) {

        //容器号
        QueryShelfOrderTaskForm queryShelfOrderTaskForm = new QueryShelfOrderTaskForm();
        queryShelfOrderTaskForm.setContainerNum(queryClientShelfOrderTaskForm.getContainerNum());//容器号
        queryShelfOrderTaskForm.setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId());//仓库id

        List<ShelfOrderTaskVO> shelfOrderTaskVOS = shelfOrderTaskService.selectListFeign(queryShelfOrderTaskForm);

        if (shelfOrderTaskVOS.size() == 0) {
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }


        // 校验库位信息

        //拿到仓库可以使用的 库位
        List<WarehouseLocationVO> warehouseLocations = this.warehouseLocationService.queryWarehouseLocation(new QueryInventoryForm().setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId()));
        logger.info("查询库用库位："+warehouseLocations);
        if(warehouseLocations.size()==0){
            return BaseResult.error(SysTips.STORAGE_LOCATION_INEXISTENCE_EXIST);
        }
        boolean present = warehouseLocations.stream().filter(s -> s.getCode().equals(queryClientShelfOrderTaskForm.getActualShelfSpace())).findAny().isPresent();
        if (!present) {
            return BaseResult.error(SysTips.STORAGE_LOCATION_INEXISTENCE_EXIST);
        }


        List<ShelfOrderTask> shelfOrderTasks = new ArrayList<>();
        shelfOrderTaskVOS.get(0).setProductionDate(null);
        ShelfOrderTask convert = ConvertUtil.convert(shelfOrderTaskVOS.get(0), ShelfOrderTask.class);
        convert.setActualShelfSpace(queryClientShelfOrderTaskForm.getActualShelfSpace());
        shelfOrderTasks.add(convert);
        shelfOrderTaskService.forcedShelf(shelfOrderTasks);
        return BaseResult.ok();
    }


    //以上架任务号和物料编号查询上架容器信息

    @ApiOperation("以上架任务号和物料编号查询上架容器信息")
    @PostMapping(value = "/client/updateQueryClientShelfOrderTaskMaterialCode")
    public BaseResult updateQueryClientShelfOrderTaskMaterialCode(@RequestBody QueryClientShelfOrderTaskForm queryClientShelfOrderTaskForm) {

        //物料编号
        //上架任务号

        QueryShelfOrderTaskForm queryShelfOrderTaskForm = new QueryShelfOrderTaskForm();
        queryShelfOrderTaskForm.setShelfNum(queryClientShelfOrderTaskForm.getShelfNum());//上架任务号
        queryShelfOrderTaskForm.setMaterialCode(queryClientShelfOrderTaskForm.getMaterialCode());//物料编号
        queryShelfOrderTaskForm.setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId());//仓库id
        List<ShelfOrderTaskVO> shelfOrderTaskVOS = shelfOrderTaskService.selectListFeign(queryShelfOrderTaskForm);

        if (shelfOrderTaskVOS.size() == 0) {
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }


        // 校验库位信息

        //拿到仓库可以使用的 库位
        List<WarehouseLocationVO> warehouseLocations = this.warehouseLocationService.queryWarehouseLocation(new QueryInventoryForm().setWarehouseId(queryClientShelfOrderTaskForm.getWarehouseId()));
        logger.info("查询库用库位："+warehouseLocations);
        if(warehouseLocations.size()==0){
            return BaseResult.error(SysTips.STORAGE_LOCATION_INEXISTENCE_EXIST);
        }

        boolean present = warehouseLocations.stream().filter(s -> s.getCode().equals(queryClientShelfOrderTaskForm.getActualShelfSpace())).findAny().isPresent();
        if (!present) {
            return BaseResult.error(SysTips.STORAGE_LOCATION_INEXISTENCE_EXIST);
        }

        List<ShelfOrderTask> shelfOrderTasks = new ArrayList<>();
        shelfOrderTaskVOS.get(0).setProductionDate(null);
        ShelfOrderTask convert = ConvertUtil.convert(shelfOrderTaskVOS.get(0), ShelfOrderTask.class);
        convert.setActualShelfSpace(queryClientShelfOrderTaskForm.getActualShelfSpace());
        shelfOrderTasks.add(convert);
        shelfOrderTaskService.forcedShelf(shelfOrderTasks);
        return BaseResult.ok();
    }


}
