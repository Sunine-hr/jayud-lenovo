package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsAllocationStrategy;
import com.jayud.wms.service.IWmsAllocationStrategyService;
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
 * 分配策略 控制类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Api(tags = "分配策略")
@RestController
@RequestMapping("/wmsAllocationStrategy")
public class WmsAllocationStrategyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsAllocationStrategyService wmsAllocationStrategyService;

    /**
     * 分页查询数据
     *
     * @param wmsAllocationStrategy   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsAllocationStrategy>>> selectPage(WmsAllocationStrategy wmsAllocationStrategy,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsAllocationStrategyService.selectPage(wmsAllocationStrategy,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsAllocationStrategy   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsAllocationStrategy>> selectList(WmsAllocationStrategy wmsAllocationStrategy,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsAllocationStrategyService.selectList(wmsAllocationStrategy));
    }

    /**
    * 新增
    * @param wmsAllocationStrategy
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsAllocationStrategy wmsAllocationStrategy ){
        return wmsAllocationStrategyService.saveMsg(wmsAllocationStrategy);
    }

    /**
     * 编辑
     * @param wmsAllocationStrategy
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsAllocationStrategy wmsAllocationStrategy ){
        return wmsAllocationStrategyService.saveMsg(wmsAllocationStrategy);
    }


    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam int id){
        wmsAllocationStrategyService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 逻辑删除
    * @param id
    **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        return wmsAllocationStrategyService.logicDel(id);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsAllocationStrategy> queryById(@RequestParam(name="id",required=true) int id) {
        WmsAllocationStrategy wmsAllocationStrategy = wmsAllocationStrategyService.getById(id);
        return BaseResult.ok(wmsAllocationStrategy);
    }

    /**
     * 根据查询条件导出分配策略
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出分配策略")
    @PostMapping(path = "/exportWmsAllocationStrategy")
    public void exportWmsAllocationStrategy(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "分配策略编号",
                "分配策略名称",
                "自定义类型(1默认,2自定义)",
                "排序",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsAllocationStrategyService.queryWmsAllocationStrategyForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "分配策略", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("保存策略")
    @PostMapping("/saveStrategy")
    public BaseResult saveStrategy(@Valid @RequestBody WmsAllocationStrategy wmsAllocationStrategy ){
        return wmsAllocationStrategyService.saveStrategy(wmsAllocationStrategy);
    }

    @GetMapping("/initStrategy")
    public BaseResult initStrategy(String materialCode,String owerCode,String warehouseCode){
        return wmsAllocationStrategyService.initStrategy(materialCode,owerCode,warehouseCode);
    }



}
