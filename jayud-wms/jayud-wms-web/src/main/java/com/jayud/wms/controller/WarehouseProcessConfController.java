package com.jayud.wms.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.wms.model.po.WarehouseProcessConf;
import com.jayud.wms.service.IWarehouseProcessConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 仓库流程配置 控制类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Api(tags = "仓库流程配置")
@RestController
@RequestMapping("/warehouseProcessConf")
public class WarehouseProcessConfController {

    @Autowired
    public IWarehouseProcessConfService warehouseProcessConfService;



    /**
     * 分页查询数据
     *
     * @param warehouseProcessConf 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<WarehouseProcessConf>> selectPage(WarehouseProcessConf warehouseProcessConf,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        return BaseResult.ok(warehouseProcessConfService.selectPage(warehouseProcessConf, pageNo, pageSize, req));
    }

    /**
     * 列表查询数据
     *
     * @param warehouseProcessConf 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WarehouseProcessConf>> selectList(WarehouseProcessConf warehouseProcessConf,
                                                         HttpServletRequest req) {
        return BaseResult.ok(warehouseProcessConfService.selectList(warehouseProcessConf));
    }


    /**
     * 查询流程配置
     *
     * @param warehouseProcessConf 查询条件
     * @return
     */
    @ApiOperation("查询流程配置")
    @GetMapping("/getConf")
    public BaseResult<List<WarehouseProcessConf>> getConf(WarehouseProcessConf warehouseProcessConf,
                                                      HttpServletRequest req) {
        List<WarehouseProcessConf> warehouseProcessConfs = warehouseProcessConfService.selectList(warehouseProcessConf);
        return BaseResult.ok(warehouseProcessConfs);
    }

    /**
     * 新增
     *
     * @param warehouseProcessConf
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WarehouseProcessConf warehouseProcessConf) {
        this.warehouseProcessConfService.update(new WarehouseProcessConf().setIsDeleted(true),
                new QueryWrapper<>(new WarehouseProcessConf().setWarehouseId(warehouseProcessConf.getWarehouseId()).setType(warehouseProcessConf.getType())));
        warehouseProcessConfService.save(warehouseProcessConf);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     *
     * @param warehouseProcessConf
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WarehouseProcessConf warehouseProcessConf) {
        warehouseProcessConfService.updateById(warehouseProcessConf);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     **/
    @ApiOperation("删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id) {
        warehouseProcessConfService.removeById(id);
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
    public BaseResult<WarehouseProcessConf> queryById(@RequestParam(name = "id", required = true) int id) {
        WarehouseProcessConf warehouseProcessConf = warehouseProcessConfService.getById(id);
        return BaseResult.ok(warehouseProcessConf);
    }


    /**
     * 批量更新"
     *
     * @param warehouseProcessConfs
     **/
    @ApiOperation("批量更新")
    @PostMapping("/batchUpdate")
    public BaseResult batchUpdate(@Valid @RequestBody List<WarehouseProcessConf> warehouseProcessConfs) {
        if (CollectionUtil.isEmpty(warehouseProcessConfs)) {
            return BaseResult.error("不存在流程配置");
        }
//        this.warehouseProcessConfService.update(new WarehouseProcessConf().setIsDeleted(true),
//                new QueryWrapper<>(new WarehouseProcessConf().setWarehouseId(warehouseProcessConfs.get(0).getWarehouseId()).setType(warehouseProcessConfs.get(0).getType())));
//        warehouseProcessConfService.saveBatch(warehouseProcessConfs);
        this.warehouseProcessConfService.updateBatchById(warehouseProcessConfs);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

}
