package com.jayud.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysTenantRoleService;
import com.jayud.auth.model.po.SysTenantRole;

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
 * 租户规则配置 控制类
 *
 * @author jayud
 * @since 2022-03-05
 */
@Slf4j
@Api(tags = "租户规则配置")
@RestController
@RequestMapping("/sysTenantRole")
public class SysTenantRoleController {



    @Autowired
    public ISysTenantRoleService sysTenantRoleService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-05
     * @param: sysTenantRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenantRole>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysTenantRole>> selectPage(SysTenantRole sysTenantRole,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysTenantRoleService.selectPage(sysTenantRole,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-05
    * @param: sysTenantRole
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysTenantRole>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysTenantRole>> selectList(SysTenantRole sysTenantRole,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysTenantRoleService.selectList(sysTenantRole));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-05
    * @param: sysTenantRole
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysTenantRole sysTenantRole ){
        sysTenantRoleService.save(sysTenantRole);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-05
     * @param: sysTenantRole
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysTenantRole sysTenantRole ){
        sysTenantRoleService.updateById(sysTenantRole);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysTenantRoleService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysTenantRoleService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysTenantRole>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysTenantRole> queryById(@RequestParam(name="id",required=true) int id) {
        SysTenantRole sysTenantRole = sysTenantRoleService.getById(id);
        return BaseResult.ok(sysTenantRole);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-05
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出租户规则配置")
    @PostMapping(path = "/exportSysTenantRole")
    public void exportSysTenantRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "租户编码",
                "是否显示公海客户",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = sysTenantRoleService.querySysTenantRoleForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "租户规则配置", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
