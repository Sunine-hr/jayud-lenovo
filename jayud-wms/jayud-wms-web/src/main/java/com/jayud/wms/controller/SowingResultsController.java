package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.QuerySowingResultsForm;
import com.jayud.wms.model.po.SowingResults;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SowingResultsVO;
import com.jayud.wms.service.ISowingResultsService;
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
 * 播种结果 控制类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Api(tags = "播种结果")
@RestController
@RequestMapping("/sowingResults")
public class SowingResultsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ISowingResultsService sowingResultsService;

    /**
     * 分页查询数据
     *
     * @param querySowingResultsForm   查询条件
     * @return
     */
    @SysDataPermission(clazz = QuerySowingResultsForm.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<SowingResultsVO>> selectPage(QuerySowingResultsForm querySowingResultsForm,
                                                     HttpServletRequest req) {
        return BaseResult.ok(sowingResultsService.selectPage(querySowingResultsForm,querySowingResultsForm.getCurrentPage(),querySowingResultsForm.getPageSize(),req));
    }

    /**
     * 列表查询数据
     *
     * @param querySowingResultsForm   查询条件
     * @return
     */
    @SysDataPermission(clazz = QuerySowingResultsForm.class)
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<SowingResultsVO>> selectList(QuerySowingResultsForm querySowingResultsForm,
                                                HttpServletRequest req) {
        return BaseResult.ok(sowingResultsService.selectList(querySowingResultsForm));
    }


    @ApiOperation("播种位示意图")
    @PostMapping(path = "/sketchMap")
    public BaseResult<List<SeedingWallLayoutTwoVo>> sketchMap() {
        return BaseResult.ok(this.sowingResultsService.sketchMap());
    }


    @ApiOperation("取消")
    @GetMapping("/cancel")
    public BaseResult cancel(@RequestParam Map<String,Object> map){
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        sowingResultsService.cancel(id);
        return BaseResult.ok();
    }

    /**
    * 新增
    * @param sowingResults
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SowingResults sowingResults ){
        sowingResultsService.save(sowingResults);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param sowingResults
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SowingResults sowingResults ){
        sowingResultsService.updateById(sowingResults);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param sowingResults
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody SowingResults sowingResults ){
        SowingResults sowingResults1 = sowingResultsService.saveOrUpdateSowingResults(sowingResults);
        return BaseResult.ok(sowingResults1);
    }

    /**
     * 物理删除
     * @param id
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/del")
//    public BaseResult del(@RequestParam int id){
//        sowingResultsService.removeById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }



    /**
     * 逻辑删除
     * @param id
     **/
//    @ApiOperation("逻辑删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/logicDel")
//    public BaseResult logicDel(@RequestParam int id){
//        sowingResultsService.delSowingResults(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SowingResults> queryById(@RequestParam(name="id",required=true) int id) {
        SowingResults sowingResults = sowingResultsService.getById(id);
        return BaseResult.ok(sowingResults);
    }

    /**
     * 根据查询条件导出播种结果
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出播种结果")
    @PostMapping(path = "/exportSowingResults")
    public void exportSowingResults(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "播种位编号",
                "收货单id",
                "收货单号",
                "物料id",
                "物料编号",
                "物料名称",
                "原容器号",
                "数量",
                "播种数量",
                "播种后数量",
                "单位",
                "新容器号",
                "新的播种后数量",
                "状态(1:未更换,2:已更换,3:确认上架)",
                "租户编码",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = sowingResultsService.querySowingResultsForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "播种结果", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }




}
