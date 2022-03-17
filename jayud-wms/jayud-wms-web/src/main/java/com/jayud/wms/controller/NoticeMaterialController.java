package com.jayud.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.wms.model.po.NoticeMaterial;
import com.jayud.wms.service.INoticeMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
/**
 * 通知单物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "通知单物料信息")
@RestController
@RequestMapping("/noticeMaterial")
public class NoticeMaterialController {

    @Autowired
    public INoticeMaterialService noticeMaterialService;



    /**
     * 分页查询数据
     *
     * @param noticeMaterial   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<NoticeMaterial>> selectPage(NoticeMaterial noticeMaterial,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(noticeMaterialService.selectPage(noticeMaterial,pageNo,pageSize,req));
    }

    /**
     * 列表查询数据
     *
     * @param noticeMaterial   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<NoticeMaterial>> selectList(NoticeMaterial noticeMaterial,
                                                HttpServletRequest req) {
        return BaseResult.ok(noticeMaterialService.selectList(noticeMaterial));
    }

    /**
    * 新增
    * @param noticeMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody NoticeMaterial noticeMaterial ){
        noticeMaterialService.save(noticeMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param noticeMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody NoticeMaterial noticeMaterial ){
        noticeMaterialService.updateById(noticeMaterial);
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
        noticeMaterialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<NoticeMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        NoticeMaterial noticeMaterial = noticeMaterialService.getById(id);
        return BaseResult.ok(noticeMaterial);
    }

}
