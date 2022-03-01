package com.jayud.auth.controller;

import com.jayud.auth.model.po.SysDict;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysDictItemService;
import com.jayud.auth.model.po.SysDictItem;

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
 * 字典项 控制类
 *
 * @author jayud
 * @since 2022-02-23
 */
@Slf4j
@Api(tags = "字典项")
@RestController
@RequestMapping("/sysDictItem")
public class SysDictItemController {


    @Autowired
    public ISysDictItemService sysDictItemService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-23
     * @param: sysDictItem
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysDictItem>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysDictItem>> selectPage(SysDictItem sysDictItem,
                                                     @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {


        return BaseResult.ok(sysDictItemService.selectPage(sysDictItem, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-23
     * @param: sysDictItem
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysDictItem>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysDictItem>> selectList(SysDictItem sysDictItem,
                                                    HttpServletRequest req) {
        return BaseResult.ok(sysDictItemService.selectList(sysDictItem));
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-23
     * @param: sysDictItem
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysDictItem sysDictItem) {
        if (StringUtils.isEmpty(sysDictItem.getItemText()) || StringUtils.isEmpty(sysDictItem.getItemValue())) {
            throw new JayudBizException("请填写字典类别/编码");
        }
        this.sysDictItemService.checkUnique(sysDictItem);
        sysDictItemService.save(sysDictItem);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-02-23
     * @param: sysDictItem
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysDictItem sysDictItem) {
        if (StringUtils.isEmpty(sysDictItem.getItemText()) || StringUtils.isEmpty(sysDictItem.getItemValue())) {
            throw new JayudBizException("请填写字典类别/编码");
        }
        sysDictItemService.updateById(sysDictItem);
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
//        sysDictItemService.phyDelById(id);
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
        sysDictItemService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 批量逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("批量逻辑删除")
    @PostMapping("/batchLogicDel")
    public BaseResult batchLogicDel(@RequestBody List<SysDictItem> sysDictItems) {
        List<Long> ids = sysDictItems.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<SysDictItem> tmps = new ArrayList<>();
        for (Long id : ids) {
            SysDictItem tmp = new SysDictItem();
            tmp.setIsDeleted(true).setId(id);
            tmps.add(tmp);
        }
        this.sysDictItemService.updateBatchById(tmps);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysDictItem>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysDictItem> queryById(@RequestParam(name = "id", required = true) int id) {
        SysDictItem sysDictItem = sysDictItemService.getById(id);
        return BaseResult.ok(sysDictItem);
    }

    @ApiOperation("根据字典编码查询子项")
    @ApiImplicitParam(name = "dictCode", value = "字典编码", dataType = "String", required = true)
    @GetMapping("/selectItemByDictCode")
    public BaseResult<List<SysDictItem>> selectItemByDictCode(String dictCode,
                                                              HttpServletRequest req) {
        return BaseResult.ok(sysDictItemService.selectItemByDictCode(dictCode));
    }

    @ApiOperation("根据字典编码查询子项")
    @PostMapping(value = "/api/selectItemByDictCode")
    public BaseResult selectItemByDictCodeFeign(@RequestParam String dictCode,
                                                              HttpServletRequest req) {
        List<SysDictItem> sysDictItems = sysDictItemService.selectItemByDictCode(dictCode);
        System.out.println("字典表："+sysDictItems);
        return BaseResult.ok(sysDictItems);
    }

}
