package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysAreaService;
import com.jayud.auth.model.po.SysArea;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 系统-中国行政地区表 控制类
 *
 * @author jayud
 * @since 2022-02-26
 */
@Slf4j
@Api(tags = "系统-中国行政地区表")
@RestController
@RequestMapping("/sysArea")
public class SysAreaController {


    @Autowired
    public ISysAreaService sysAreaService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-26
     * @param: sysArea
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysArea>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysArea>> selectPage(SysArea sysArea,
                                                 @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        return BaseResult.ok(sysAreaService.selectPage(sysArea, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-26
     * @param: sysArea
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysArea>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysArea>> selectList(SysArea sysArea,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysAreaService.selectList(sysArea));
    }

    @ApiOperation("列表查询数据远程调用Feign")
    @PostMapping("/api/selectListSysAreaFeign")
    public BaseResult selectListSysAreaFeign(@RequestParam(name = "level", required = false) Integer level, @RequestParam(name = "parentCode", required = false) Long parentCode) {
        SysArea sysArea = new SysArea();
        if (level != null) {
            sysArea.setLevel(level);
        }
        if (parentCode != null) {
            sysArea.setParentCode(parentCode);
        }
        return BaseResult.ok(sysAreaService.selectList(sysArea));
    }

    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-26
     * @param: sysArea
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysArea sysArea) {
        sysAreaService.save(sysArea);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-02-26
     * @param: sysArea
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysArea sysArea) {
        sysAreaService.updateById(sysArea);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-02-26
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id) {
        sysAreaService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-26
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        sysAreaService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-26
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysArea>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysArea> queryById(@RequestParam(name = "id", required = true) int id) {
        SysArea sysArea = sysAreaService.getById(id);
        return BaseResult.ok(sysArea);
    }


}
