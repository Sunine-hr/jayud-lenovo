package com.jayud.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.service.ISysDictItemService;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysDictService;
import com.jayud.auth.model.po.SysDict;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 字典 控制类
 *
 * @author jayud
 * @since 2022-02-23
 */
@Slf4j
@Api(tags = "字典")
@RestController
@RequestMapping("/sysDict")
public class SysDictController {


    @Autowired
    public ISysDictService sysDictService;
    @Autowired
    public ISysDictItemService sysDictItemService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-23
     * @param: sysDict
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysDict>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysDict>> selectPage(SysDict sysDict,
                                                 @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        sysDict.setIsDeleted(false);
        return BaseResult.ok(sysDictService.selectPage(sysDict, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-23
     * @param: sysDict
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysDict>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysDict>> selectList(SysDict sysDict,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysDictService.selectList(sysDict));
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-23
     * @param: sysDict
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysDict sysDict) {
        if (StringUtils.isEmpty(sysDict.getDictName()) || StringUtils.isEmpty(sysDict.getDictCode())) {
            throw new JayudBizException("请填写字典名称/字典描述");
        }

        this.sysDictService.checkUnique(sysDict);
        sysDictService.save(sysDict);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-02-23
     * @param: sysDict
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysDict sysDict) {
        if (StringUtils.isEmpty(sysDict.getDictName()) || StringUtils.isEmpty(sysDict.getDictCode())) {
            throw new JayudBizException("请填写字典名称/字典描述");
        }
        this.sysDictService.checkUnique(sysDict);
        sysDictService.updateById(sysDict);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id) {
//        sysDictService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        if (this.sysDictItemService.count(new QueryWrapper<>(new SysDictItem().setIsDeleted(false).setDictId(id))) > 0) {
            return BaseResult.error("请先删除数据字典");
        }
        sysDictService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 批量逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("批量逻辑删除")
//    @PostMapping("/batchLogicDel")
//    public BaseResult batchLogicDel(@RequestBody List<SysDict> sysDicts) {
//        List<Long> sysDictIds = sysDicts.stream().map(e -> e.getId()).collect(Collectors.toList());
//        List<SysDict> tmps = new ArrayList<>();
//        for (Long sysDictId : sysDictIds) {
//            SysDict tmp = new SysDict();
//            tmp.setIsDeleted(true).setId(sysDictId);
//            tmps.add(tmp);
//        }
//        this.sysDictService.updateBatchById(tmps);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysDict>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysDict> queryById(@RequestParam(name = "id", required = true) int id) {
        SysDict sysDict = sysDictService.getById(id);
        return BaseResult.ok(sysDict);
    }


}
