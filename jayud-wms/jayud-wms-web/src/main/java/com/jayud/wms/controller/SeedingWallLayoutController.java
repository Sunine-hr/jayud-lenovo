package com.jayud.wms.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.SeedingWallLayout;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SeedingWallLayoutVo;
import com.jayud.wms.service.ISeedingWallLayoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 播种位布局 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "播种位布局")
@RestController
@RequestMapping("/seedingWallLayout")
public class SeedingWallLayoutController {

    @Autowired
    public ISeedingWallLayoutService seedingWallLayoutService;


    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SeedingWallLayout> queryById(@RequestParam(name = "id", required = true) int id) {
        SeedingWallLayout seedingWallLayout = seedingWallLayoutService.getById(id);
        return BaseResult.ok(seedingWallLayout);
    }

    /**
     * 根据播种墙id,获取播种墙的播种位布局
     */
    @ApiOperation("根据播种墙id,获取播种墙的播种位布局")
    @ApiImplicitParam(name = "seedingWallId", value = "播种墙id", dataType = "int", required = true)
    @GetMapping(value = "/queryBySeedingWallId")
    public BaseResult<SeedingWallLayoutVo> queryBySeedingWallId(@RequestParam(name = "seedingWallId", required = true) int seedingWallId) {
        SeedingWallLayoutVo seedingWallLayoutVo = seedingWallLayoutService.queryBySeedingWallId(seedingWallId);
        return BaseResult.ok(seedingWallLayoutVo);
    }

    /**
     * 保存播种墙的播种位布局
     */
    @ApiOperation("保存播种墙的播种位布局")
    @PostMapping("/saveSeedingWallLayout")
    public BaseResult saveSeedingWallLayout(@RequestBody SeedingWallLayoutVo bo) {
        seedingWallLayoutService.saveSeedingWallLayout(bo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);

    }

    @ApiOperation("播种位示意图")
    @PostMapping(path = "/sketchMap")
    public BaseResult<List<SeedingWallLayoutTwoVo>> sketchMap() {
        return BaseResult.ok(this.seedingWallLayoutService.sketchMap(CurrentUserUtil.getCurrrentUserWorkbenchCode(), 1L));
    }

}
