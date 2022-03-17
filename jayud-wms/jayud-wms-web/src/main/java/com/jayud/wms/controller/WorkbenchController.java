package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.bo.PutawayBackLibraryForm;
import com.jayud.wms.model.bo.QueryWarehouseShelfForm;
import com.jayud.wms.model.po.Workbench;
import com.jayud.wms.model.vo.WarehouseShelfSketchMapVO;
import com.jayud.wms.service.IWorkbenchService;
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
import java.util.*;

/**
 * 工作台 控制类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Api(tags = "工作台")
@RestController
@RequestMapping("/workbench")
public class WorkbenchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWorkbenchService workbenchService;
    @Autowired
    private AuthClient authClient;

    /**
     * 分页查询数据
     *
     * @param workbench   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<Workbench>>> selectPage(Workbench workbench,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(workbenchService.selectPage(workbench,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param workbench   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<Workbench>> selectList(Workbench workbench,
                                                HttpServletRequest req) {
        return BaseResult.ok(workbenchService.selectList(workbench));
    }

    /**
    * 保存(新增+编辑)
    * @param workbench
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody Workbench workbench ){
        Workbench workbench1 = workbenchService.saveOrUpdateWorkbench(workbench);
        return BaseResult.ok(workbench1);
    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        workbenchService.delWorkbench(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<Workbench> queryById(@RequestParam(name="id",required=true) int id) {
        Workbench workbench = workbenchService.getById(id);
        return BaseResult.ok(workbench);
    }

    /**
     * 根据查询条件导出工作台
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出工作台")
    @PostMapping(path = "/exportWorkbench")
    public void exportWorkbench(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "序号",
                "所属仓库",
                "所属仓库库区",
                "工作台编号",
                "工作台名称",
                "工作台类型",
                "AGV排队上限",
                "排队货架类型",
                "备注信息",
                "状态"
            );
            List<LinkedHashMap<String, Object>> dataList = workbenchService.queryWorkbenchForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "工作台", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 工作类型 的下拉框数据
     *
     * @return
     */
    @ApiOperation("工作类型的下拉框数据")
    @GetMapping(path = "/queryWorkbenchType")
    public BaseResult<Object> queryWorkbenchType() {
        return BaseResult.ok(getDictMap("workbench_type"));
    }

    /**
     * AGV排队上限、排队货架类型 的下拉框数据
     *
     * @return
     */
    @ApiOperation("AGV排队上限、排队货架类型 的下拉框数据")
    @GetMapping(path = "/queryAgvAndQueue")
    public BaseResult<Object> queryAgvAndQueue() {
        Map<String, Object> data = MapUtil.newHashMap();
        //AGV排队上限
        data.put("agvData",getDictMap("agv_queue_upper"));
        //排队货架类型
        data.put("queueData",getDictMap("queue_up_type"));
        return BaseResult.ok(data);
    }

    /**
     * 【入库】
     * 工作台收货上架
     *     1、货架号 查询 货架示意图
     *     2、货架号+物料编号(货主、批次、生产日期、数量、单位)+库位编号 确认 添加收货记录(本次收货信息)(前端)
     *     3、选择记录，撤销 收货记录(前端)
     *     4、货架回库
     *         4.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-上架）
     *         4.2、自动生成收货单以及上架任务明细
     *             4.2.1、生成收货通知单
     *             4.2.2、生成收货单
     *             4.2.3、生成收货单对应的上架任务明细
     *
     */

    /**
     * 工作台收货上架:货架号 查询 货架示意图
     */
    @ApiOperation("工作台收货上架:货架号 查询 货架示意图")
    @PostMapping(path = "/selectSketchMapByShelf")
    public BaseResult<List<WarehouseShelfSketchMapVO>> selectSketchMapByShelf(@RequestBody QueryWarehouseShelfForm form){
        List<WarehouseShelfSketchMapVO> sketchMapList = workbenchService.selectSketchMapByShelf(form);
        return BaseResult.ok(sketchMapList);
    }

    /**
     * 工作台收货上架:工作台货架回库
     *     4、货架回库
     *         4.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-上架）
     *         4.2、自动生成收货单以及上架任务明细
     *             4.2.1、生成收货通知单
     *             4.2.2、生成收货单
     *             4.2.3、生成收货单对应的上架任务明细
     */
    @ApiOperation("上架回库，货架回库")
    @PostMapping("/putawayBackLibrary")
    public BaseResult putawayBackLibrary(@RequestBody PutawayBackLibraryForm form){
        workbenchService.putawayBackLibrary(form);
        return BaseResult.ok("操作成功");
    }

//    /**
//     * 【出库】
//     * 工作台拣货出库
//     *     1、货架号 查询 货架示意图
//     *     2、货架号+物料编号+库位编号 确认 出库任务信息(拣货出库任务信息)
//     *     3、货架回库
//     *         3.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-下架）
//     */


    private List<LinkedHashMap<String, Object>> getDictMap(String dictCode){
        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();
        List<SysDictItem> itemList = authClient.selectItemByDictCode(dictCode).getResult();
        itemList.forEach(item -> {
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",item.getItemText());
            maps.put("value",item.getItemValue());
            dataList.add(maps);
        });
        return dataList;
    }

}
