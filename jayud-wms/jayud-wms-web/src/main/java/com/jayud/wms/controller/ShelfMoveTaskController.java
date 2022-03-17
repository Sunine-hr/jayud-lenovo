package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.HikAGVFrom;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.CreateShelfMoveTaskForm;
import com.jayud.wms.model.po.ShelfMoveTask;
import com.jayud.wms.service.IShelfMoveTaskService;
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
 * 货架移动任务 控制类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Api(tags = "货架移动任务")
@RestController
@RequestMapping("/shelfMoveTask")
public class ShelfMoveTaskController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IShelfMoveTaskService shelfMoveTaskService;

    /**
     * 分页查询数据
     *
     * @param shelfMoveTask   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<ShelfMoveTask>>> selectPage(@RequestBody ShelfMoveTask shelfMoveTask,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(shelfMoveTaskService.selectPage(shelfMoveTask,shelfMoveTask.getCurrentPage(),shelfMoveTask.getPageSize(),req)));
    }

    /**
     * 列表查询数据
     *
     * @param shelfMoveTask   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<ShelfMoveTask>> selectList(ShelfMoveTask shelfMoveTask,
                                                HttpServletRequest req) {
        return BaseResult.ok(shelfMoveTaskService.selectList(shelfMoveTask));
    }

//    /**
//    * 新增
//    * @param shelfMoveTask
//    **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody ShelfMoveTask shelfMoveTask ){
//        shelfMoveTaskService.save(shelfMoveTask);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }

//    /**
//     * 编辑
//     * @param shelfMoveTask
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody ShelfMoveTask shelfMoveTask ){
//        shelfMoveTaskService.updateById(shelfMoveTask);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

    /**
    * 保存(新增+编辑)
    * @param shelfMoveTask
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody ShelfMoveTask shelfMoveTask ){
        ShelfMoveTask shelfMoveTask1 = shelfMoveTaskService.saveOrUpdateShelfMoveTask(shelfMoveTask);
        return BaseResult.ok(shelfMoveTask1);
    }

//    /**
//     * 物理删除
//     * @param id
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/del")
//    public BaseResult del(@RequestParam int id){
//        shelfMoveTaskService.removeById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        shelfMoveTaskService.delShelfMoveTask(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<ShelfMoveTask> queryById(@RequestParam(name="id",required=true) int id) {
        ShelfMoveTask shelfMoveTask = shelfMoveTaskService.getById(id);
        return BaseResult.ok(shelfMoveTask);
    }

    /**
     * 根据查询条件导出货架移动任务
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出货架移动任务")
    @PostMapping(path = "/exportShelfMoveTask")
    public void exportShelfMoveTask(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "货架移动任务号",
                "货架移动任务明细号",
                "任务类型",
                "订单状态",
                "订单来源",
                "货架编号",
                "所属仓库",
                "所属库区",
                "工作台编号",
                "完成时间",
                "创建人",
                "创建时间"
            );
            List<LinkedHashMap<String, Object>> dataList = shelfMoveTaskService.queryShelfMoveTaskForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "货架移动任务", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

    /**
     * 创建货架移库任务
     * @param form
     **/
    @ApiOperation("创建货架移库任务")
    @PostMapping("/createShelfMoveTask")
    public BaseResult createShelfMoveTask(@Valid @RequestBody CreateShelfMoveTaskForm form ){
        shelfMoveTaskService.createShelfMoveTask(form);
        return BaseResult.ok();
    }

    /**
     * 根据货架移动任务明细号，更新状态
     */
    @ApiOperation("根据货架移动任务明细号，更新状态")
    @PostMapping("/updateShelfMoveTaskByMxCode")
    public BaseResult updateShelfMoveTaskByMxCode(@RequestBody HikAGVFrom from){
        return shelfMoveTaskService.updateShelfMoveTaskByMxCode(from);
    }


}
