package com.jayud.auth.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.entity.TreeNode;
import com.jayud.common.utils.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.IRegionCityService;
import com.jayud.auth.model.po.RegionCity;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 省市区关联表 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "省市区关联表")
@RestController
@RequestMapping("/regionCity")
public class RegionCityController {



    @Autowired
    public IRegionCityService regionCityService;

    @ApiOperation(value = "获取省市区树结构")
    @PostMapping(value = "/api/adrrTree")
    public CommonResult<List<TreeNode>> adrrTree() {
        List<RegionCity> list = this.regionCityService.list();
        List<TreeNode> treeNodes = new ArrayList<>();
        list.forEach(e -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(e.getId()).setLabel(e.getName()).setParentId(e.getParentId());
            treeNodes.add(treeNode);
        });
        List<TreeNode> tree = TreeUtil.getTree(treeNodes, 0L);
        return CommonResult.success(tree);
    }


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: regionCity
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.RegionCity>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<RegionCity>> selectPage(RegionCity regionCity,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(regionCityService.selectPage(regionCity,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: regionCity
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.RegionCity>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<RegionCity>> selectList(RegionCity regionCity,
                                                HttpServletRequest req) {
        return BaseResult.ok(regionCityService.selectList(regionCity));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: regionCity
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody RegionCity regionCity ){
        regionCityService.save(regionCity);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: regionCity
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody RegionCity regionCity ){
        regionCityService.updateById(regionCity);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        regionCityService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        regionCityService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.RegionCity>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<RegionCity> queryById(@RequestParam(name="id",required=true) int id) {
        RegionCity regionCity = regionCityService.getById(id);
        return BaseResult.ok(regionCity);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-11
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出省市区关联表")
    @PostMapping(path = "/exportRegionCity")
    public void exportRegionCity(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "CODE",
                "名称",
                "父级ID"
            );
            List<LinkedHashMap<String, Object>> dataList = regionCityService.queryRegionCityForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "省市区关联表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
