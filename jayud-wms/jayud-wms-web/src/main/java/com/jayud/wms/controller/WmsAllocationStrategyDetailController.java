package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsAllocationStrategyDetail;
import com.jayud.wms.model.vo.WmsAllocationStrategyDetailVO;
import com.jayud.wms.service.IWmsAllocationStrategyDetailService;
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
 * 分配策略详情 控制类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Api(tags = "分配策略详情")
@RestController
@RequestMapping("/wmsAllocationStrategyDetail")
public class WmsAllocationStrategyDetailController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsAllocationStrategyDetailService wmsAllocationStrategyDetailService;

    /**
     * 分页查询数据
     *
     * @param wmsAllocationStrategyDetailVO   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsAllocationStrategyDetailVO>>> selectPage(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO,
                                                                                     @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                     HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsAllocationStrategyDetailService.selectPage(wmsAllocationStrategyDetailVO,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsAllocationStrategyDetailVO   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsAllocationStrategyDetailVO>> selectList(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsAllocationStrategyDetailService.selectList(wmsAllocationStrategyDetailVO));
    }

    /**
    * 新增
    * @param wmsAllocationStrategyDetail
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsAllocationStrategyDetail wmsAllocationStrategyDetail ){
        wmsAllocationStrategyDetailService.save(wmsAllocationStrategyDetail);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsAllocationStrategyDetail
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsAllocationStrategyDetail wmsAllocationStrategyDetail ){
        wmsAllocationStrategyDetailService.updateById(wmsAllocationStrategyDetail);
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
        wmsAllocationStrategyDetailService.phyDelById(id);
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
        wmsAllocationStrategyDetailService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsAllocationStrategyDetail> queryById(@RequestParam(name="id",required=true) int id) {
        WmsAllocationStrategyDetail wmsAllocationStrategyDetail = wmsAllocationStrategyDetailService.getById(id);
        return BaseResult.ok(wmsAllocationStrategyDetail);
    }

    /**
     * 根据查询条件导出分配策略详情
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出分配策略详情")
    @PostMapping(path = "/exportWmsAllocationStrategyDetail")
    public void exportWmsAllocationStrategyDetail(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "分配策略编号",
                "分配策略详情编号",
                "排序",
                "策略类型(1 路线顺序,2 清空库位-由低到高)",
                "计量单位",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsAllocationStrategyDetailService.queryWmsAllocationStrategyDetailForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "分配策略详情", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("保存详情")
    @PostMapping("/saveStrategyDetail")
    public BaseResult saveStrategyDetail(@Valid @RequestBody WmsAllocationStrategyDetail wmsAllocationStrategyDetail ){
        return wmsAllocationStrategyDetailService.saveStrategyDetail(wmsAllocationStrategyDetail);
    }

}
