package com.jayud.wms.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.wms.model.po.WmsMaterialBarCode;
import com.jayud.wms.service.IWmsMaterialBarCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
/**
 * 物料-条码 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "物料-条码")
@RestController
@RequestMapping("/wmsMaterialBarCode")
public class WmsMaterialBarCodeController {

    @Autowired
    public IWmsMaterialBarCodeService wmsMaterialBarCodeService;



    /**
     * 分页查询数据
     *
     * @param wmsMaterialBarCode   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public ListPageRuslt<WmsMaterialBarCode> selectPage(WmsMaterialBarCode wmsMaterialBarCode,
                                                        HttpServletRequest req) {
        return PaginationBuilder.buildPageResult(wmsMaterialBarCodeService.selectPage(wmsMaterialBarCode,req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsMaterialBarCode   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsMaterialBarCode>> selectList(WmsMaterialBarCode wmsMaterialBarCode,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialBarCodeService.selectList(wmsMaterialBarCode));
    }

    /**
    * 新增
    * @param wmsMaterialBarCode
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsMaterialBarCode wmsMaterialBarCode ){
        wmsMaterialBarCodeService.save(wmsMaterialBarCode);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsMaterialBarCode
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsMaterialBarCode wmsMaterialBarCode ){
        wmsMaterialBarCodeService.updateById(wmsMaterialBarCode);
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
        wmsMaterialBarCodeService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsMaterialBarCode> queryById(@RequestParam(name="id",required=true) int id) {
        WmsMaterialBarCode wmsMaterialBarCode = wmsMaterialBarCodeService.getById(id);
        return BaseResult.ok(wmsMaterialBarCode);
    }

}
