package com.jayud.wms.controller;

import com.jayud.wms.model.bo.BreakoutWorkbenchForm;
import com.jayud.wms.model.po.BreakoutWorkbench;
import com.jayud.wms.service.IBreakoutWorkbenchService;
import com.jayud.wms.model.vo.WorkbenchVO;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 工作台类型信息 控制类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Api(tags = "工作台类型信息")
@RestController
@RequestMapping("/breakoutWorkbench")
public class BreakoutWorkbenchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IBreakoutWorkbenchService breakoutWorkbenchService;

//    /**
//     * 分页查询数据
//     *
//     * @param breakoutWorkbench   查询条件
//     * @return
//     */
//    @ApiOperation("分页查询数据")
//    @GetMapping("/selectPage")
//    public BaseResult<CommonPageResult<IPage<BreakoutWorkbench>>> selectPage(BreakoutWorkbench breakoutWorkbench,
//                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
//                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//                                                HttpServletRequest req) {
//        return BaseResult.ok(new CommonPageResult(breakoutWorkbenchService.selectPage(breakoutWorkbench,currentPage,pageSize,req)));
//    }

//    /**
//     * 列表查询数据
//     *
//     * @param breakoutWorkbench   查询条件
//     * @return
//     */
//    @ApiOperation("列表查询数据")
//    @GetMapping("/selectList")
//    public BaseResult<List<BreakoutWorkbench>> selectList(BreakoutWorkbench breakoutWorkbench,
//                                                HttpServletRequest req) {
//        return BaseResult.ok(breakoutWorkbenchService.selectList(breakoutWorkbench));
//    }

//    /**
//    * 新增
//    * @param breakoutWorkbench
//    **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody BreakoutWorkbench breakoutWorkbench ){
//        breakoutWorkbenchService.save(breakoutWorkbench);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }

//    /**
//     * 编辑
//     * @param breakoutWorkbench
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody BreakoutWorkbench breakoutWorkbench ){
//        breakoutWorkbenchService.updateById(breakoutWorkbench);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

//    /**
//    * 保存(新增+编辑)
//    * @param breakoutWorkbench
//    **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody BreakoutWorkbench breakoutWorkbench ){
//        BreakoutWorkbench breakoutWorkbench1 = breakoutWorkbenchService.saveOrUpdateBreakoutWorkbench(breakoutWorkbench);
//        return BaseResult.ok(breakoutWorkbench1);
//    }

//    /**
//     * 物理删除
//     * @param id
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/del")
//    public BaseResult del(@RequestParam int id){
//        breakoutWorkbenchService.removeById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

//    /**
//     * 逻辑删除
//     * @param id
//     **/
//    @ApiOperation("逻辑删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/logicDel")
//    public BaseResult logicDel(@RequestParam int id){
//        breakoutWorkbenchService.delBreakoutWorkbench(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<BreakoutWorkbench> queryById(@RequestParam(name="id",required=true) int id) {
        BreakoutWorkbench breakoutWorkbench = breakoutWorkbenchService.getById(id);
        return BaseResult.ok(breakoutWorkbench);
    }

//    /**
//     * 根据查询条件导出工作台类型信息
//     *
//     * @param response 响应对象
//     * @param paramMap 参数Map
//     */
//    @ApiOperation("根据查询条件导出工作台类型信息")
//    @PostMapping(path = "/exportBreakoutWorkbench")
//    public void exportBreakoutWorkbench(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
//        try {
//            List<String> headList = Arrays.asList(
//                "主键",
//                "编号",
//                "备注信息",
//                "是否删除",
//                "工作台类型(1:普通,2:分播,3:交接)",
//                "工作台id"
//            );
//            List<LinkedHashMap<String, Object>> dataList = breakoutWorkbenchService.queryBreakoutWorkbenchForExcel(paramMap);
//            ExcelUtils.exportExcel(headList, dataList, "工作台类型信息", response);
//        } catch (Exception e) {
//            logger.warn(e.toString());
//        }
//    }

    /**
     * 根据工作台id，获取工作台业务信息
     */
    @ApiOperation("根据工作台id，获取工作台业务信息")
    @ApiImplicitParam(name = "workbenchId",value = "工作台id",dataType = "int",required = true)
    @GetMapping(value = "/queryByWorkbenchId")
    public BaseResult<WorkbenchVO> queryByWorkbenchId(@RequestParam(name="workbenchId", required=true) int workbenchId){
        WorkbenchVO workbenchVO = breakoutWorkbenchService.queryByWorkbenchId(workbenchId);
        return BaseResult.ok(workbenchVO);
    }

    /**
     * 保存工作台业务信息
     */
    @ApiOperation("保存工作台业务信息")
    @PostMapping("/saveBreakoutWorkbench")
    public BaseResult saveBreakoutWorkbench(@RequestBody WorkbenchVO bo){
        breakoutWorkbenchService.saveBreakoutWorkbench(bo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);

    }

    /**
     * 根据工作台id，添加播种墙编号
     */
    @ApiOperation("根据工作台id，添加播种墙编号")
    @PostMapping("/addBreakoutWorkbench")
    public BaseResult<BreakoutWorkbench> addBreakoutWorkbench(@Valid @RequestBody BreakoutWorkbenchForm bo){
        BreakoutWorkbench breakoutWorkbench = breakoutWorkbenchService.addBreakoutWorkbench(bo);
        return BaseResult.ok(breakoutWorkbench);
    }


}
