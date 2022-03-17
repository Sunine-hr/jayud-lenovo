package com.jayud.wms.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.wms.model.po.WmsMaterialPackingSpecs;
import com.jayud.wms.service.IWmsMaterialPackingSpecsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
/**
 * 物料-包装规格 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "物料-包装规格")
@RestController
@RequestMapping("/wmsMaterialPackingSpecs")
public class WmsMaterialPackingSpecsController {

    @Autowired
    public IWmsMaterialPackingSpecsService wmsMaterialPackingSpecsService;



    /**
     * 分页查询数据
     *
     * @param wmsMaterialPackingSpecs   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public ListPageRuslt<WmsMaterialPackingSpecs> selectPage(WmsMaterialPackingSpecs wmsMaterialPackingSpecs,
                                                             HttpServletRequest req) {
        return PaginationBuilder.buildPageResult(wmsMaterialPackingSpecsService.selectPage(wmsMaterialPackingSpecs,req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsMaterialPackingSpecs   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsMaterialPackingSpecs>> selectList(WmsMaterialPackingSpecs wmsMaterialPackingSpecs,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialPackingSpecsService.selectList(wmsMaterialPackingSpecs));
    }

    /**
    * 新增
    * @param wmsMaterialPackingSpecs
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsMaterialPackingSpecs wmsMaterialPackingSpecs ){
        wmsMaterialPackingSpecsService.save(wmsMaterialPackingSpecs);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsMaterialPackingSpecs
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsMaterialPackingSpecs wmsMaterialPackingSpecs ){
        wmsMaterialPackingSpecsService.updateById(wmsMaterialPackingSpecs);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 删除
     * @param id
     **/
    @ApiOperation("删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        wmsMaterialPackingSpecsService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsMaterialPackingSpecs> queryById(@RequestParam(name="id",required=true) int id) {
        WmsMaterialPackingSpecs wmsMaterialPackingSpecs = wmsMaterialPackingSpecsService.getById(id);
        return BaseResult.ok(wmsMaterialPackingSpecs);
    }


    /**
     * @description 获取初始化数据
     * @author  ciro
     * @date   2021/12/17 9:33
     * @param:
     * @return: com.jyd.component.commons.result.Result<java.util.List<com.jayud.model.po.WmsMaterialPackingSpecs>>
     **/
    @ApiOperation("获取初始化数据")
    @GetMapping(value = "/getInitList")
    public BaseResult<List<WmsMaterialPackingSpecs>> getInitList() {
        return BaseResult.ok(wmsMaterialPackingSpecsService.getInitList());
    }

    @ApiOperation("根据物料id查询单位")
    @GetMapping(value = "/getByMaterialId")
    public BaseResult<List<String>> getByMaterialId(long materialId) {
        return BaseResult.ok(wmsMaterialPackingSpecsService.getUnitByMaterialId(materialId));
    }


}
